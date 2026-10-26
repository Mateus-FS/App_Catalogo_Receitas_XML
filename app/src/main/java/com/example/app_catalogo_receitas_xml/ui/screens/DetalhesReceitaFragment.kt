package com.example.app_catalogo_receitas_xml.ui.screens

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.model.Receita
import com.example.app_catalogo_receitas_xml.ui.components.MiniaturaReceitaView
import com.google.android.material.color.MaterialColors

/**
 * Equivalente XML de DetalhesReceitaScreen.
 *
 * Uso:
 *   DetalhesReceitaFragment.newInstance(receita, favorita = true)
 *
 * A Activity host deve implementar [DetalhesReceitaFragment.Listener].
 */
class DetalhesReceitaFragment : Fragment() {

    interface Listener {
        fun onVoltar()
        fun onToggleFavorito(receita: Receita)
        fun ehFavorita(receita: Receita): Boolean
    }

    private var receita: Receita? = null
    private var favorita = false

    private val listener get() = activity as? Listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        receita  = arguments?.getParcelable(ARG_RECEITA)
        favorita = arguments?.getBoolean(ARG_FAVORITA, false) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_detalhes_receita, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receita = receita ?: return

        // Estado atual vem da Activity (os argumentos só guardam o valor de quando a tela abriu
        // e ficam desatualizados após alternar o favorito ou recriar a Activity).
        favorita = listener?.ehFavorita(receita) ?: favorita

        // ── Header colorido ──────────────────────────────────────────
        val estilo = MiniaturaReceitaView.estiloDaCategoria(receita.categoria)
        val header = view.findViewById<FrameLayout>(R.id.headerCategoria)
        val fundoDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(requireContext(), estilo.fundoRes))
        }
        header.background = fundoDrawable

        view.findViewById<TextView>(R.id.txtEmojiHeader).text = estilo.emoji

        // ── Botão Voltar ──────────────────────────────────────────────
        view.findViewById<ImageButton>(R.id.btnVoltar).setOnClickListener {
            listener?.onVoltar()
        }

        // ── Botão Favorito ────────────────────────────────────────────
        val btnFavorito = view.findViewById<ImageButton>(R.id.btnFavoritoDetalhe)
        atualizarIconeFavorito(btnFavorito)
        btnFavorito.setOnClickListener {
            listener?.onToggleFavorito(receita)
        }

        // ── Textos de cabeçalho ───────────────────────────────────────
        view.findViewById<TextView>(R.id.txtCategoria).apply {
            isVisible = receita.categoria.isNotBlank()
            text = receita.categoria
        }

        view.findViewById<TextView>(R.id.txtTituloReceita).text = receita.titulo

        val resumo = listOf(
            receita.tempo,
            receita.dificuldade,
            if (receita.porcoes > 0) "${receita.porcoes} porções" else ""
        ).filter { it.isNotBlank() }.joinToString(" · ")

        view.findViewById<TextView>(R.id.txtResumo).apply {
            isVisible = resumo.isNotEmpty()
            text = resumo
        }

        view.findViewById<TextView>(R.id.txtDescricao).apply {
            isVisible = receita.descricao.isNotBlank()
            text = receita.descricao
        }

        // ── Ingredientes ──────────────────────────────────────────────
        val corTexto = MaterialColors.getColor(
            view, com.google.android.material.R.attr.colorOnBackground
        )
        val containerIngredientes = view.findViewById<LinearLayout>(R.id.containerIngredientes)
        receita.ingredientes.forEach { ingrediente ->
            adicionarLinhaTexto(containerIngredientes, "•", ingrediente, corTexto)
        }

        // ── Passos ────────────────────────────────────────────────────
        val containerPassos = view.findViewById<LinearLayout>(R.id.containerPassos)
        receita.passos.forEachIndexed { index, passo ->
            adicionarLinhaTexto(containerPassos, "${index + 1}.", passo, corTexto)
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────

    /** Alterna o ícone e tint do botão favorito conforme o estado atual. */
    fun setFavorita(isFavorita: Boolean) {
        favorita = isFavorita
        view?.findViewById<ImageButton>(R.id.btnFavoritoDetalhe)?.let {
            atualizarIconeFavorito(it)
        }
    }

    private fun atualizarIconeFavorito(btn: ImageButton) {
        if (favorita) {
            btn.setImageResource(R.drawable.ic_favorite)
            btn.setColorFilter(ContextCompat.getColor(requireContext(), R.color.favorito))
            btn.contentDescription = getString(R.string.cd_remover_favorito)
        } else {
            btn.setImageResource(R.drawable.ic_favorite_border)
            btn.clearColorFilter()
            btn.contentDescription = getString(R.string.cd_adicionar_favorito)
        }
    }

    /**
     * Adiciona uma linha "prefixo + texto" no estilo bodyLarge
     * ao container informado (equivale ao Row { Text("•") Text(ingrediente) } do Compose).
     */
    private fun adicionarLinhaTexto(
        container: LinearLayout,
        prefixo: String,
        texto: String,
        @ColorInt cor: Int
    ) {
        val row = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val txtPrefixo = TextView(requireContext()).apply {
            this.text = prefixo
            setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyLarge)
            setTextColor(cor)
            layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.prefixo_largura),
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val txtConteudo = TextView(requireContext()).apply {
            this.text = texto
            setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyLarge)
            setTextColor(cor)
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        row.addView(txtPrefixo)
        row.addView(txtConteudo)
        container.addView(row)
    }

    companion object {
        private const val ARG_RECEITA  = "receita"
        private const val ARG_FAVORITA = "favorita"

        fun newInstance(receita: Receita, favorita: Boolean = false) =
            DetalhesReceitaFragment().apply {
                arguments = bundleOf(
                    ARG_RECEITA  to receita,
                    ARG_FAVORITA to favorita
                )
            }
    }
}

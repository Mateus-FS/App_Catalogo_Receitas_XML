package com.example.app_catalogo_receitas_xml.ui.screens

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.databinding.FragmentDetalhesReceitaBinding
import com.example.app_catalogo_receitas_xml.model.Receita
import com.example.app_catalogo_receitas_xml.ui.components.LinhaReceitaView
import com.example.app_catalogo_receitas_xml.ui.viewmodel.ReceitasViewModel
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.launch

/**
 * Tela de detalhes de uma receita.
 *
 * Usa o [ReceitasViewModel] compartilhado e recebe apenas o ID da receita via arguments.
 */
class DetalhesReceitaFragment : Fragment(R.layout.fragment_detalhes_receita) {

    private val viewModel: ReceitasViewModel by activityViewModels()

    private var _binding: FragmentDetalhesReceitaBinding? = null
    private val binding get() = _binding!!

    private var receitaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receitaId = arguments?.getInt(ARG_RECEITA_ID) ?: -1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetalhesReceitaBinding.bind(view)

        val receita = viewModel.receitaPorId(receitaId) ?: return

        configurarHeader(receita)
        configurarBotoes(receita)
        configurarTextos(receita)
        configurarIngredientesEPassos(receita)
        observarFavorito(receita)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // ── Configuração das views ────────────────────────────────────────────

    private fun configurarHeader(receita: Receita) {
        val fundoDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(requireContext(), receita.categoria.fundoRes))
        }
        binding.headerCategoria.background = fundoDrawable
        binding.txtEmojiHeader.text = receita.categoria.emoji
    }

    private fun configurarBotoes(receita: Receita) {
        binding.btnVoltar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.btnFavoritoDetalhe.setOnClickListener {
            viewModel.alternarFavorito(receita.id)
        }
    }

    private fun configurarTextos(receita: Receita) {
        binding.txtCategoria.apply {
            isVisible = receita.categoria.titulo.isNotBlank()
            text = receita.categoria.titulo
        }

        binding.txtTituloReceita.text = receita.titulo

        val resumo = listOf(
            receita.tempo,
            receita.dificuldade,
            if (receita.porcoes > 0) "${receita.porcoes} porções" else ""
        ).filter { it.isNotBlank() }.joinToString(" · ")

        binding.txtResumo.apply {
            isVisible = resumo.isNotEmpty()
            text = resumo
        }

        binding.txtDescricao.apply {
            isVisible = receita.descricao.isNotBlank()
            text = receita.descricao
        }
    }

    private fun configurarIngredientesEPassos(receita: Receita) {
        val corTexto = MaterialColors.getColor(
            binding.root, com.google.android.material.R.attr.colorOnBackground
        )
        receita.ingredientes.forEach { ingrediente ->
            adicionarLinhaTexto("•", ingrediente, corTexto, toPassos = false)
        }
        receita.passos.forEachIndexed { index, passo ->
            adicionarLinhaTexto("${index + 1}.", passo, corTexto, toPassos = true)
        }
    }

    // ── Observadores ─────────────────────────────────────────────────────

    private fun observarFavorito(receita: Receita) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoritas.collect { favoritasList: List<Receita> ->
                    atualizarIconeFavorito(
                        binding.btnFavoritoDetalhe,
                        favoritasList.any { it.id == receita.id }
                    )
                }
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private fun atualizarIconeFavorito(btn: ImageButton, favorita: Boolean) {
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

    private fun adicionarLinhaTexto(
        prefixo: String,
        texto: String,
        @ColorInt cor: Int,
        toPassos: Boolean
    ) {
        val linhaView = LinhaReceitaView(requireContext()).apply {
            setLinha(prefixo, texto)
        }
        val container = if (toPassos) binding.containerPassos else binding.containerIngredientes
        container.addView(linhaView)
    }

    companion object {
        private const val ARG_RECEITA_ID = "receita_id"

        fun newInstance(receitaId: Int) =
            DetalhesReceitaFragment().apply {
                arguments = bundleOf(ARG_RECEITA_ID to receitaId)
            }
    }
}

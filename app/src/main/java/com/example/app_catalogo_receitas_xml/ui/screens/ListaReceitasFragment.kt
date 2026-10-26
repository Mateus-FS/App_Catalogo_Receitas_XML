package com.example.app_catalogo_receitas_xml.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.model.Receita
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

/**
 * Equivalente XML de ListaReceitasScreen.
 *
 * Os dados vêm da Activity host ([Listener.obterReceitas] / [Listener.obterFavoritosIds])
 * sempre que a view é criada; enquanto a tela está visível, a Activity chama
 * [updateFavoritos] quando os favoritos mudam.
 */
class ListaReceitasFragment : Fragment() {

    interface Listener {
        fun onReceitaClick(receita: Receita)
        fun onToggleFavorito(receita: Receita)
        fun obterReceitas(): List<Receita>
        fun obterFavoritosIds(): Set<Int>
    }

    // ── Dados ──────────────────────────────────────────────────────────
    private var todasReceitas: List<Receita> = emptyList()
    private var favoritos: Set<Int> = emptySet()

    private var buscaAtual = ""
    private var categoriaAtual = TODAS

    // ── Views ──────────────────────────────────────────────────────────
    private lateinit var recycler: RecyclerView
    private lateinit var txtVazio: TextView
    private lateinit var chipGroup: ChipGroup
    private lateinit var etBusca: TextInputEditText

    private val adapter by lazy {
        ReceitaAdapter(
            onClick         = { listener?.onReceitaClick(it) },
            onFavoritoClick = { listener?.onToggleFavorito(it) }
        )
    }

    private val listener get() = activity as? Listener

    // ── Ciclo de vida ──────────────────────────────────────────────────
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_lista_receitas, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler   = view.findViewById(R.id.recyclerReceitas)
        txtVazio   = view.findViewById(R.id.txtVazio)
        chipGroup  = view.findViewById(R.id.chipGroupCategorias)
        etBusca    = view.findViewById(R.id.etBusca)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        (recycler.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        recycler.addItemDecoration(
            SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.card_spacing))
        )

        // Reaplica a busca anterior (a view pode estar sendo recriada ao voltar dos detalhes)
        etBusca.setText(buscaAtual)
        etBusca.doAfterTextChanged { text ->
            buscaAtual = text?.toString() ?: ""
            aplicarFiltros()
        }

        // A view foi (re)criada: busca o estado atual na Activity, monta os chips e a lista.
        todasReceitas = listener?.obterReceitas() ?: todasReceitas
        favoritos = listener?.obterFavoritosIds() ?: favoritos
        reconstruirChips(todasReceitas)
        aplicarFiltros()
    }

    // ── API pública ────────────────────────────────────────────────────

    /** Atualiza o conjunto de IDs favoritos sem recriar os chips. Seguro antes da view existir. */
    fun updateFavoritos(novos: Set<Int>) {
        favoritos = novos
        aplicarFiltros()
    }

    // ── Lógica interna ─────────────────────────────────────────────────

    private fun reconstruirChips(receitas: List<Receita>) {
        chipGroup.removeAllViews()

        val categorias = listOf(TODAS) + receitas
            .map { it.categoria }
            .filter { it.isNotBlank() }
            .distinct()

        // Se a categoria selecionada não existe mais, volta para "Todas"
        if (categoriaAtual !in categorias) categoriaAtual = TODAS

        categorias.forEach { categoria ->
            // Infla o layout estilizado (Filter + seletores de cor) em vez de `Chip(context)`,
            // que usaria o estilo Assist padrão do tema e ignoraria component_chip_categoria.xml.
            val chip = layoutInflater.inflate(
                R.layout.component_chip_categoria, chipGroup, false
            ) as Chip
            // O layout tem id fixo (@+id/chip); o ChipGroup usa o id para rastrear a seleção,
            // então cada chip precisa de um id próprio.
            chip.id = View.generateViewId()
            chip.text = categoria
            chip.isChecked = (categoria == categoriaAtual)
            chip.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    categoriaAtual = categoria
                    aplicarFiltros()
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun aplicarFiltros() {
        if (view == null) return // será aplicado em onViewCreated
        val termo = buscaAtual.trim()
        val filtradas = todasReceitas.filter { receita ->
            val bateCategoria = categoriaAtual == TODAS || receita.categoria == categoriaAtual
            val bateBusca = termo.isEmpty() ||
                    receita.titulo.contains(termo, ignoreCase = true) ||
                    receita.ingredientes.any { it.contains(termo, ignoreCase = true) }
            bateCategoria && bateBusca
        }

        adapter.submitList(filtradas.map { ReceitaItem(it, it.id in favoritos) })
        txtVazio.visibility = if (filtradas.isEmpty()) View.VISIBLE else View.GONE
        recycler.visibility  = if (filtradas.isEmpty()) View.GONE  else View.VISIBLE
    }

    companion object {
        private const val TODAS = "Todas"
        fun newInstance() = ListaReceitasFragment()
    }
}

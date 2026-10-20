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
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.model.Receita
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

/**
 * Equivalente XML de ListaReceitasScreen.
 *
 * A Activity host deve implementar [ListaReceitasFragment.Listener] e
 * chamar [updateReceitas] / [updateFavoritos] quando os dados mudarem.
 */
class ListaReceitasFragment : Fragment() {

    interface Listener {
        fun onReceitaClick(receita: Receita)
        fun onToggleFavorito(receita: Receita)
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
            onClick        = { listener?.onReceitaClick(it) },
            onFavoritoClick = { listener?.onToggleFavorito(it) },
            getFavorita    = { it.id in favoritos }
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
        recycler.addItemDecoration(
            SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.card_spacing))
        )

        etBusca.doAfterTextChanged { text ->
            buscaAtual = text?.toString() ?: ""
            aplicarFiltros()
        }
    }

    // ── API pública ────────────────────────────────────────────────────

    /** Atualiza a lista completa de receitas e reconstrói os chips. */
    fun updateReceitas(receitas: List<Receita>) {
        todasReceitas = receitas
        reconstruirChips(receitas)
        aplicarFiltros()
    }

    /** Atualiza o conjunto de IDs favoritos sem recriar os chips. */
    fun updateFavoritos(novos: Set<Int>) {
        favoritos = novos
        aplicarFiltros()
    }

    // ── Lógica interna ─────────────────────────────────────────────────

    private fun reconstruirChips(receitas: List<Receita>) {
        if (!isAdded) return
        chipGroup.removeAllViews()

        val categorias = listOf(TODAS) + receitas
            .map { it.categoria }
            .filter { it.isNotBlank() }
            .distinct()

        categorias.forEach { categoria ->
            val chip = Chip(requireContext()).apply {
                text = categoria
                isCheckable = true
                isChecked = (categoria == categoriaAtual)
                setOnCheckedChangeListener { _, checked ->
                    if (checked) {
                        categoriaAtual = categoria
                        aplicarFiltros()
                    }
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun aplicarFiltros() {
        if (!isAdded) return
        val termo = buscaAtual.trim()
        val filtradas = todasReceitas.filter { receita ->
            val bateCategoria = categoriaAtual == TODAS || receita.categoria == categoriaAtual
            val bateBusca = termo.isEmpty() ||
                    receita.titulo.contains(termo, ignoreCase = true) ||
                    receita.ingredientes.any { it.contains(termo, ignoreCase = true) }
            bateCategoria && bateBusca
        }

        adapter.submitList(filtradas)
        txtVazio.visibility = if (filtradas.isEmpty()) View.VISIBLE else View.GONE
        recycler.visibility  = if (filtradas.isEmpty()) View.GONE  else View.VISIBLE
    }

    companion object {
        private const val TODAS = "Todas"
        fun newInstance() = ListaReceitasFragment()
    }
}

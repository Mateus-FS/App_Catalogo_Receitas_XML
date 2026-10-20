package com.example.app_catalogo_receitas_xml.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.model.Receita

/**
 * Equivalente XML de ReceitasFavoritasScreen.
 *
 * A Activity host deve implementar [ReceitasFavoritasFragment.Listener]
 * e chamar [updateFavoritas] quando a lista mudar.
 */
class ReceitasFavoritasFragment : Fragment() {

    interface Listener {
        fun onReceitaClick(receita: Receita)
        fun onToggleFavorito(receita: Receita)
    }

    // ── Views ──────────────────────────────────────────────────────────
    private lateinit var recycler: RecyclerView
    private lateinit var layoutVazio: View

    private val adapter by lazy {
        ReceitaAdapter(
            onClick         = { listener?.onReceitaClick(it) },
            onFavoritoClick = { listener?.onToggleFavorito(it) },
            getFavorita     = { true } // todas as receitas aqui são favoritas
        )
    }

    private val listener get() = activity as? Listener

    // ── Ciclo de vida ──────────────────────────────────────────────────
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_receitas_favoritas, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler    = view.findViewById(R.id.recyclerFavoritas)
        layoutVazio = view.findViewById(R.id.layoutVazio)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        recycler.addItemDecoration(
            SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.card_spacing))
        )
    }

    // ── API pública ────────────────────────────────────────────────────

    /** Atualiza a lista de receitas favoritas. */
    fun updateFavoritas(favoritas: List<Receita>) {
        if (!isAdded) return
        adapter.submitList(favoritas)
        val vazio = favoritas.isEmpty()
        layoutVazio.visibility = if (vazio) View.VISIBLE else View.GONE
        recycler.visibility    = if (vazio) View.GONE    else View.VISIBLE
    }

    companion object {
        fun newInstance() = ReceitasFavoritasFragment()
    }
}

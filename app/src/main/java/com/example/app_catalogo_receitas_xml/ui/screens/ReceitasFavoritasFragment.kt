package com.example.app_catalogo_receitas_xml.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.model.Receita

/**
 * Equivalente XML de ReceitasFavoritasScreen.
 *
 * Os dados vêm da Activity host ([Listener.obterFavoritas]) sempre que a view é
 * criada; enquanto a tela está visível, a Activity chama [updateFavoritas].
 */
class ReceitasFavoritasFragment : Fragment() {

    interface Listener {
        fun onReceitaClick(receita: Receita)
        fun onToggleFavorito(receita: Receita)
        fun obterFavoritas(): List<Receita>
    }

    // ── Dados ──────────────────────────────────────────────────────────
    private var favoritas: List<Receita> = emptyList()

    // ── Views ──────────────────────────────────────────────────────────
    private lateinit var recycler: RecyclerView
    private lateinit var layoutVazio: View

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
    ): View = inflater.inflate(R.layout.fragment_receitas_favoritas, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler    = view.findViewById(R.id.recyclerFavoritas)
        layoutVazio = view.findViewById(R.id.layoutVazio)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        (recycler.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        recycler.addItemDecoration(
            SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.card_spacing))
        )

        // A view foi (re)criada: busca o estado atual na Activity e desenha.
        favoritas = listener?.obterFavoritas() ?: favoritas
        render()
    }

    // ── API pública ────────────────────────────────────────────────────

    /** Atualiza a lista de receitas favoritas. Seguro de chamar antes da view existir. */
    fun updateFavoritas(novas: List<Receita>) {
        favoritas = novas
        render()
    }

    // ── Lógica interna ─────────────────────────────────────────────────

    private fun render() {
        if (view == null) return // será desenhado em onViewCreated
        adapter.submitList(favoritas.map { ReceitaItem(it, favorita = true) })
        val vazio = favoritas.isEmpty()
        layoutVazio.visibility = if (vazio) View.VISIBLE else View.GONE
        recycler.visibility    = if (vazio) View.GONE    else View.VISIBLE
    }

    companion object {
        fun newInstance() = ReceitasFavoritasFragment()
    }
}

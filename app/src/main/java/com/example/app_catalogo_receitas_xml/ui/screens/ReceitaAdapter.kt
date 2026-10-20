package com.example.app_catalogo_receitas_xml.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.model.Receita
import com.example.app_catalogo_receitas_xml.ui.components.MiniaturaReceitaView

/**
 * Adapter para RecyclerView que usa component_receita_card.xml.
 * Equivalente ao bloco items { ReceitaCard(...) } dos composables.
 */
class ReceitaAdapter(
    private val onClick: (Receita) -> Unit,
    private val onFavoritoClick: (Receita) -> Unit,
    private val getFavorita: (Receita) -> Boolean
) : ListAdapter<Receita, ReceitaAdapter.ViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_receita_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val card       = view.findViewById<View>(R.id.receitaCard)
        private val miniatura  = view.findViewById<MiniaturaReceitaView>(R.id.miniatura)
        private val titulo     = view.findViewById<TextView>(R.id.receitaTitulo)
        private val detalhes   = view.findViewById<TextView>(R.id.receitaDetalhes)
        private val categoria  = view.findViewById<TextView>(R.id.receitaCategoria)
        private val btnFav     = view.findViewById<ImageButton>(R.id.btnFavorito)

        fun bind(receita: Receita) {
            val ctx = itemView.context
            val favorita = getFavorita(receita)

            card.setOnClickListener { onClick(receita) }

            miniatura.setCategoria(receita.categoria)

            titulo.text = receita.titulo

            val detalhesTxt = listOf(receita.tempo, receita.dificuldade)
                .filter { it.isNotBlank() }
                .joinToString(" · ")
                .ifEmpty { receita.descricao }
            detalhes.text = detalhesTxt

            categoria.isVisible = receita.categoria.isNotBlank()
            categoria.text = receita.categoria

            // Ícone e tint do favorito
            if (favorita) {
                btnFav.setImageResource(R.drawable.ic_favorite)
                btnFav.setColorFilter(ContextCompat.getColor(ctx, R.color.favorito))
                btnFav.contentDescription = ctx.getString(R.string.cd_remover_favorito)
            } else {
                btnFav.setImageResource(R.drawable.ic_favorite_border)
                btnFav.clearColorFilter()
                btnFav.contentDescription = ctx.getString(R.string.cd_adicionar_favorito)
            }
            btnFav.setOnClickListener { onFavoritoClick(receita) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Receita>() {
            override fun areItemsTheSame(old: Receita, new: Receita) = old.id == new.id
            override fun areContentsTheSame(old: Receita, new: Receita) = old == new
        }
    }
}

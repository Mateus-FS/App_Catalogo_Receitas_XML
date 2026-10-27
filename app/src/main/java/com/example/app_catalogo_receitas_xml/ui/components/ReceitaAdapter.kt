package com.example.app_catalogo_receitas_xml.ui.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app_catalogo_receitas_xml.databinding.ComponentReceitaCardBinding
import com.example.app_catalogo_receitas_xml.model.Receita
import com.example.app_catalogo_receitas_xml.ui.viewmodel.ReceitaItem

/**
 * Adapter do RecyclerView que exibe os cards de receitas.
 *
 * @param onClick          toque no card (abrir detalhes).
 * @param onFavoritoClick  toque no coração (alternar favorito).
 */
class ReceitaAdapter(
    private val onClick: (Receita) -> Unit,
    private val onFavoritoClick: (Receita) -> Unit
) : ListAdapter<ReceitaItem, ReceitaAdapter.ViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ComponentReceitaCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ComponentReceitaCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReceitaItem) {
            val receita = item.receita

            binding.cartao.setOnClickListener { onClick(receita) }

            binding.miniatura.setCategoria(receita.categoria)
            binding.titulo.text = receita.titulo
            binding.detalhes.text = resumo(receita)

            binding.categoria.apply {
                isVisible = receita.categoria.titulo.isNotBlank()
                text = receita.categoria.titulo
            }

            binding.botaoFavorito.apply {
                setFavorito(item.favorita)
                setOnClickListener { onFavoritoClick(receita) }
            }
        }

        /** "tempo · dificuldade"; se ambos estiverem vazios, usa a descrição. */
        private fun resumo(receita: Receita): String =
            listOf(receita.tempo, receita.dificuldade)
                .filter { it.isNotBlank() }
                .joinToString(" · ")
                .ifEmpty { receita.descricao }
    }

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<ReceitaItem>() {
            override fun areItemsTheSame(old: ReceitaItem, new: ReceitaItem) =
                old.receita.id == new.receita.id

            override fun areContentsTheSame(old: ReceitaItem, new: ReceitaItem) = old == new
        }
    }
}
package com.example.app_catalogo_receitas_xml.ui.screens

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.data.Favoritos
import com.example.app_catalogo_receitas_xml.databinding.ComponentReceitaCardBinding
import com.example.app_catalogo_receitas_xml.model.Receita
import com.example.app_catalogo_receitas_xml.util.estiloDaCategoria

/**
 * Adapter do RecyclerView: transforma cada Receita em um card (component_receita_card.xml).
 * Usado na Lista e nos Favoritos.
 */
class ReceitaAdapter(
    private val onClick: (Receita) -> Unit,
    private val onToggleFavorito: (Receita) -> Unit
) : RecyclerView.Adapter<ReceitaAdapter.ReceitaViewHolder>() {

    private var itens: List<Receita> = emptyList()

    /** Troca a lista exibida e redesenha os cards (inclusive os corações). */
    fun submeter(novos: List<Receita>) {
        itens = novos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitaViewHolder {
        val binding = ComponentReceitaCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceitaViewHolder(binding)
    }

    override fun getItemCount(): Int = itens.size

    override fun onBindViewHolder(holder: ReceitaViewHolder, position: Int) {
        holder.vincular(itens[position])
    }

    inner class ReceitaViewHolder(
        private val binding: ComponentReceitaCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun vincular(receita: Receita) {
            val contexto = binding.root.context
            val favorita = Favoritos.contem(receita.id)
            val estilo = estiloDaCategoria(receita.categoria)

            // Miniatura colorida com o emoji da categoria
            binding.miniatura.backgroundTintList = ColorStateList.valueOf(estilo.fundo)
            binding.emoji.text = estilo.emoji

            binding.titulo.text = receita.titulo

            // "50 min · Fácil" (se não houver tempo/dificuldade, mostra a descrição)
            binding.detalhes.text = listOf(receita.tempo, receita.dificuldade)
                .filter { it.isNotBlank() }
                .joinToString(" · ")
                .ifEmpty { receita.descricao }

            binding.categoria.isVisible = receita.categoria.isNotBlank()
            binding.categoria.text = receita.categoria

            // Coração: cheio e vermelho se for favorita; contorno e cinza se não
            binding.botaoFavorito.setImageResource(
                if (favorita) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            val corDoCoracao = if (favorita) R.color.favorito else R.color.texto_secundario
            binding.botaoFavorito.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(contexto, corDoCoracao))
            binding.botaoFavorito.contentDescription = contexto.getString(
                if (favorita) R.string.cd_remover_favorito else R.string.cd_adicionar_favorito
            )

            binding.cartao.setOnClickListener { onClick(receita) }
            binding.botaoFavorito.setOnClickListener { onToggleFavorito(receita) }
        }
    }
}
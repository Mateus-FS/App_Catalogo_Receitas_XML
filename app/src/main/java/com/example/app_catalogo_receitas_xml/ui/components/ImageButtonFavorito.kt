package com.example.app_catalogo_receitas_xml.ui.components

import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.example.app_catalogo_receitas_xml.R

/**
 * Aplica ao botão a aparência de "favorita" ou "não favorita":
 * ícone, cor e descrição de acessibilidade.
 *
 * Centraliza o que antes estava copiado no ReceitaAdapter e no DetalhesReceitaFragment.
 */
fun ImageButton.setFavorito(favorita: Boolean) {
    if (favorita) {
        setImageResource(R.drawable.ic_favorite)
        setColorFilter(ContextCompat.getColor(context, R.color.favorito))
        contentDescription = context.getString(R.string.cd_remover_favorito)
    } else {
        setImageResource(R.drawable.ic_favorite_border)
        clearColorFilter()
        contentDescription = context.getString(R.string.cd_adicionar_favorito)
    }
}
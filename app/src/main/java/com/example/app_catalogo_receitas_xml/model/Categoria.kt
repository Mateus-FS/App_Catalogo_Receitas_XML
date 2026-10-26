package com.example.app_catalogo_receitas_xml.model

import androidx.annotation.ColorRes
import com.example.app_catalogo_receitas_xml.R

enum class Categoria(
    val titulo: String,
    val emoji: String,
    @ColorRes val fundoRes: Int
) {
    MASSAS("Massas", "🍝", R.color.categoria_massas),
    DOCES("Doces", "🍰", R.color.categoria_doces),
    SALADAS("Saladas", "🥗", R.color.categoria_saladas),
    PRATOS_PRINCIPAIS("Pratos principais", "🥘", R.color.categoria_pratos_principais),
    CAFE_DA_MANHA("Café da manhã", "🥞", R.color.categoria_cafe_manha)
}

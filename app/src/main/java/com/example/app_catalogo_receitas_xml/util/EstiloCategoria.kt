package com.example.app_catalogo_receitas_xml.util

/** Cor de fundo e emoji que representam cada categoria de receita. */
data class EstiloCategoria(val fundo: Int, val emoji: String)

fun estiloDaCategoria(categoria: String): EstiloCategoria = when (categoria) {
    "Massas" -> EstiloCategoria(0xFFF3D6C6.toInt(), "🍝")
    "Doces" -> EstiloCategoria(0xFFF6E3A9.toInt(), "🍰")
    "Saladas" -> EstiloCategoria(0xFFD6E7CB.toInt(), "🥗")
    "Pratos principais" -> EstiloCategoria(0xFFF2D0C8.toInt(), "🍲")
    "Café da manhã" -> EstiloCategoria(0xFFEFE6D6.toInt(), "🥞")
    else -> EstiloCategoria(0xFFE6E8DF.toInt(), "🍽️")
}
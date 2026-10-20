package com.example.app_catalogo_receitas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Cor de fundo e emoji que representam cada categoria de receita. */
data class EstiloCategoria(val fundo: Color, val emoji: String)

fun estiloDaCategoria(categoria: String): EstiloCategoria = when (categoria) {
    "Massas" -> EstiloCategoria(Color(0xFFF3D6C6), "🍝")
    "Doces" -> EstiloCategoria(Color(0xFFF6E3A9), "🍰")
    "Saladas" -> EstiloCategoria(Color(0xFFD6E7CB), "🥗")
    "Pratos principais" -> EstiloCategoria(Color(0xFFF2D0C8), "🍲")
    "Café da manhã" -> EstiloCategoria(Color(0xFFEFE6D6), "🥞")
    else -> EstiloCategoria(Color(0xFFE6E8DF), "🍽️")
}

/** Quadrado arredondado e colorido com o emoji da categoria (usado nos cards). */
@Composable
fun MiniaturaReceita(
    categoria: String,
    modifier: Modifier = Modifier,
    tamanho: Dp = 76.dp
) {
    val estilo = estiloDaCategoria(categoria)
    Box(
        modifier = modifier
            .size(tamanho)
            .clip(RoundedCornerShape(16.dp))
            .background(estilo.fundo),
        contentAlignment = Alignment.Center
    ) {
        Text(text = estilo.emoji, fontSize = 34.sp)
    }
}
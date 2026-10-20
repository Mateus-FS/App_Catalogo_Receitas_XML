package com.example.app_catalogo_receitas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_catalogo_receitas.model.Receita
import com.example.app_catalogo_receitas.ui.theme.Favorito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceitaCard(
    receita: Receita,
    favorita: Boolean,
    onClick: () -> Unit,
    onToggleFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    // "50 min · Fácil" (se não houver tempo/dificuldade, mostra a descrição)
    val detalhes = listOf(receita.tempo, receita.dificuldade)
        .filter { it.isNotBlank() }
        .joinToString(" · ")
        .ifEmpty { receita.descricao }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MiniaturaReceita(categoria = receita.categoria)
            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = receita.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = detalhes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (receita.categoria.isNotBlank()) {
                    Text(
                        text = receita.categoria,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            IconButton(onClick = onToggleFavorito) {
                Icon(
                    imageVector = if (favorita) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (favorita) "Remover dos favoritos" else "Adicionar aos favoritos",
                    tint = if (favorita) Favorito else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
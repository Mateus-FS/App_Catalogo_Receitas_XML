package com.example.app_catalogo_receitas.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class Aba { RECEITAS, FAVORITOS }

@Composable
fun BarraNavegacao(
    abaSelecionada: Aba,
    onReceitasClick: () -> Unit,
    onFavoritosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cores = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = Color.Transparent,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
    val favoritosSelecionada = abaSelecionada == Aba.FAVORITOS

    Column(modifier = modifier) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            NavigationBarItem(
                selected = abaSelecionada == Aba.RECEITAS,
                onClick = onReceitasClick,
                icon = { Icon(imageVector = Icons.Filled.Menu, contentDescription = null) },
                label = { Text("Receitas") },
                colors = cores
            )
            NavigationBarItem(
                selected = favoritosSelecionada,
                onClick = onFavoritosClick,
                icon = {
                    Icon(
                        imageVector = if (favoritosSelecionada) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Filled.FavoriteBorder
                        },
                        contentDescription = null
                    )
                },
                label = { Text("Favoritos") },
                colors = cores
            )
        }
    }
}
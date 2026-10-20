package com.example.app_catalogo_receitas.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_catalogo_receitas.data.Receitas
import com.example.app_catalogo_receitas.model.Receita
import com.example.app_catalogo_receitas.ui.components.Aba
import com.example.app_catalogo_receitas.ui.components.BarraNavegacao
import com.example.app_catalogo_receitas.ui.components.ReceitaCard
import com.example.app_catalogo_receitas.ui.theme.AppCatalogoReceitaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceitasFavoritasScreen(
    favoritas: List<Receita>,
    onReceitaClick: (Receita) -> Unit,
    onToggleFavorito: (Receita) -> Unit,
    onReceitasClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BarraNavegacao(
                abaSelecionada = Aba.FAVORITOS,
                onReceitasClick = onReceitasClick,
                onFavoritosClick = {}
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Suas preferidas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Favoritos",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            if (favoritas.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp, start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Toque no coração de uma receita para guardá-la aqui.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(favoritas, key = { it.id }) { receita ->
                    ReceitaCard(
                        receita = receita,
                        favorita = true,
                        onClick = { onReceitaClick(receita) },
                        onToggleFavorito = { onToggleFavorito(receita) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReceitasFavoritasScreenPreview() {
    AppCatalogoReceitaTheme {
        ReceitasFavoritasScreen(
            favoritas = Receitas.receitas.take(1),
            onReceitaClick = {},
            onToggleFavorito = {},
            onReceitasClick = {}
        )
    }
}
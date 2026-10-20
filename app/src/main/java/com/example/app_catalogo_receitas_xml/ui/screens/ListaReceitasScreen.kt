package com.example.app_catalogo_receitas.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_catalogo_receitas.data.Receitas
import com.example.app_catalogo_receitas.model.Receita
import com.example.app_catalogo_receitas.ui.components.Aba
import com.example.app_catalogo_receitas.ui.components.BarraNavegacao
import com.example.app_catalogo_receitas.ui.components.ChipCategoria
import com.example.app_catalogo_receitas.ui.components.ReceitaCard
import com.example.app_catalogo_receitas.ui.theme.AppCatalogoReceitaTheme

private const val TODAS = "Todas"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaReceitasScreen(
    receitas: List<Receita>,
    favoritos: Set<Int>,
    onReceitaClick: (Receita) -> Unit,
    onToggleFavorito: (Receita) -> Unit,
    onFavoritasClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var busca by rememberSaveable { mutableStateOf("") }
    var categoriaSelecionada by rememberSaveable { mutableStateOf(TODAS) }

    // Chips: "Todas" + as categorias que existirem nas receitas
    val categorias = listOf(TODAS) + receitas
        .map { it.categoria }
        .filter { it.isNotBlank() }
        .distinct()

    val termo = busca.trim()
    val filtradas = receitas.filter { receita ->
        val bateCategoria = categoriaSelecionada == TODAS || receita.categoria == categoriaSelecionada
        val bateBusca = termo.isEmpty() ||
                receita.titulo.contains(termo, ignoreCase = true) ||
                receita.ingredientes.any { it.contains(termo, ignoreCase = true) }
        bateCategoria && bateBusca
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BarraNavegacao(
                abaSelecionada = Aba.RECEITAS,
                onReceitasClick = {},
                onFavoritosClick = onFavoritasClick
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
                        text = "O que vamos cozinhar hoje?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Receitas",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = busca,
                    onValueChange = { busca = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar receita ou ingrediente") },
                    leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    categorias.forEach { categoria ->
                        ChipCategoria(
                            texto = categoria,
                            selecionado = categoria == categoriaSelecionada,
                            onClick = { categoriaSelecionada = categoria }
                        )
                    }
                }
            }

            if (filtradas.isEmpty()) {
                item {
                    Text(
                        text = "Nenhuma receita encontrada.",
                        modifier = Modifier.padding(top = 24.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(filtradas, key = { it.id }) { receita ->
                    ReceitaCard(
                        receita = receita,
                        favorita = receita.id in favoritos,
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
private fun ListaReceitasScreenPreview() {
    AppCatalogoReceitaTheme {
        ListaReceitasScreen(
            receitas = Receitas.receitas,
            favoritos = setOf(1),
            onReceitaClick = {},
            onToggleFavorito = {},
            onFavoritasClick = {}
        )
    }
}
package com.example.app_catalogo_receitas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_catalogo_receitas.R
import com.example.app_catalogo_receitas.model.Receita
import com.example.app_catalogo_receitas.ui.components.Aba
import com.example.app_catalogo_receitas.ui.components.BarraNavegacao
import com.example.app_catalogo_receitas.ui.components.estiloDaCategoria
import com.example.app_catalogo_receitas.ui.theme.AppCatalogoReceitaTheme
import com.example.app_catalogo_receitas.ui.theme.Favorito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesReceitaScreen(
    receita: Receita,
    onVoltar: () -> Unit,
    modifier: Modifier = Modifier,
    favorita: Boolean = false,
    onToggleFavorito: () -> Unit = {},
    onReceitasClick: () -> Unit = {},
    onFavoritosClick: () -> Unit = {}
) {
    val estilo = estiloDaCategoria(receita.categoria)

    // "50 min · Fácil · 8 porções"
    val resumo = listOf(
        receita.tempo,
        receita.dificuldade,
        if (receita.porcoes > 0) "${receita.porcoes} porções" else ""
    ).filter { it.isNotBlank() }.joinToString(" · ")

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BarraNavegacao(
                abaSelecionada = Aba.RECEITAS,
                onReceitasClick = onReceitasClick,
                onFavoritosClick = onFavoritosClick
            )
        }
    ) { innerPadding ->
        // Só o espaço de baixo: o topo fica livre para o fundo colorido ir até a borda da tela
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(estilo.fundo)
            ) {
                Text(
                    text = estilo.emoji,
                    fontSize = 88.sp,
                    modifier = Modifier.align(Alignment.Center)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onVoltar,
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                    IconButton(
                        onClick = onToggleFavorito,
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (favorita) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (favorita) "Remover dos favoritos" else "Adicionar aos favoritos",
                            tint = if (favorita) Favorito else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (receita.categoria.isNotBlank()) {
                    Text(
                        text = receita.categoria,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = receita.titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (resumo.isNotEmpty()) {
                    Text(
                        text = resumo,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (receita.descricao.isNotBlank()) {
                    Text(
                        text = receita.descricao,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Ingredientes",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                receita.ingredientes.forEach { ingrediente ->
                    Row {
                        Text(
                            text = "•",
                            modifier = Modifier.width(20.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = ingrediente,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Modo de preparo",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                receita.passos.forEachIndexed { indice, passo ->
                    Row {
                        Text(
                            text = "${indice + 1}.",
                            modifier = Modifier.width(28.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = passo,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalhesReceitaScreenPreview() {
    AppCatalogoReceitaTheme {
        DetalhesReceitaScreen(
            receita = Receita(
                id = 1,
                titulo = "Bolo de Cenoura",
                descricao = "Bolo fofinho de cenoura com cobertura de chocolate.",
                ingredientes = listOf(
                    "3 cenouras médias",
                    "4 ovos",
                    "1 xícara de óleo",
                    "2 xícaras de açúcar",
                    "2 xícaras de farinha de trigo"
                ),
                passos = listOf(
                    "Bata no liquidificador as cenouras, os ovos e o óleo.",
                    "Misture com o açúcar e a farinha.",
                    "Asse em forno médio por 40 minutos."
                ),
                imagem = R.drawable.ic_launcher_foreground,
                categoria = "Doces",
                tempo = "50 min",
                dificuldade = "Fácil",
                porcoes = 8
            ),
            onVoltar = {},
            favorita = true
        )
    }
}
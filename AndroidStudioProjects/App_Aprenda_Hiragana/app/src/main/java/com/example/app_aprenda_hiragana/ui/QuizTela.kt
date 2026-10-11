package com.example.app_aprenda_hiragana.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_aprenda_hiragana.ui.theme.AcertoVerde
import com.example.app_aprenda_hiragana.ui.theme.ErroVermelho
import com.example.app_aprenda_hiragana.viewModel.QuizViewModel

private val FormaBotao = RoundedCornerShape(16.dp)
private val AlturaBotao = 56.dp

@Composable
fun QuizTela(viewModel: QuizViewModel) {

    val questao by viewModel.questaoAtual.collectAsState()
    val pontuacao by viewModel.pontuacao.collectAsState()
    val respostaSelecionada by viewModel.respostaSelecionada.collectAsState()
    val indiceAtual by viewModel.indiceAtual.collectAsState()
    val totalRodada by viewModel.totalRodada.collectAsState()
    val respondidas by viewModel.respondidas.collectAsState()
    val terminou by viewModel.terminou.collectAsState()
    val restantes by viewModel.restantes.collectAsState()
    val emInicio by viewModel.emInicio.collectAsState()

    if (emInicio) {

        InicioTela(onIniciar = { viewModel.iniciar() })

    } else if (terminou) {

        ResultadoTela(
            pontuacao = pontuacao,
            respondidas = respondidas,
            restantes = restantes,
            onContinuar = { viewModel.continuar() },
            onReiniciar = { viewModel.reiniciarTudo() },
            onInicio = { viewModel.voltarAoInicio() }
        )

    } else {

        val respondeu = respostaSelecionada != null
        val acertou = respostaSelecionada == questao.respostaCorreta
        val progresso = indiceAtual + if (respondeu) 1 else 0

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Questão ${indiceAtual + 1} de $totalRodada",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextButton(onClick = { viewModel.encerrar() }) {
                    Text("Sair")
                }
            }

            BarraProgresso(passos = totalRodada, concluidos = progresso)

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .heightIn(min = 140.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(28.dp)
                    )
            ) {
                Text(
                    text = "Pontos: $pontuacao",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )

                Text(
                    text = questao.simbolo,
                    fontSize = 96.sp,
                    // Fonte sem serifa/cursiva: traço mais limpo para reconhecer o caractere
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                questao.alternativas.forEach { alternativa ->

                    val ehCorreta = alternativa == questao.respostaCorreta
                    val ehEscolhaErrada = alternativa == respostaSelecionada && !ehCorreta

                    val corFundo = when {
                        !respondeu -> MaterialTheme.colorScheme.surfaceVariant
                        ehCorreta -> AcertoVerde
                        ehEscolhaErrada -> ErroVermelho
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val corTexto = when {
                        !respondeu -> MaterialTheme.colorScheme.onSurface
                        ehCorreta || ehEscolhaErrada -> Color.White
                        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                    }

                    val borda = when {
                        !respondeu -> MaterialTheme.colorScheme.outline
                        ehCorreta -> AcertoVerde
                        ehEscolhaErrada -> ErroVermelho
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    }

                    Button(
                        onClick = { viewModel.responder(alternativa) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AlturaBotao),
                        enabled = !respondeu,
                        shape = FormaBotao,
                        border = BorderStroke(1.dp, borda),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = corFundo,
                            contentColor = corTexto,
                            disabledContainerColor = corFundo,
                            disabledContentColor = corTexto
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = alternativa,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.align(Alignment.Center)
                            )

                            if (respondeu && (ehCorreta || ehEscolhaErrada)) {
                                Icon(
                                    imageVector = if (ehCorreta) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = if (ehCorreta) "Resposta correta" else "Sua resposta, errada",
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center
            ) {
                if (respondeu) {
                    Text(
                        text = if (acertou) {
                            "Correto!"
                        } else {
                            "Errado. A resposta era: ${questao.respostaCorreta}"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (acertou) AcertoVerde else ErroVermelho
                    )
                }
            }

            Button(
                onClick = { viewModel.proximaQuestao() },
                enabled = respondeu,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AlturaBotao),
                shape = FormaBotao,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.45f)
                )
            ) {
                Text(
                    text = if (indiceAtual == totalRodada - 1) "Ver resultado" else "Próxima",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun BarraProgresso(passos: Int, concluidos: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(passos) { i ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        if (i < concluidos) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primaryContainer
                    )
            )
        }
    }
}

@Composable
fun ResultadoTela(
    pontuacao: Int,
    respondidas: Int,
    restantes: Int,
    onContinuar: () -> Unit,
    onReiniciar: () -> Unit,
    onInicio: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Sua nota",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (respondidas == 0) {

            Text(
                text = "Você saiu sem responder nenhuma questão.",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

        } else {

            val porcentagem = pontuacao * 100 / respondidas

            Text(
                text = "$pontuacao de $respondidas",
                fontSize = 56.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "$porcentagem% de acertos",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (restantes > 0) {

            Text(
                text = "Ainda restam $restantes símbolos para praticar.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onContinuar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AlturaBotao),
                shape = FormaBotao
            ) {
                Text("Continuar com outro quiz", fontSize = 18.sp)
            }

        } else {

            Text(
                text = "Você já passou por todos os símbolos! 🎉",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onReiniciar,
            modifier = Modifier
                .fillMaxWidth()
                .height(AlturaBotao),
            shape = FormaBotao
        ) {
            Text("Recomeçar do zero", fontSize = 18.sp)
        }


        Spacer(modifier = Modifier.height(4.dp))

        TextButton(onClick = onInicio) {
            Text("Voltar ao início", fontSize = 16.sp)
        }
    }
}


@Composable
fun InicioTela(onIniciar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(32.dp)
                    )
                    .padding(horizontal = 40.dp, vertical = 28.dp)
            ) {
                Text(
                    text = "あ",
                    fontSize = 96.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Aprenda Hiragana",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Treine o reconhecimento dos caracteres em rodadas rápidas de perguntas.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onIniciar,
            modifier = Modifier
                .fillMaxWidth()
                .height(AlturaBotao),
            shape = FormaBotao
        ) {
            Text(
                text = "Começar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
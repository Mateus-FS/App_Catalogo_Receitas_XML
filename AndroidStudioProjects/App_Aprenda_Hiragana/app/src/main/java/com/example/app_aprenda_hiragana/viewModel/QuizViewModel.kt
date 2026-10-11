package com.example.app_aprenda_hiragana.viewModel

import androidx.lifecycle.ViewModel
import com.example.app_aprenda_hiragana.data.questoesHiragana
import com.example.app_aprenda_hiragana.model.Questao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel : ViewModel() {

    private val tamanhoQuiz = 5
    private val pool = questoesHiragana.shuffled().toMutableList()
    private var rodada: List<Questao> = sortearRodada()
    private val _questaoAtual = MutableStateFlow(rodada[0].embaralhada())
    val questaoAtual: StateFlow<Questao> = _questaoAtual.asStateFlow()
    private val _indiceAtual = MutableStateFlow(0)
    val indiceAtual: StateFlow<Int> = _indiceAtual.asStateFlow()
    private val _totalRodada = MutableStateFlow(rodada.size)
    val totalRodada: StateFlow<Int> = _totalRodada.asStateFlow()
    private val _pontuacao = MutableStateFlow(0)
    val pontuacao: StateFlow<Int> = _pontuacao.asStateFlow()
    private val _respondidas = MutableStateFlow(0)
    val respondidas: StateFlow<Int> = _respondidas.asStateFlow()
    private val _respostaSelecionada = MutableStateFlow<String?>(null)
    val respostaSelecionada: StateFlow<String?> = _respostaSelecionada.asStateFlow()
    private val _terminou = MutableStateFlow(false)
    val terminou: StateFlow<Boolean> = _terminou.asStateFlow()
    private val _restantes = MutableStateFlow(pool.size)
    val restantes: StateFlow<Int> = _restantes.asStateFlow()
    private val _emInicio = MutableStateFlow(true)
    val emInicio: StateFlow<Boolean> = _emInicio.asStateFlow()

    private fun Questao.embaralhada() = copy(alternativas = alternativas.shuffled())

    private fun sortearRodada(): List<Questao> {
        val quantidade = minOf(tamanhoQuiz, pool.size)
        val sorteadas = pool.subList(0, quantidade).toList()
        pool.subList(0, quantidade).clear()
        return sorteadas
    }

    private fun iniciarRodada() {
        rodada = sortearRodada()
        _totalRodada.value = rodada.size
        _indiceAtual.value = 0
        _questaoAtual.value = rodada[0].embaralhada()
        _pontuacao.value = 0
        _respondidas.value = 0
        _respostaSelecionada.value = null
        _restantes.value = pool.size
        _terminou.value = false
    }

    fun responder(resposta: String) {

        if (_respostaSelecionada.value != null) return

        _respostaSelecionada.value = resposta
        _respondidas.value++

        if (resposta == questaoAtual.value.respostaCorreta) {
            _pontuacao.value++
        }
    }

    fun proximaQuestao() {

        if (_indiceAtual.value < rodada.lastIndex) {
            _indiceAtual.value++
            _questaoAtual.value = rodada[_indiceAtual.value].embaralhada()
            _respostaSelecionada.value = null
        } else {
            encerrar()
        }
    }

    fun encerrar() {

        if (_terminou.value) return

        pool.addAll(0, rodada.drop(_respondidas.value))

        _restantes.value = pool.size
        _terminou.value = true
    }

    fun continuar() {
        if (pool.isEmpty()) return
        iniciarRodada()
    }

    fun reiniciarTudo() {
        pool.clear()
        pool.addAll(questoesHiragana.shuffled())
        iniciarRodada()
    }

    fun iniciar() {
        _emInicio.value = false
    }

    fun voltarAoInicio() {
        reiniciarTudo()
        _emInicio.value = true
    }
}
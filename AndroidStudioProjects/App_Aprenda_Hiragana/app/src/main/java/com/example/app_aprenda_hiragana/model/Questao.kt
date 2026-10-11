package com.example.app_aprenda_hiragana.model

data class Questao (
    val simbolo: String,
    val respostaCorreta: String,
    val alternativas: List<String>
)
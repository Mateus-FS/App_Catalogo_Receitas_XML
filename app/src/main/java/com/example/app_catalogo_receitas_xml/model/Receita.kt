package com.example.app_catalogo_receitas_xml.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Receita(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val tempo: String,
    val dificuldade: String,
    val porcoes: Int,
    val descricao: String,
    val ingredientes: List<String>,
    val passos: List<String>
) : Parcelable

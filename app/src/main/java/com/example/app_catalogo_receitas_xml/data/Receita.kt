package com.example.app_catalogo_receitas_xml.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Receita(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val ingredientes: List<String>,
    val passos: List<String>,
    @DrawableRes val imagem: Int,
    val favorito: Boolean = false,
    val categoria: String = "",
    val tempo: String = "",
    val dificuldade: String = "",
    val porcoes: Int = 0
) : Parcelable
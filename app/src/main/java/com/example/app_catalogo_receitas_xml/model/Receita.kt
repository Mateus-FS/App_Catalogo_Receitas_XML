package com.example.app_catalogo_receitas_xml.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Receita(
    val id: Int,
    val titulo: String,
    val categoria: Categoria,
    val tempo: String = "",
    val dificuldade: String = "",
    val porcoes: Int = 0,
    val descricao: String,
    val ingredientes: List<String>,
    val passos: List<String>,
    @DrawableRes val imagem: Int = 0,
    val favorito: Boolean = false
) : Parcelable

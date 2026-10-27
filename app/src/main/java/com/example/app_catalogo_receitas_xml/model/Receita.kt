package com.example.app_catalogo_receitas_xml.model

/**
 * Receita do catálogo.
 *
 * Só o [id] trafega entre telas (via arguments do Fragment); o objeto completo é buscado
 * no ViewModel. Por isso a classe não precisa mais ser Parcelable.
 */
data class Receita(
    val id: Int,
    val titulo: String,
    val categoria: Categoria,
    val tempo: String = "",
    val dificuldade: String = "",
    val porcoes: Int = 0,
    val descricao: String,
    val ingredientes: List<String>,
    val passos: List<String>
)
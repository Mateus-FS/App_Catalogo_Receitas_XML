package com.example.app_catalogo_receitas_xml.data

import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.model.Receita

object Receitas {

    private val img = R.drawable.ic_launcher_foreground

    val receitas = listOf(
        Receita(
            id = 1,
            titulo = "Bolo de Cenoura",
            descricao = "Bolo com cobertura de chocolate.",
            ingredientes = listOf(
                "3 cenouras",
                "4 ovos",
                "1 xícara de óleo",
                "2 xícaras de açúcar",
                "2 e 1/2 xícaras de farinha",
                "1 colher de fermento"
            ),
            passos = listOf(
                "Bata no liquidificador a cenoura, os ovos e o óleo.",
                "Misture com o açúcar e a farinha.",
                "Adicione o fermento e asse por 40 minutos."
            ),
            imagem = img,
            categoria = "Doces",
            tempo = "50 min",
            dificuldade = "Fácil",
            porcoes = 8
        ),
        Receita(
            id = 2,
            titulo = "Macarrão ao Alho e Óleo",
            descricao = "Prato rápido e saboroso.",
            ingredientes = listOf(
                "250 g de espaguete",
                "4 dentes de alho",
                "Azeite",
                "Sal",
                "Salsinha"
            ),
            passos = listOf(
                "Cozinhe o macarrão em água com sal.",
                "Doure o alho fatiado no azeite.",
                "Misture tudo e finalize com salsinha."
            ),
            imagem = img,
            categoria = "Massas",
            tempo = "20 min",
            dificuldade = "Fácil",
            porcoes = 2
        ),
        Receita(
            id = 3,
            titulo = "Panqueca Simples",
            descricao = "Ótima para café da manhã.",
            ingredientes = listOf(
                "1 ovo",
                "1 xícara de leite",
                "1 xícara de farinha",
                "1 pitada de sal"
            ),
            passos = listOf(
                "Misture todos os ingredientes.",
                "Frite uma concha da massa em frigideira untada.",
                "Vire quando dourar e sirva."
            ),
            imagem = img,
            categoria = "Café da manhã",
            tempo = "15 min",
            dificuldade = "Fácil",
            porcoes = 4
        )
    )

    fun porId(id: Int): Receita? = receitas.find { it.id == id }
}
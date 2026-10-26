package com.example.app_catalogo_receitas_xml.data

import com.example.app_catalogo_receitas_xml.model.Receita

object Receitas {
    val receitas = listOf(
        Receita(
            id = 1,
            titulo = "Bolo de Cenoura",
            categoria = "Doces",
            tempo = "45 min",
            dificuldade = "Fácil",
            porcoes = 8,
            descricao = "Bolo tradicional de cenoura com cobertura durinha de chocolate.",
            ingredientes = listOf(
                "3 cenouras médias picadas",
                "3 ovos",
                "1 xícara de óleo",
                "2 xícaras de açúcar",
                "2 xícaras de farinha de trigo",
                "1 colher (sopa) de fermento em pó"
            ),
            passos = listOf(
                "Bata no liquidificador as cenouras, os ovos e o óleo até obter uma mistura homogênea.",
                "Em uma tigela, misture o açúcar e a farinha de trigo.",
                "Despeje a mistura do liquidificador na tigela e mexa bem.",
                "Adicione o fermento e misture delicadamente com uma espátula.",
                "Despeje em uma forma untada e enfarinhada e asse em forno pré-aquecido a 180°C por cerca de 40 minutos."
            )
        ),
        Receita(
            id = 2,
            titulo = "Macarrão à Bolonhesa",
            categoria = "Massas",
            tempo = "30 min",
            dificuldade = "Fácil",
            porcoes = 4,
            descricao = "Delicioso macarrão com molho caseiro de carne moída e tomate.",
            ingredientes = listOf(
                "500g de macarrão espaguete",
                "400g de carne moída",
                "1 lata de molho de tomate",
                "1 cebola picada",
                "2 dentes de alho picados",
                "Azeite, sal e pimenta a gosto"
            ),
            passos = listOf(
                "Cozinhe o macarrão em água fervente com sal até ficar al dente.",
                "Em uma panela, doure a cebola e o alho no azeite.",
                "Adicione a carne moída e refogue até dourar.",
                "Acrescente o molho de tomate, acerte o sal e deixe cozinhar por 10 minutos.",
                "Sirva o molho quente sobre o macarrão."
            )
        ),
        Receita(
            id = 3,
            titulo = "Salada Caesar",
            categoria = "Saladas",
            tempo = "20 min",
            dificuldade = "Fácil",
            porcoes = 2,
            descricao = "Salada crocante com alface americana, croutons e molho Caesar.",
            ingredientes = listOf(
                "1 pé de alface americana",
                "1 xícara de croutons",
                "50g de queijo parmesão ralado",
                "1/2 xícara de molho Caesar",
                "1 peito de frango grelhado e fatiado"
            ),
            passos = listOf(
                "Lave e rasgue as folhas de alface.",
                "Em uma saladeira, coloque a alface, as fatias de frango e os croutons.",
                "Regue com o molho Caesar e polvilhe o queijo parmesão por cima."
            )
        ),
        Receita(
            id = 4,
            titulo = "Panqueca Americana",
            categoria = "Café da Manhã",
            tempo = "15 min",
            dificuldade = "Fácil",
            porcoes = 3,
            descricao = "Panquecas fofinhas ideais para acompanhar mel ou geleia.",
            ingredientes = listOf(
                "1 xícara de farinha de trigo",
                "2 colheres (sopa) de açúcar",
                "1 colher (chá) de fermento",
                "1 ovo",
                "3/4 xícara de leite",
                "2 colheres (sopa) de manteiga derretida"
            ),
            passos = listOf(
                "Misture os ingredientes secos em uma tigela.",
                "Adicione o ovo, o leite e a manteiga derretida, misturando apenas até incorporar.",
                "Aqueça uma frigideira antiaderente untada e despeje porções da massa.",
                "Vire quando começarem a formar bolhas na superfície e doure o outro lado."
            )
        ),
        Receita(
            id = 5,
            titulo = "Strogonoff de Frango",
            categoria = "Pratos Principais",
            tempo = "35 min",
            dificuldade = "Médio",
            porcoes = 5,
            descricao = "Clássico strogonoff de frango cremoso servido com batata palha.",
            ingredientes = listOf(
                "1kg de peito de frango em cubos",
                "1 caixa de creme de leite",
                "2 colheres (sopa) de extrato de tomate",
                "2 colheres (sopa) de ketchup",
                "1 colher (sopa) de mostarda",
                "1 cebola picada",
                "Batata palha para acompanhar"
            ),
            passos = listOf(
                "Em uma panela, doure a cebola e sele os cubos de frango.",
                "Adicione o extrato de tomate, o ketchup e a mostarda.",
                "Desligue o fogo e misture o creme de leite.",
                "Sirva acompanhado de arroz branco e batata palha."
            )
        )
    )
}

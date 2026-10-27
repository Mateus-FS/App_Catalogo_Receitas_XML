package com.example.app_catalogo_receitas_xml.ui.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.ui.screens.DetalhesReceitaFragment

/**
 * Abre a tela de detalhes da receita e empilha no back stack, para o botão
 * Voltar retornar à tela de origem (Lista ou Favoritos).
 *
 * Fica em um único lugar para que os fragments não conheçam o id do container
 * nem o detalhe da transação.
 */
fun Fragment.abrirDetalhes(receitaId: Int) {
    parentFragmentManager.commit {
        setReorderingAllowed(true)
        replace(R.id.fragment_container, DetalhesReceitaFragment.newInstance(receitaId))
        addToBackStack(null)
    }
}
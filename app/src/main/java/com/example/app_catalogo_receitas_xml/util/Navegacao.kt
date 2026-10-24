package com.example.app_catalogo_receitas_xml.util

import android.app.Activity
import android.content.Intent
import com.example.app_catalogo_receitas_xml.MainActivity
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.model.Receita
import com.example.app_catalogo_receitas_xml.ui.screens.DetalhesReceitaActivity
import com.example.app_catalogo_receitas_xml.ui.screens.ReceitasFavoritasActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

enum class Aba { RECEITAS, FAVORITOS }

/** Troca de tela: cada tela é uma Activity, aberta com uma Intent explícita. */
object Navegacao {

    /** Nome (chave) usado no putExtra() para enviar a receita para a tela de detalhes. */
    const val EXTRA_RECEITA = "receita"

    fun abrirDetalhes(origem: Activity, receita: Receita) {
        val intent = Intent(origem, DetalhesReceitaActivity::class.java)
        intent.putExtra(EXTRA_RECEITA, receita)
        origem.startActivity(intent)
    }

    /**
     * Volta para a Lista (MainActivity). CLEAR_TOP fecha as telas que estão por cima dela e
     * SINGLE_TOP reaproveita a Lista que já existe (não cria outra).
     */
    private fun irParaReceitas(origem: Activity) {
        val intent = Intent(origem, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        origem.startActivity(intent)
    }

    private fun irParaFavoritas(origem: Activity) {
        val intent = Intent(origem, ReceitasFavoritasActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        origem.startActivity(intent)
        // Vindo dos detalhes, fecha a tela de detalhes para a pilha ficar: Lista -> Favoritos
        if (origem is DetalhesReceitaActivity) {
            origem.finish()
        }
    }

    /** Liga a barra inferior: marca a aba atual e abre a outra tela quando tocar nela. */
    fun configurarBarra(origem: Activity, barra: BottomNavigationView, abaAtual: Aba) {
        barra.selectedItemId = if (abaAtual == Aba.RECEITAS) R.id.nav_receitas else R.id.nav_favoritos

        // Aba Favoritos: coração cheio quando selecionada, só contorno quando não
        val iconeFavoritos = if (abaAtual == Aba.FAVORITOS) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        barra.menu.findItem(R.id.nav_favoritos).setIcon(iconeFavoritos)

        barra.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_receitas -> if (abaAtual != Aba.RECEITAS) irParaReceitas(origem)
                R.id.nav_favoritos -> if (abaAtual != Aba.FAVORITOS) irParaFavoritas(origem)
            }
            // false: a seleção visual não muda; cada tela já mostra a sua própria aba marcada
            false
        }
    }
}
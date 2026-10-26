package com.example.app_catalogo_receitas_xml.ui.viewmodel

import com.example.app_catalogo_receitas_xml.model.Receita

/**
 * Item exibido no card da lista: a receita + se ela está favoritada.
 *
 * O estado de favorito faz parte do item para que o DiffUtil perceba a mudança
 * e refaça o bind do card (sem isso, o ícone do coração não atualizaria na lista).
 */
data class ReceitaItem(val receita: Receita, val favorita: Boolean)

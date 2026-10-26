package com.example.app_catalogo_receitas_xml.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_catalogo_receitas_xml.data.ReceitasDataSource
import com.example.app_catalogo_receitas_xml.model.Categoria
import com.example.app_catalogo_receitas_xml.model.Receita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Estado da lista de receitas já pronto para a tela desenhar.
 *
 * @property itens receitas que passaram pelo filtro de busca e categoria,
 *                 cada uma com a informação de favorita.
 */
data class ListaUiState(
    val itens: List<ReceitaItem>,
    val busca: String,
    val categoriaSelecionada: String
) {
    val vazio: Boolean get() = itens.isEmpty()
}

/**
 * ViewModel compartilhado pelos três Fragments via `activityViewModels()`.
 *
 * Favoritos, busca e categoria são persistidos no [SavedStateHandle],
 * sobrevivendo à rotação e à morte do processo pelo sistema.
 */
class ReceitasViewModel(
    private val estado: SavedStateHandle = SavedStateHandle()
) : ViewModel() {

    private val todasReceitas: List<Receita> = ReceitasDataSource.receitas

    // ── Estado interno (fonte de verdade) ─────────────────────────────────

    private val _favoritosIds = MutableStateFlow(
        estado.get<IntArray>(KEY_FAVORITOS)?.toSet() ?: emptySet()
    )
    private val _busca = MutableStateFlow(estado.get<String>(KEY_BUSCA) ?: "")
    private val _categoria = MutableStateFlow(estado.get<String>(KEY_CATEGORIA) ?: TODAS)

    // ── Estado exposto para as telas ─────────────────────────────────────

    val favoritosIds: StateFlow<Set<Int>> = _favoritosIds.asStateFlow()

    /** Categorias para os chips: "Todas" + as categorias existentes (sem repetição). */
    val categorias: List<String> =
        listOf(TODAS) + Categoria.entries.map { it.titulo }

    /** Lista da tela inicial, já filtrada por busca e categoria. */
    val listaUiState: StateFlow<ListaUiState> =
        combine(_busca, _categoria, _favoritosIds) { busca, categoria, favoritos ->
            montarLista(busca, categoria, favoritos)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = montarLista(_busca.value, _categoria.value, _favoritosIds.value)
        )

    /** Receitas favoritadas, para a tela de Favoritos. */
    val favoritas: StateFlow<List<Receita>> =
        _favoritosIds
            .map { ids -> todasReceitas.filter { it.id in ids } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
                initialValue = todasReceitas.filter { it.id in _favoritosIds.value }
            )

    // ── Consultas ─────────────────────────────────────────────────────────

    fun receitaPorId(id: Int): Receita? = todasReceitas.firstOrNull { it.id == id }

    // ── Ações vindas da UI ────────────────────────────────────────────────

    fun alternarFavorito(id: Int) {
        val novos = _favoritosIds.value.toMutableSet().apply {
            if (!remove(id)) add(id)
        }
        _favoritosIds.value = novos
        estado[KEY_FAVORITOS] = novos.toIntArray()
    }

    fun buscar(texto: String) {
        if (texto == _busca.value) return
        _busca.value = texto
        estado[KEY_BUSCA] = texto
    }

    fun selecionarCategoria(categoria: String) {
        if (categoria == _categoria.value) return
        _categoria.value = categoria
        estado[KEY_CATEGORIA] = categoria
    }

    // ── Regra de filtro ───────────────────────────────────────────────────

    private fun montarLista(
        busca: String,
        categoria: String,
        favoritos: Set<Int>
    ): ListaUiState {
        val termo = busca.trim()
        val itens = todasReceitas
            .filter { receita ->
                val bateCategoria = categoria == TODAS || receita.categoria.titulo == categoria
                val bateBusca = termo.isEmpty() ||
                        receita.titulo.contains(termo, ignoreCase = true) ||
                        receita.ingredientes.any { it.contains(termo, ignoreCase = true) }
                bateCategoria && bateBusca
            }
            .map { ReceitaItem(it, favorita = it.id in favoritos) }

        return ListaUiState(itens, busca, categoria)
    }

    companion object {
        const val TODAS = "Todas"
        private const val KEY_FAVORITOS = "favoritos_ids"
        private const val KEY_BUSCA = "busca"
        private const val KEY_CATEGORIA = "categoria"
        private const val STOP_TIMEOUT_MS = 5_000L
    }
}

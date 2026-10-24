package com.example.app_catalogo_receitas_xml.data

/**
 * Guarda os ids das receitas favoritas.
 *
 * É um object (singleton): existe uma única instância em todo o app, então as três
 * Activities enxergam a mesma lista de favoritos.
 */
object Favoritos {

    private val ids = mutableSetOf<Int>()

    fun contem(id: Int): Boolean = id in ids

    /** Se o id já é favorito, remove; senão, adiciona. */
    fun alternar(id: Int) {
        if (!ids.remove(id)) {
            ids.add(id)
        }
    }
}
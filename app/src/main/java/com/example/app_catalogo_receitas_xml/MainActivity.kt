package com.example.app_catalogo_receitas_xml

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.app_catalogo_receitas_xml.data.Receitas
import com.example.app_catalogo_receitas_xml.model.Receita
import com.example.app_catalogo_receitas_xml.ui.screens.DetalhesReceitaFragment
import com.example.app_catalogo_receitas_xml.ui.screens.ListaReceitasFragment
import com.example.app_catalogo_receitas_xml.ui.screens.ReceitasFavoritasFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),
    ListaReceitasFragment.Listener,
    ReceitasFavoritasFragment.Listener,
    DetalhesReceitaFragment.Listener {

    private val todasReceitas = Receitas.receitas
    private val favoritosIds = mutableSetOf<Int>()

    private lateinit var bottomNavContainer: View
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Restaura os favoritos após rotação / recriação da Activity
        savedInstanceState?.getIntArray(KEY_FAVORITOS)?.let { favoritosIds.addAll(it.toList()) }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        bottomNavContainer = findViewById(R.id.barraNavegacao)
        bottomNavView = bottomNavContainer.findViewById(R.id.bottomNavigationView)

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_receitas -> {
                    exibirFragmentoLista()
                    true
                }
                R.id.nav_favoritos -> {
                    exibirFragmentoFavoritos()
                    true
                }
                else -> false
            }
        }

        supportFragmentManager.addOnBackStackChangedListener { atualizarVisibilidadeBarra() }

        if (savedInstanceState == null) {
            exibirFragmentoLista()
        } else {
            atualizarVisibilidadeBarra()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(KEY_FAVORITOS, favoritosIds.toIntArray())
    }

    // Os fragments buscam os dados na Activity quando a view é criada
    // (ver obterReceitas / obterFavoritosIds / obterFavoritas / ehFavorita).
    private fun exibirFragmentoLista() {
        val fragment = ListaReceitasFragment.newInstance()
        trocarFragmento(fragment, TAG_LISTA)
    }

    private fun exibirFragmentoFavoritos() {
        val fragment = ReceitasFavoritasFragment.newInstance()
        trocarFragmento(fragment, TAG_FAVORITOS)
    }

    private fun trocarFragmento(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }

    private fun atualizarVisibilidadeBarra() {
        val fragmentAtual = supportFragmentManager.findFragmentById(R.id.fragment_container)
        bottomNavContainer.visibility =
            if (fragmentAtual is DetalhesReceitaFragment) View.GONE else View.VISIBLE
    }

    private fun listaFavoritas(): List<Receita> = todasReceitas.filter { it.id in favoritosIds }

    // ── Fonte de dados para os fragments ─────────────────────────────────

    override fun obterReceitas(): List<Receita> = todasReceitas

    override fun obterFavoritosIds(): Set<Int> = favoritosIds.toSet()

    override fun obterFavoritas(): List<Receita> = listaFavoritas()

    override fun ehFavorita(receita: Receita): Boolean = receita.id in favoritosIds

    // ── Callbacks dos Listeners ──────────────────────────────────────────

    override fun onReceitaClick(receita: Receita) {
        val fragment = DetalhesReceitaFragment.newInstance(
            receita = receita,
            favorita = receita.id in favoritosIds
        )
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onToggleFavorito(receita: Receita) {
        if (receita.id in favoritosIds) {
            favoritosIds.remove(receita.id)
        } else {
            favoritosIds.add(receita.id)
        }

        // Atualiza todas as telas existentes, inclusive a que está por baixo dos detalhes
        // na pilha (a lista/favoritos de onde o usuário veio). Se a view dela não existe
        // no momento, o estado é lido de novo quando ela for recriada.
        (supportFragmentManager.findFragmentByTag(TAG_LISTA) as? ListaReceitasFragment)
            ?.updateFavoritos(favoritosIds.toSet())
        (supportFragmentManager.findFragmentByTag(TAG_FAVORITOS) as? ReceitasFavoritasFragment)
            ?.updateFavoritas(listaFavoritas())
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as? DetalhesReceitaFragment)
            ?.setFavorita(receita.id in favoritosIds)
    }

    override fun onVoltar() {
        onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        private const val TAG_LISTA = "lista"
        private const val TAG_FAVORITOS = "favoritos"
        private const val KEY_FAVORITOS = "favoritos_ids"
    }
}

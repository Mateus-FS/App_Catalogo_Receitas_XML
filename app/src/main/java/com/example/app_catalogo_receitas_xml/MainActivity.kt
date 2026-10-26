package com.example.app_catalogo_receitas_xml

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.app_catalogo_receitas_xml.ui.screens.DetalhesReceitaFragment
import com.example.app_catalogo_receitas_xml.ui.screens.ListaReceitasFragment
import com.example.app_catalogo_receitas_xml.ui.screens.ReceitasFavoritasFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavContainer: View
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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

    companion object {
        private const val TAG_LISTA = "lista"
        private const val TAG_FAVORITOS = "favoritos"
    }
}

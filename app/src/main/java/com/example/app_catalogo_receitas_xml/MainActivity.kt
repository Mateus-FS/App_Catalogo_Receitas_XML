package com.example.app_catalogo_receitas_xml

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.app_catalogo_receitas_xml.databinding.ActivityMainBinding
import com.example.app_catalogo_receitas_xml.ui.screens.DetalhesReceitaFragment
import com.example.app_catalogo_receitas_xml.ui.screens.ListaReceitasFragment
import com.example.app_catalogo_receitas_xml.ui.screens.ReceitasFavoritasFragment

/**
 * Única Activity do app: hospeda os Fragments e a barra de navegação inferior.
 *
 * Não guarda estado de receitas nem de favoritos; isso é responsabilidade
 * do [com.example.app_catalogo_receitas_xml.ui.viewmodel.ReceitasViewModel].
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarInsets()
        configurarBarraNavegacao()

        supportFragmentManager.addOnBackStackChangedListener { atualizarVisibilidadeBarra() }

        if (savedInstanceState == null) {
            trocarFragmento(ListaReceitasFragment.newInstance(), TAG_LISTA)
        } else {
            // Os fragments são restaurados pelo sistema; só ajusta a barra.
            atualizarVisibilidadeBarra()
        }
    }

    private fun configurarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Sem padding inferior: a barra de navegação cuida do próprio espaçamento.
            view.setPadding(barras.left, barras.top, barras.right, 0)
            insets
        }
    }

    private fun configurarBarraNavegacao() {
        binding.barraNavegacao.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_receitas -> trocarFragmento(ListaReceitasFragment.newInstance(), TAG_LISTA)
                R.id.nav_favoritos ->
                    trocarFragmento(ReceitasFavoritasFragment.newInstance(), TAG_FAVORITOS)
                else -> return@setOnItemSelectedListener false
            }
            true
        }
    }

    private fun trocarFragmento(fragment: Fragment, tag: String) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, fragment, tag)
        }
    }

    /** A barra inferior some na tela de detalhes, que tem o próprio botão de voltar. */
    private fun atualizarVisibilidadeBarra() {
        val atual = supportFragmentManager.findFragmentById(R.id.fragment_container)
        binding.barraNavegacao.root.isVisible = atual !is DetalhesReceitaFragment
    }

    private companion object {
        const val TAG_LISTA = "lista"
        const val TAG_FAVORITOS = "favoritos"
    }
}
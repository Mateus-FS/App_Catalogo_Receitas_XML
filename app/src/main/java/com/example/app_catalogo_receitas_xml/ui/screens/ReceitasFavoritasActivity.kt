package com.example.app_catalogo_receitas_xml.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_catalogo_receitas_xml.data.Favoritos
import com.example.app_catalogo_receitas_xml.data.Receitas
import com.example.app_catalogo_receitas_xml.databinding.ActivityReceitasFavoritasBinding
import com.example.app_catalogo_receitas_xml.util.Aba
import com.example.app_catalogo_receitas_xml.util.Navegacao
import com.example.app_catalogo_receitas_xml.util.configurarBordaABorda
import com.example.app_catalogo_receitas_xml.util.somarInsetSuperior

/** Tela 3: receitas marcadas como favoritas. */
class ReceitasFavoritasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceitasFavoritasBinding
    private lateinit var adapter: ReceitaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configurarBordaABorda()
        binding = ActivityReceitasFavoritasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.conteudo.somarInsetSuperior()

        adapter = ReceitaAdapter(
            onClick = { receita -> Navegacao.abrirDetalhes(this, receita) },
            onToggleFavorito = { receita ->
                Favoritos.alternar(receita.id)
                carregar()
            }
        )
        binding.listaFavoritas.layoutManager = LinearLayoutManager(this)
        binding.listaFavoritas.adapter = adapter

        Navegacao.configurarBarra(this, binding.barraNavegacao.bottomNavigationView, Aba.FAVORITOS)
    }

    override fun onResume() {
        super.onResume()
        // Sempre que a tela aparece, relê os favoritos
        carregar()
    }

    private fun carregar() {
        val favoritas = Receitas.receitas.filter { Favoritos.contem(it.id) }
        adapter.submeter(favoritas)
        binding.estadoVazio.isVisible = favoritas.isEmpty()
    }
}
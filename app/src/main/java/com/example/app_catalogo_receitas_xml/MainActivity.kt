package com.example.app_catalogo_receitas_xml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_catalogo_receitas_xml.data.Favoritos
import com.example.app_catalogo_receitas_xml.data.Receitas
import com.example.app_catalogo_receitas_xml.databinding.ActivityMainBinding
import com.example.app_catalogo_receitas_xml.databinding.ComponentChipCategoriaBinding
import com.example.app_catalogo_receitas_xml.ui.screens.ReceitaAdapter
import com.example.app_catalogo_receitas_xml.util.Aba
import com.example.app_catalogo_receitas_xml.util.Navegacao
import com.example.app_catalogo_receitas_xml.util.configurarBordaABorda
import com.example.app_catalogo_receitas_xml.util.somarInsetSuperior

/** Tela 1 (tela inicial): lista de receitas com busca e filtro por categoria. */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ReceitaAdapter

    // Estado da tela: a categoria escolhida (o texto da busca fica guardado no próprio campo)
    private var categoriaSelecionada = TODAS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configurarBordaABorda()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.conteudo.somarInsetSuperior()

        // Recupera a categoria se a tela foi recriada (por exemplo, ao girar o celular)
        categoriaSelecionada = savedInstanceState?.getString(ESTADO_CATEGORIA) ?: TODAS

        adapter = ReceitaAdapter(
            onClick = { receita -> Navegacao.abrirDetalhes(this, receita) },
            onToggleFavorito = { receita ->
                Favoritos.alternar(receita.id)
                aplicarFiltros()
            }
        )
        binding.listaReceitas.layoutManager = LinearLayoutManager(this)
        binding.listaReceitas.adapter = adapter

        criarChips()
        binding.campoBusca.doAfterTextChanged { aplicarFiltros() }
        Navegacao.configurarBarra(this, binding.barraNavegacao.bottomNavigationView, Aba.RECEITAS)

        aplicarFiltros()
    }

    override fun onResume() {
        super.onResume()
        // Ao voltar de outra tela, os favoritos podem ter mudado: redesenha os corações
        aplicarFiltros()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ESTADO_CATEGORIA, categoriaSelecionada)
    }

    /** Chips: "Todas" + as categorias que existirem nas receitas. */
    private fun criarChips() {
        val categorias = listOf(TODAS) + Receitas.receitas
            .map { it.categoria }
            .filter { it.isNotBlank() }
            .distinct()

        categorias.forEach { categoria ->
            val chip = ComponentChipCategoriaBinding
                .inflate(layoutInflater, binding.chipsCategorias, false)
                .root
            chip.text = categoria
            chip.isChecked = categoria == categoriaSelecionada
            chip.setOnCheckedChangeListener { _, marcado ->
                if (marcado) {
                    categoriaSelecionada = categoria
                    aplicarFiltros()
                }
            }
            binding.chipsCategorias.addView(chip)
        }
    }

    /** Mostra só as receitas que combinam com a categoria escolhida E com o texto da busca. */
    private fun aplicarFiltros() {
        val termo = binding.campoBusca.text?.toString().orEmpty().trim()

        val filtradas = Receitas.receitas.filter { receita ->
            val bateCategoria = categoriaSelecionada == TODAS || receita.categoria == categoriaSelecionada
            val bateBusca = termo.isEmpty() ||
                    receita.titulo.contains(termo, ignoreCase = true) ||
                    receita.ingredientes.any { it.contains(termo, ignoreCase = true) }
            bateCategoria && bateBusca
        }

        adapter.submeter(filtradas)
        binding.mensagemVazia.isVisible = filtradas.isEmpty()
    }

    companion object {
        private const val TODAS = "Todas"
        private const val ESTADO_CATEGORIA = "categoria_selecionada"
    }
}
package com.example.app_catalogo_receitas_xml.ui.screens

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.databinding.ComponentChipCategoriaBinding
import com.example.app_catalogo_receitas_xml.databinding.FragmentListaReceitasBinding
import com.example.app_catalogo_receitas_xml.model.Categoria
import com.example.app_catalogo_receitas_xml.ui.components.ReceitaAdapter
import com.example.app_catalogo_receitas_xml.ui.components.SpacingItemDecoration
import com.example.app_catalogo_receitas_xml.ui.navigation.abrirDetalhes
import com.example.app_catalogo_receitas_xml.ui.viewmodel.ListaUiState
import com.example.app_catalogo_receitas_xml.ui.viewmodel.ReceitasViewModel
import kotlinx.coroutines.launch

/**
 * Tela inicial: busca, chips de categoria e lista de receitas.
 *
 * Não conhece a MainActivity: o estado vem do [ReceitasViewModel] compartilhado
 * e a tela apenas desenha o [ListaUiState] que ele publica.
 */
class ListaReceitasFragment : Fragment(R.layout.fragment_lista_receitas) {

    private val viewModel: ReceitasViewModel by activityViewModels()

    private var _binding: FragmentListaReceitasBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ReceitaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListaReceitasBinding.bind(view)

        configurarLista()
        configurarBusca()
        configurarChips()
        observarEstado()
    }

    override fun onDestroyView() {
        binding.recyclerReceitas.adapter = null
        _binding = null
        super.onDestroyView()
    }

    // ── Configuração das views ────────────────────────────────────────────

    private fun configurarLista() {
        adapter = ReceitaAdapter(
            onClick = { receita -> abrirDetalhes(receita.id) },
            onFavoritoClick = { receita -> viewModel.alternarFavorito(receita.id) }
        )
        with(binding.recyclerReceitas) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ListaReceitasFragment.adapter
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
            addItemDecoration(
                SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.card_spacing))
            )
        }
    }

    private fun configurarBusca() {
        // Texto inicial antes do listener, para não disparar busca ao voltar dos detalhes
        // ou ao recriar a view.
        binding.etBusca.setText(viewModel.listaUiState.value.busca)
        binding.etBusca.doAfterTextChanged { texto ->
            viewModel.buscar(texto?.toString().orEmpty())
        }
    }

    /**
     * Chips: "Todas" (representada por `null`) + uma por [Categoria].
     * As opções são fixas, então são criadas uma vez. A seleção vem do ViewModel e é
     * marcada antes de registrar o listener, evitando laço de eventos.
     */
    private fun configurarChips() {
        val selecionada = viewModel.listaUiState.value.categoriaSelecionada
        val opcoes: List<Categoria?> = listOf(null) + Categoria.entries
        binding.chipGroupCategorias.removeAllViews()

        opcoes.forEach { categoria ->
            // Infla o layout do chip para aplicar o estilo correto (seletores de cor).
            val chip = ComponentChipCategoriaBinding
                .inflate(layoutInflater, binding.chipGroupCategorias, false)
                .root
            chip.id = View.generateViewId()
            chip.text = categoria?.titulo ?: getString(R.string.categoria_todas)
            chip.isChecked = categoria == selecionada
            chip.setOnCheckedChangeListener { _, marcado ->
                if (marcado) viewModel.selecionarCategoria(categoria)
            }
            binding.chipGroupCategorias.addView(chip)
        }
    }

    // ── Estado → tela ─────────────────────────────────────────────────────

    private fun observarEstado() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listaUiState.collect(::renderizar)
            }
        }
    }

    private fun renderizar(estado: ListaUiState) {
        adapter.submitList(estado.itens)
        binding.txtVazio.isVisible = estado.vazio
        binding.recyclerReceitas.isVisible = !estado.vazio
    }

    companion object {
        fun newInstance() = ListaReceitasFragment()
    }
}
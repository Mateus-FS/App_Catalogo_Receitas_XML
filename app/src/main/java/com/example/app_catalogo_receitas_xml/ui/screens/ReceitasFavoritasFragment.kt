package com.example.app_catalogo_receitas_xml.ui.screens

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.databinding.FragmentReceitasFavoritasBinding
import com.example.app_catalogo_receitas_xml.model.Receita
import com.example.app_catalogo_receitas_xml.ui.viewmodel.ReceitaItem
import com.example.app_catalogo_receitas_xml.ui.viewmodel.ReceitasViewModel
import com.example.app_catalogo_receitas_xml.ui.components.SpacingItemDecoration
import kotlinx.coroutines.launch

/**
 * Tela de receitas favoritas.
 *
 * Usa o [ReceitasViewModel] compartilhado e ViewBinding.
 */
class ReceitasFavoritasFragment : Fragment(R.layout.fragment_receitas_favoritas) {

    private val viewModel: ReceitasViewModel by activityViewModels()

    private var _binding: FragmentReceitasFavoritasBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ReceitaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReceitasFavoritasBinding.bind(view)

        configurarLista()
        observarEstado()
    }

    override fun onDestroyView() {
        binding.recyclerFavoritas.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun configurarLista() {
        adapter = ReceitaAdapter(
            onClick = { receita -> abrirDetalhes(receita.id) },
            onFavoritoClick = { receita -> viewModel.alternarFavorito(receita.id) }
        )
        with(binding.recyclerFavoritas) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ReceitasFavoritasFragment.adapter
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
            addItemDecoration(
                SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.card_spacing))
            )
        }
    }

    private fun observarEstado() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoritas.collect(::renderizar)
            }
        }
    }

    private fun renderizar(favoritas: List<Receita>) {
        adapter.submitList(favoritas.map { ReceitaItem(it, favorita = true) })
        val vazio = favoritas.isEmpty()
        binding.layoutVazio.isVisible = vazio
        binding.recyclerFavoritas.isVisible = !vazio
    }

    private fun abrirDetalhes(receitaId: Int) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, DetalhesReceitaFragment.newInstance(receitaId))
            addToBackStack(null)
        }
    }

    companion object {
        fun newInstance() = ReceitasFavoritasFragment()
    }
}

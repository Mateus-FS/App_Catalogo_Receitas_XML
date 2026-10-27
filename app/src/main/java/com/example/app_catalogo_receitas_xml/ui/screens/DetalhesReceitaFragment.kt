package com.example.app_catalogo_receitas_xml.ui.screens

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.databinding.FragmentDetalhesReceitaBinding
import com.example.app_catalogo_receitas_xml.model.Receita
import com.example.app_catalogo_receitas_xml.ui.components.LinhaReceitaView
import com.example.app_catalogo_receitas_xml.ui.components.setFavorito
import com.example.app_catalogo_receitas_xml.ui.viewmodel.ReceitasViewModel
import kotlinx.coroutines.launch

/**
 * Tela de detalhes de uma receita.
 *
 * Recebe apenas o id pelos arguments; os dados vêm do [ReceitasViewModel] compartilhado.
 */
class DetalhesReceitaFragment : Fragment(R.layout.fragment_detalhes_receita) {

    private val viewModel: ReceitasViewModel by activityViewModels()

    private var _binding: FragmentDetalhesReceitaBinding? = null
    private val binding get() = _binding!!

    // requireArguments() falha cedo se o fragment for criado sem passar por newInstance().
    private val receitaId: Int get() = requireArguments().getInt(ARG_RECEITA_ID)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetalhesReceitaBinding.bind(view)

        val receita = viewModel.receitaPorId(receitaId)
        if (receita == null) {
            // Id inexistente: não deixa o usuário numa tela em branco.
            parentFragmentManager.popBackStack()
            return
        }

        configurarHeader(receita)
        configurarTextos(receita)
        configurarIngredientesEPassos(receita)
        configurarBotoes()
        observarFavorito()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // ── Configuração das views ────────────────────────────────────────────

    private fun configurarHeader(receita: Receita) {
        binding.headerCategoria.setBackgroundColor(
            ContextCompat.getColor(requireContext(), receita.categoria.fundoRes)
        )
        binding.txtEmojiHeader.text = receita.categoria.emoji
    }

    private fun configurarTextos(receita: Receita) {
        binding.txtCategoria.apply {
            isVisible = receita.categoria.titulo.isNotBlank()
            text = receita.categoria.titulo
        }

        binding.txtTituloReceita.text = receita.titulo

        val resumo = montarResumo(receita)
        binding.txtResumo.apply {
            isVisible = resumo.isNotEmpty()
            text = resumo
        }

        binding.txtDescricao.apply {
            isVisible = receita.descricao.isNotBlank()
            text = receita.descricao
        }
    }

    private fun configurarIngredientesEPassos(receita: Receita) {
        preencherLinhas(binding.containerIngredientes, receita.ingredientes) { "•" }
        preencherLinhas(binding.containerPassos, receita.passos) { indice -> "${indice + 1}." }
    }

    private fun configurarBotoes() {
        binding.btnVoltar.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.btnFavoritoDetalhe.setOnClickListener { viewModel.alternarFavorito(receitaId) }
    }

    // ── Estado → tela ─────────────────────────────────────────────────────

    private fun observarFavorito() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoritosIds.collect { ids ->
                    binding.btnFavoritoDetalhe.setFavorito(receitaId in ids)
                }
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    /** "tempo · dificuldade · N porções", ignorando o que estiver vazio. */
    private fun montarResumo(receita: Receita): String {
        val porcoes = if (receita.porcoes > 0) {
            resources.getQuantityString(R.plurals.porcoes, receita.porcoes, receita.porcoes)
        } else {
            ""
        }
        return listOf(receita.tempo, receita.dificuldade, porcoes)
            .filter { it.isNotBlank() }
            .joinToString(" · ")
    }

    /** Adiciona uma [LinhaReceitaView] por item; [marcador] recebe o índice do item. */
    private fun preencherLinhas(
        container: ViewGroup,
        itens: List<String>,
        marcador: (indice: Int) -> String
    ) {
        itens.forEachIndexed { indice, texto ->
            container.addView(
                LinhaReceitaView(requireContext()).apply { setLinha(marcador(indice), texto) }
            )
        }
    }

    companion object {
        private const val ARG_RECEITA_ID = "receita_id"

        fun newInstance(receitaId: Int) = DetalhesReceitaFragment().apply {
            arguments = bundleOf(ARG_RECEITA_ID to receitaId)
        }
    }
}
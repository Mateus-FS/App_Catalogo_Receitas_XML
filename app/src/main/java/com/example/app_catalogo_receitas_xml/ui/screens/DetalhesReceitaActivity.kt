package com.example.app_catalogo_receitas_xml.ui.screens

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.data.Favoritos
import com.example.app_catalogo_receitas_xml.databinding.ActivityDetalhesReceitaBinding
import com.example.app_catalogo_receitas_xml.databinding.ComponentLinhaListaBinding
import com.example.app_catalogo_receitas_xml.model.Receita
import com.example.app_catalogo_receitas_xml.util.Aba
import com.example.app_catalogo_receitas_xml.util.Navegacao
import com.example.app_catalogo_receitas_xml.util.configurarBordaABorda
import com.example.app_catalogo_receitas_xml.util.dp
import com.example.app_catalogo_receitas_xml.util.estiloDaCategoria
import com.example.app_catalogo_receitas_xml.util.somarInsetSuperior

/** Tela 2: detalhes de uma receita (a receita chega pela Intent). */
class DetalhesReceitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesReceitaBinding
    private lateinit var receita: Receita

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configurarBordaABorda()

        // Resgata a receita enviada com putExtra() pela tela anterior
        val recebida = IntentCompat.getParcelableExtra(intent, Navegacao.EXTRA_RECEITA, Receita::class.java)
        if (recebida == null) {
            finish()
            return
        }
        receita = recebida

        binding = ActivityDetalhesReceitaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.botoesTopo.somarInsetSuperior()

        preencherTela()

        binding.botaoVoltar.setOnClickListener { finish() }
        binding.botaoFavorito.setOnClickListener {
            Favoritos.alternar(receita.id)
            atualizarCoracao()
        }
        Navegacao.configurarBarra(this, binding.barraNavegacao.bottomNavigationView, Aba.RECEITAS)
    }

    private fun preencherTela() {
        val estilo = estiloDaCategoria(receita.categoria)
        binding.cabecalho.setBackgroundColor(estilo.fundo)
        binding.emoji.text = estilo.emoji

        binding.categoria.isVisible = receita.categoria.isNotBlank()
        binding.categoria.text = receita.categoria

        binding.titulo.text = receita.titulo

        // "50 min · Fácil · 8 porções"
        val resumo = listOf(
            receita.tempo,
            receita.dificuldade,
            if (receita.porcoes > 0) "${receita.porcoes} porções" else ""
        ).filter { it.isNotBlank() }.joinToString(" · ")
        binding.resumo.isVisible = resumo.isNotEmpty()
        binding.resumo.text = resumo

        binding.descricao.isVisible = receita.descricao.isNotBlank()
        binding.descricao.text = receita.descricao

        receita.ingredientes.forEach { ingrediente ->
            adicionarLinha(binding.listaIngredientes, "•", ingrediente, 20)
        }
        receita.passos.forEachIndexed { indice, passo ->
            adicionarLinha(binding.listaPassos, "${indice + 1}.", passo, 28)
        }

        atualizarCoracao()
    }

    /** Cria uma linha (marcador + texto) e coloca dentro da lista. */
    private fun adicionarLinha(lista: ViewGroup, marcador: String, texto: String, larguraMarcadorDp: Int) {
        val linha = ComponentLinhaListaBinding.inflate(layoutInflater, lista, false)
        linha.marcador.text = marcador
        linha.marcador.updateLayoutParams { width = dp(larguraMarcadorDp) }
        linha.texto.text = texto
        lista.addView(linha.root)
    }

    /** Coração cheio e vermelho se for favorita; contorno e cinza se não. */
    private fun atualizarCoracao() {
        val favorita = Favoritos.contem(receita.id)
        binding.botaoFavorito.setImageResource(
            if (favorita) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )
        val cor = if (favorita) R.color.favorito else R.color.texto_secundario
        binding.botaoFavorito.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, cor))
        binding.botaoFavorito.contentDescription = getString(
            if (favorita) R.string.cd_remover_favorito else R.string.cd_adicionar_favorito
        )
    }
}
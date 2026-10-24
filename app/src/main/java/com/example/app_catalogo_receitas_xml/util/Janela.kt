package com.example.app_catalogo_receitas_xml.util

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * Faz o app desenhar atrás das barras do sistema (tela cheia).
 * O app é claro, então os ícones da barra de status e de navegação ficam escuros.
 * Chamar ANTES de setContentView().
 */
fun ComponentActivity.configurarBordaABorda() {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
    )
}

/** Soma a altura da barra de status ao padding de cima da view (para o conteúdo não ficar embaixo dela). */
fun View.somarInsetSuperior() {
    val paddingOriginal = paddingTop
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(top = paddingOriginal + barras.top)
        insets
    }
}

/** Converte dp em pixels. */
fun Context.dp(valor: Int): Int = (valor * resources.displayMetrics.density).toInt()
package com.example.app_catalogo_receitas_xml.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.app_catalogo_receitas_xml.R

data class EstiloCategoria(
    val emoji: String,
    @ColorRes val fundoRes: Int
)

class MiniaturaReceitaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val container: FrameLayout
    private val txtEmoji: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.component_miniatura_receita, this, true)
        container = view.findViewById(R.id.miniaturaContainer)
        txtEmoji = view.findViewById(R.id.miniaturaEmoji)
    }

    fun setCategoria(categoria: String) {
        val estilo = estiloDaCategoria(categoria)
        txtEmoji.text = estilo.emoji
        container.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, estilo.fundoRes))
    }

    companion object {
        fun estiloDaCategoria(categoria: String): EstiloCategoria = when (categoria.lowercase().trim()) {
            "massas" -> EstiloCategoria("🍝", R.color.categoria_massas)
            "doces" -> EstiloCategoria("🍰", R.color.categoria_doces)
            "saladas" -> EstiloCategoria("🥗", R.color.categoria_saladas)
            "pratos principais" -> EstiloCategoria("🥘", R.color.categoria_pratos_principais)
            "café da manhã" -> EstiloCategoria("🥞", R.color.categoria_cafe_manha)
            else -> EstiloCategoria("🍽️", R.color.categoria_outros)
        }
    }
}

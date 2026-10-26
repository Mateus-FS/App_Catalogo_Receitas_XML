package com.example.app_catalogo_receitas_xml.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.app_catalogo_receitas_xml.R
import com.example.app_catalogo_receitas_xml.model.Categoria

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

    fun setCategoria(categoria: Categoria) {
        txtEmoji.text = categoria.emoji
        container.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, categoria.fundoRes))
    }
}

package com.example.app_catalogo_receitas_xml.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.app_catalogo_receitas_xml.databinding.ComponentMiniaturaReceitaBinding
import com.example.app_catalogo_receitas_xml.model.Categoria

/**
 * Quadradinho colorido com o emoji da categoria, usado no card da lista.
 * Utiliza o layout component_miniatura_receita.xml.
 */
class MiniaturaReceitaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding =
        ComponentMiniaturaReceitaBinding.inflate(LayoutInflater.from(context), this, true)

    fun setCategoria(categoria: Categoria) {
        binding.miniaturaEmoji.text = categoria.emoji
        binding.miniaturaContainer.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, categoria.fundoRes))
    }
}
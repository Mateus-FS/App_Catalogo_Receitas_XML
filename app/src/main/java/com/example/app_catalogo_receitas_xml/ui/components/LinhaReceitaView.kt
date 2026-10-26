package com.example.app_catalogo_receitas_xml.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.app_catalogo_receitas_xml.databinding.ComponentLinhaListaBinding

/**
 * Componente reutilizável para uma linha de ingrediente ou passo de preparo.
 * Utiliza o layout [com.example.app_catalogo_receitas_xml.R.layout.component_linha_lista].
 */
class LinhaReceitaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ComponentLinhaListaBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        orientation = HORIZONTAL
    }

    fun setLinha(marcador: String, texto: String) {
        binding.marcador.text = marcador
        binding.texto.text = texto
    }
}

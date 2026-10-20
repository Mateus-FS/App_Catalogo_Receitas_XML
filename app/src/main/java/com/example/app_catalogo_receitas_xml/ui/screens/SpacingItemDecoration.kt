package com.example.app_catalogo_receitas_xml.ui.screens

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Adiciona espaçamento vertical entre os itens do RecyclerView.
 * Equivale ao verticalArrangement = Arrangement.spacedBy(14.dp) do LazyColumn.
 */
class SpacingItemDecoration(private val spacingPx: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position > 0) outRect.top = spacingPx
    }
}

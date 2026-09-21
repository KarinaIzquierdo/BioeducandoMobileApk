package com.bioeducando.mobile.viewmodels.publico

import com.bioeducando.mobile.data.model.admin.Noticia

data class PublicNoticiasUiState(
    val noticias: List<Noticia> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val likedNoticias: Set<Int> = emptySet()
)

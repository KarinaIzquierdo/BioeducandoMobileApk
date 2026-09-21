package com.bioeducando.mobile.viewmodels.publico

import com.bioeducando.mobile.data.model.admin.Comentario
import com.bioeducando.mobile.data.model.admin.Publicacion

data class PublicComunidadUiState(
    val publicaciones: List<Publicacion> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val likedPosts: Set<Int> = emptySet(),
    val expandedComments: Set<Int> = emptySet(),
    val comentarios: Map<Int, List<Comentario>> = emptyMap(),
    val loadingComments: Set<Int> = emptySet()
)

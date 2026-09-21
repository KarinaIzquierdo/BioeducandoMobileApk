package com.bioeducando.mobile.viewmodels.admin

import com.bioeducando.mobile.data.model.admin.Comentario
import com.bioeducando.mobile.data.model.admin.Publicacion

data class AdminComunidadUiState(
    val publicaciones: List<Publicacion> = emptyList(),
    val isLoading: Boolean = false,
    val isPublishing: Boolean = false,
    val publishError: String? = null,
    val error: String? = null,
    val likedPostIds: Set<Int> = emptySet(),
    val selectedPostId: Int? = null,
    val selectedPostComments: List<Comentario> = emptyList(),
    val isLoadingComments: Boolean = false,
    val commentsError: String? = null
)

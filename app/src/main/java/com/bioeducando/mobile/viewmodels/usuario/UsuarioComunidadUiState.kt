package com.bioeducando.mobile.viewmodels.usuario

import com.bioeducando.mobile.data.model.admin.Publicacion

data class UsuarioComunidadUiState(
    val publicaciones: List<Publicacion> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPublishing: Boolean = false,
    val publishError: String? = null,
    val currentUserName: String? = null,
    val currentUserPhoto: String? = null,
    val isLoadingUser: Boolean = false,
    val userError: String? = null
)

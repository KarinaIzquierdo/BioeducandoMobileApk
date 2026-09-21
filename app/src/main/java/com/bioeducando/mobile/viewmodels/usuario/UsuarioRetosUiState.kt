package com.bioeducando.mobile.viewmodels.usuario

import com.bioeducando.mobile.data.model.admin.Reto

data class UsuarioRetosUiState(
    val retos: List<Reto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

package com.bioeducando.mobile.viewmodels.publico

import com.bioeducando.mobile.data.model.admin.Reto

data class PublicRetosUiState(
    val retos: List<Reto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

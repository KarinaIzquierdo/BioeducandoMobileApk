package com.bioeducando.mobile.viewmodels.admin

import com.bioeducando.mobile.data.model.admin.Reto

data class AdminRetosUiState(
    val retos: List<Reto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

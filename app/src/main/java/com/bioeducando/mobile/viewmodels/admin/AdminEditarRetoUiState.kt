package com.bioeducando.mobile.viewmodels.admin

import com.bioeducando.mobile.data.model.admin.Reto

data class AdminEditarRetoUiState(
    val reto: Reto? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

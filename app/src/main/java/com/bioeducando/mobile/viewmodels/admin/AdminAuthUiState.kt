package com.bioeducando.mobile.viewmodels.admin

import com.bioeducando.mobile.data.model.admin.User

data class AdminAuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val token: String? = null,
    val user: User? = null
)

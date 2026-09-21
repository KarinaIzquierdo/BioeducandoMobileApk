package com.bioeducando.mobile.viewmodels.admin

import com.bioeducando.mobile.data.model.admin.User

data class AdminPerfilUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isUpdatingPassword: Boolean = false,
    val error: String? = null,
    val saveMessage: String? = null,
    val passwordMessage: String? = null,
    val nameField: String = "",
    val emailField: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = ""
)

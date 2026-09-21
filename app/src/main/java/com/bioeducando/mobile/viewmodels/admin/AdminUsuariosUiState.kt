package com.bioeducando.mobile.viewmodels.admin

import com.bioeducando.mobile.data.model.admin.Role
import com.bioeducando.mobile.data.model.admin.User

data class AdminUsuariosUiState(
    val usuarios: List<User> = emptyList(),
    val roles: List<Role> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCreating: Boolean = false,
    val createMessage: String? = null,
    val showCreateDialog: Boolean = false,
    val newName: String = "",
    val newEmail: String = "",
    val newPassword: String = "",
    val newPasswordConfirmation: String = "",
    val selectedRoleId: Int? = null
)

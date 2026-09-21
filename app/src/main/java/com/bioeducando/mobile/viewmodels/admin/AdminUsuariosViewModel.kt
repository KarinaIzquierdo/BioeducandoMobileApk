package com.bioeducando.mobile.viewmodels.admin

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import com.bioeducando.mobile.repositories.admin.AdminRepository
import com.bioeducando.mobile.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

class AdminUsuariosViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = AdminRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(AdminUsuariosUiState())
    val uiState: StateFlow<AdminUsuariosUiState> = _uiState.asStateFlow()

    init {
        loadUsuarios()
        loadRoles()
    }

    fun loadUsuarios() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.getUsers(token)
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            usuarios = response.body() ?: emptyList()
                        )
                    }
                } else {
                    val body = response.errorBody()?.string()
                    val message = parseMessage(body)
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}: $message") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun loadRoles() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) return@launch

            try {
                val response = repository.getRoles(token)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(roles = response.body() ?: emptyList()) }
                }
            } catch (_: Exception) { }
        }
    }

    fun onNewNameChange(value: String) {
        _uiState.update { it.copy(newName = value) }
    }

    fun onNewEmailChange(value: String) {
        _uiState.update { it.copy(newEmail = value) }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update { it.copy(newPassword = value) }
    }

    fun onNewPasswordConfirmationChange(value: String) {
        _uiState.update { it.copy(newPasswordConfirmation = value) }
    }

    fun onNewRoleIdChange(roleId: Int) {
        _uiState.update { it.copy(selectedRoleId = roleId) }
    }

    fun showCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = true) }
    }

    fun hideCreateDialog() {
        _uiState.update {
            it.copy(
                showCreateDialog = false,
                newName = "",
                newEmail = "",
                newPassword = "",
                newPasswordConfirmation = "",
                selectedRoleId = null,
                createMessage = null
            )
        }
    }

    fun crearUsuario() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) {
                Toast.makeText(getApplication(), "No hay sesión activa", Toast.LENGTH_SHORT).show()
                return@launch
            }

            _uiState.update { it.copy(isCreating = true, createMessage = null) }

            val s = _uiState.value
            val name = s.newName.trim()
            val email = s.newEmail.trim()
            val password = s.newPassword
            val confirmation = s.newPasswordConfirmation
            val roleId = s.selectedRoleId

            if (name.isBlank() || email.isBlank() || password.isBlank() || roleId == null) {
                _uiState.update { it.copy(isCreating = false, createMessage = "Completa todos los campos") }
                return@launch
            }

            if (password != confirmation) {
                _uiState.update { it.copy(isCreating = false, createMessage = "Las contraseñas no coinciden") }
                return@launch
            }

            try {
                val response = repository.createUser(
                    token = token,
                    name = name,
                    email = email,
                    password = password,
                    passwordConfirmation = confirmation,
                    roleId = roleId
                )
                if (response.isSuccessful) {
                    _uiState.update { it.copy(isCreating = false, createMessage = "Usuario creado") }
                    Toast.makeText(getApplication(), "Usuario creado", Toast.LENGTH_SHORT).show()
                    hideCreateDialog()
                    loadUsuarios()
                } else {
                    val body = response.errorBody()?.string()
                    val message = parseMessage(body)
                    _uiState.update { it.copy(isCreating = false, createMessage = "Error: $message") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isCreating = false, createMessage = e.localizedMessage ?: "Error") }
            }
        }
    }

    private fun parseMessage(body: String?): String {
        return try {
            JSONObject(body ?: "").optString("message", body ?: "Error desconocido")
        } catch (_: Exception) {
            body ?: "Error desconocido"
        }
    }
}

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

class AdminPerfilViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = AdminRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(AdminPerfilUiState())
    val uiState: StateFlow<AdminPerfilUiState> = _uiState.asStateFlow()

    init {
        loadPerfil()
    }

    fun loadPerfil() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.getPerfil(token)
                if (response.isSuccessful) {
                    val user = response.body()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user,
                            nameField = user?.name ?: "",
                            emailField = user?.email ?: ""
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

    fun onNameChange(value: String) {
        _uiState.update { it.copy(nameField = value) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(emailField = value) }
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.update { it.copy(currentPassword = value) }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update { it.copy(newPassword = value) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
    }

    fun guardarCambios() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) {
                Toast.makeText(getApplication(), "No hay sesión activa", Toast.LENGTH_SHORT).show()
                return@launch
            }

            _uiState.update { it.copy(isSaving = true, saveMessage = null) }

            val name = _uiState.value.nameField
            val email = _uiState.value.emailField

            if (name.isBlank() || email.isBlank()) {
                _uiState.update { it.copy(isSaving = false, saveMessage = "Nombre y correo son obligatorios") }
                return@launch
            }

            try {
                val response = repository.updatePerfil(token, name, email)
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            saveMessage = "Cambios guardados",
                            user = response.body() ?: it.user
                        )
                    }
                    Toast.makeText(getApplication(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    val body = response.errorBody()?.string()
                    val message = parseMessage(body)
                    _uiState.update { it.copy(isSaving = false, saveMessage = "Error: $message") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, saveMessage = e.localizedMessage ?: "Error") }
            }
        }
    }

    fun actualizarContrasena() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) {
                Toast.makeText(getApplication(), "No hay sesión activa", Toast.LENGTH_SHORT).show()
                return@launch
            }

            _uiState.update { it.copy(isUpdatingPassword = true, passwordMessage = null) }

            val current = _uiState.value.currentPassword
            val new = _uiState.value.newPassword
            val confirm = _uiState.value.confirmPassword

            if (current.isBlank() || new.isBlank() || confirm.isBlank()) {
                _uiState.update { it.copy(isUpdatingPassword = false, passwordMessage = "Completa todos los campos") }
                return@launch
            }

            if (new != confirm) {
                _uiState.update { it.copy(isUpdatingPassword = false, passwordMessage = "Las contraseñas no coinciden") }
                return@launch
            }

            try {
                val response = repository.updatePerfilPassword(token, current, new, confirm)
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isUpdatingPassword = false,
                            passwordMessage = "Contraseña actualizada",
                            currentPassword = "",
                            newPassword = "",
                            confirmPassword = ""
                        )
                    }
                    Toast.makeText(getApplication(), "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                } else {
                    val body = response.errorBody()?.string()
                    val message = parseMessage(body)
                    _uiState.update { it.copy(isUpdatingPassword = false, passwordMessage = "Error: $message") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isUpdatingPassword = false, passwordMessage = e.localizedMessage ?: "Error") }
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

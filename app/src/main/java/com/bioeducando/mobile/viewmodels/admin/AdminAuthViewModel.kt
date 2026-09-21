package com.bioeducando.mobile.viewmodels.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.data.model.admin.AuthResponse
import com.bioeducando.mobile.data.model.admin.LoginRequest
import com.bioeducando.mobile.data.model.admin.RegisterRequest
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import com.bioeducando.mobile.repositories.admin.AdminRepository
import com.bioeducando.mobile.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminAuthViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = AdminRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(AdminAuthUiState())
    val uiState: StateFlow<AdminAuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            try {
                val response = repository.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        saveSession(body)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true,
                                token = body.access_token,
                                user = body.user
                            )
                        }
                    } ?: _uiState.update { it.copy(isLoading = false, error = "Respuesta vacía del servidor") }
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}: $errorBody") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun register(name: String, email: String, password: String, passwordConfirmation: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            try {
                val request = RegisterRequest(
                    name = name,
                    email = email,
                    password = password,
                    password_confirmation = passwordConfirmation,
                    role_id = 3
                )
                val response = repository.register(request)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        saveSession(body)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true,
                                token = body.access_token,
                                user = body.user
                            )
                        }
                    } ?: _uiState.update { it.copy(isLoading = false, error = "Respuesta vacía del servidor") }
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}: $errorBody") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun saveSession(response: AuthResponse) {
        tokenManager.saveToken(response.access_token)
        tokenManager.saveRole(response.user.role_id.toString())
        tokenManager.saveUser(
            response.user.id,
            response.user.name,
            response.user.email,
            response.user.profile_photo_url
        )
    }
}

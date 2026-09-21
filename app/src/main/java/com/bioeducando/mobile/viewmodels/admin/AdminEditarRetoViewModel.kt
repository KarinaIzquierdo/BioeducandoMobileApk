package com.bioeducando.mobile.viewmodels.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.data.model.admin.Reto
import com.bioeducando.mobile.data.model.admin.RetoUpdateRequest
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import com.bioeducando.mobile.repositories.admin.AdminRepository
import com.bioeducando.mobile.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminEditarRetoViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = AdminRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(AdminEditarRetoUiState())
    val uiState: StateFlow<AdminEditarRetoUiState> = _uiState.asStateFlow()

    fun loadReto(retoId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, successMessage = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.getReto(token, retoId)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, reto = response.body()) }
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}: $errorBody") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun updateReto(
        retoId: Int,
        reto: RetoUpdateRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, successMessage = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isSaving = false, error = "No hay sesión activa") }
                onError("No hay sesión activa")
                return@launch
            }

            try {
                val response = repository.updateReto(token, retoId, reto)
                if (response.isSuccessful) {
                    response.body()?.let { updated ->
                        _uiState.update { it.copy(isSaving = false, reto = updated, successMessage = "Misión actualizada") }
                    } ?: _uiState.update { it.copy(isSaving = false, successMessage = "Misión actualizada") }
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    _uiState.update { it.copy(isSaving = false, error = "Error ${response.code()}: $errorBody") }
                    onError("Error ${response.code()}: $errorBody")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.localizedMessage ?: "Error desconocido") }
                onError(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}

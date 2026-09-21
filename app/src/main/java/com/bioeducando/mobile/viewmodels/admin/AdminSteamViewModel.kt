package com.bioeducando.mobile.viewmodels.admin

import android.app.Application
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

class AdminSteamViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = AdminRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(AdminSteamUiState())
    val uiState: StateFlow<AdminSteamUiState> = _uiState.asStateFlow()

    fun loadProyectos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.getSteamProyectos(token)
                if (response.isSuccessful) {
                    val body = response.body()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            proyectos = body?.proyectos ?: emptyList(),
                            solicitudes = body?.solicitudes ?: emptyList()
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}: $errorBody") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun updateEstado(id: Int, estado: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: return@launch
            try {
                val response = repository.updateSteamEstado(token, id, estado)
                if (response.isSuccessful) {
                    loadProyectos()
                }
            } catch (_: Exception) {
            } finally {
                onComplete()
            }
        }
    }

    fun eliminarProyecto(id: Int, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: return@launch
            try {
                val response = repository.deleteSteamProyecto(token, id)
                if (response.isSuccessful) {
                    loadProyectos()
                }
            } catch (_: Exception) {
            } finally {
                onComplete()
            }
        }
    }
}

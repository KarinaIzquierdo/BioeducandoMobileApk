package com.bioeducando.mobile.viewmodels.usuario

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.data.model.admin.PraeInfo
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import com.bioeducando.mobile.repositories.usuario.UsuarioPraeRepository
import com.bioeducando.mobile.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioPraeViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = UsuarioPraeRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(UsuarioPraeUiState())
    val uiState: StateFlow<UsuarioPraeUiState> = _uiState.asStateFlow()

    init {
        loadPrae()
    }

    fun loadPrae() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.getPrae(token)
                if (response.isSuccessful) {
                    val body = response.body()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            info = body?.info ?: PraeInfo(),
                            actividades = body?.actividades ?: emptyList(),
                            documentos = body?.documentos ?: emptyList(),
                            error = null
                        )
                    }
                } else {
                    val body = response.errorBody()?.string()?.take(200)
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}${body?.let { ": $it" } ?: ""}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }
}

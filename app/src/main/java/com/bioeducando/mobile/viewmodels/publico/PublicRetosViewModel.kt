package com.bioeducando.mobile.viewmodels.publico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import com.bioeducando.mobile.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PublicRetosViewModel : ViewModel() {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)

    private val _uiState = MutableStateFlow(PublicRetosUiState())
    val uiState: StateFlow<PublicRetosUiState> = _uiState.asStateFlow()

    init {
        loadRetos()
    }

    fun loadRetos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val response = apiService.getPublicRetos()
                if (response.isSuccessful) {
                    response.body()?.let { retos ->
                        _uiState.update { it.copy(isLoading = false, retos = retos) }
                    } ?: _uiState.update { it.copy(isLoading = false, error = "Respuesta vacía del servidor") }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }
}

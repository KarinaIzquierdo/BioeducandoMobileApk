package com.bioeducando.mobile.viewmodels.usuario

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import com.bioeducando.mobile.repositories.usuario.UsuarioEcoEstudioRepository
import com.bioeducando.mobile.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioEcoEstudioViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = UsuarioEcoEstudioRepository(getApplication(), apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(UsuarioEcoEstudioUiState())
    val uiState: StateFlow<UsuarioEcoEstudioUiState> = _uiState.asStateFlow()

    init {
        loadContenidos()
    }

    fun onDescripcionChange(value: String) = _uiState.update { it.copy(descripcion = value) }
    fun onArchivoUriChange(uri: Uri?) = _uiState.update { it.copy(archivoUri = uri) }
    fun onPdfUriChange(uri: Uri?) = _uiState.update { it.copy(pdfUri = uri) }
    fun limpiarExito() = _uiState.update { it.copy(exito = null) }

    fun loadContenidos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                return@launch
            }
            try {
                val response = repository.getContenidos(token)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, contenidos = response.body() ?: emptyList()) }
                } else {
                    val body = response.errorBody()?.string()?.take(200)
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}${body?.let { ": $it" } ?: ""}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun publicar() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.descripcion.isBlank() || state.archivoUri == null) {
                _uiState.update { it.copy(error = "Descripción y archivo son obligatorios") }
                return@launch
            }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(error = "No hay sesión activa") }
                return@launch
            }

            _uiState.update { it.copy(isPublicando = true, error = null, exito = null) }

            try {
                val response = repository.publicar(
                    token = token,
                    descripcion = state.descripcion,
                    archivoUri = state.archivoUri,
                    pdfUri = state.pdfUri
                )
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isPublicando = false,
                            descripcion = "",
                            archivoUri = null,
                            pdfUri = null,
                            exito = "Publicado correctamente",
                            contenidos = listOfNotNull(response.body()) + it.contenidos
                        )
                    }
                    Toast.makeText(getApplication(), "Publicado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    val body = response.errorBody()?.string()?.take(200)
                    _uiState.update { it.copy(isPublicando = false, error = "Error ${response.code()}${body?.let { ": $it" } ?: ""}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isPublicando = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }
}

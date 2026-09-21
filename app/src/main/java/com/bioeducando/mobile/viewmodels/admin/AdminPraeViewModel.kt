package com.bioeducando.mobile.viewmodels.admin

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.bioeducando.mobile.data.model.admin.PraeInfo
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Locale

class AdminPraeViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = AdminRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(AdminPraeUiState())
    val uiState: StateFlow<AdminPraeUiState> = _uiState.asStateFlow()

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

    fun updateInfo(descripcion: String, objetivos: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSavingInfo = true, error = null, infoSaved = false) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isSavingInfo = false, error = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.updatePraeInfo(token, descripcion, objetivos)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(infoSaved = true, isSavingInfo = false) }
                    loadPrae()
                } else {
                    val body = response.errorBody()?.string()?.take(200)
                    _uiState.update { it.copy(isSavingInfo = false, error = "Error ${response.code()}${body?.let { ": $it" } ?: ""}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSavingInfo = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun setActividadTitulo(value: String) = _uiState.update { it.copy(actividadTitulo = value) }
    fun setActividadDescripcion(value: String) = _uiState.update { it.copy(actividadDescripcion = value) }
    fun setActividadFecha(value: String) = _uiState.update { it.copy(actividadFecha = value) }
    fun setActividadEstado(value: String) = _uiState.update { it.copy(actividadEstado = value) }
    fun clearActividadForm() = _uiState.update {
        it.copy(
            actividadTitulo = "",
            actividadDescripcion = "",
            actividadFecha = "",
            actividadEstado = "proxima"
        )
    }

    private fun formatearFecha(fecha: String): String {
        if (fecha.isBlank()) return fecha
        return try {
            val parser = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = parser.parse(fecha)
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date!!)
        } catch (e: Exception) {
            fecha
        }
    }

    fun createActividad() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSavingActividad = true, error = null, actividadSaved = false) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isSavingActividad = false, error = "No hay sesión activa") }
                return@launch
            }

            val state = _uiState.value
            try {
                val response = repository.createPraeActividad(
                    token,
                    state.actividadTitulo,
                    state.actividadDescripcion,
                    formatearFecha(state.actividadFecha),
                    state.actividadEstado
                )
                if (response.isSuccessful) {
                    _uiState.update { it.copy(actividadSaved = true, isSavingActividad = false) }
                    clearActividadForm()
                    loadPrae()
                } else {
                    val body = response.errorBody()?.string()?.take(200)
                    _uiState.update { it.copy(isSavingActividad = false, error = "Error ${response.code()}${body?.let { ": $it" } ?: ""}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSavingActividad = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun deleteActividad(id: Int) {
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: return@launch
            try {
                repository.deletePraeActividad(token, id)
                loadPrae()
            } catch (_: Exception) {
            }
        }
    }

    fun setDocumentoTitulo(value: String) = _uiState.update { it.copy(documentoTitulo = value) }
    fun setDocumentoUri(uri: Uri?) = _uiState.update { it.copy(documentoUri = uri) }

    fun createDocumento() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSavingDocumento = true, error = null, documentoSaved = false) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isSavingDocumento = false, error = "No hay sesión activa") }
                return@launch
            }

            val state = _uiState.value
            val uri = state.documentoUri
            if (uri == null) {
                _uiState.update { it.copy(isSavingDocumento = false, error = "Selecciona un archivo PDF") }
                return@launch
            }

            try {
                val part = createArchivoPart(uri) ?: run {
                    _uiState.update { it.copy(isSavingDocumento = false, error = "No se pudo leer el archivo") }
                    return@launch
                }
                val titulo = state.documentoTitulo.toRequestBody("text/plain".toMediaTypeOrNull())
                val response = repository.createPraeDocumento(token, titulo, part)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(documentoSaved = true, isSavingDocumento = false, documentoTitulo = "", documentoUri = null) }
                    loadPrae()
                } else {
                    val body = response.errorBody()?.string()?.take(200)
                    _uiState.update { it.copy(isSavingDocumento = false, error = "Error ${response.code()}${body?.let { ": $it" } ?: ""}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSavingDocumento = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun deleteDocumento(id: Int) {
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: return@launch
            try {
                repository.deletePraeDocumento(token, id)
                loadPrae()
            } catch (_: Exception) {
            }
        }
    }

    private fun createArchivoPart(uri: Uri): MultipartBody.Part? {
        val contentResolver = getApplication<Application>().contentResolver
        val type = contentResolver.getType(uri) ?: "application/pdf"
        return contentResolver.openInputStream(uri)?.use { input ->
            val bytes = input.readBytes()
            val body = bytes.toRequestBody(type.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("archivo", "documento.pdf", body)
        }
    }
}

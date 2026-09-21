package com.bioeducando.mobile.viewmodels.admin

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.data.model.admin.Noticia
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import com.bioeducando.mobile.repositories.admin.AdminRepository
import com.bioeducando.mobile.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

class AdminNoticiasViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = AdminRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(AdminNoticiasUiState())
    val uiState: StateFlow<AdminNoticiasUiState> = _uiState.asStateFlow()

    init {
        loadNoticias()
    }

    fun loadNoticias() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.getNoticias(token)
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            noticias = response.body() ?: emptyList()
                        )
                    }
                } else {
                    val body = response.errorBody()?.string()
                    val message = try {
                        JSONObject(body ?: "").optString("message", body ?: "Error desconocido")
                    } catch (_: Exception) {
                        body ?: "Error desconocido"
                    }
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}: $message") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun deleteNoticia(id: Int) {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) {
                Toast.makeText(getApplication(), "No hay sesión activa", Toast.LENGTH_SHORT).show()
                return@launch
            }

            try {
                val response = repository.deleteNoticia(token, id)
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Noticia eliminada", Toast.LENGTH_SHORT).show()
                    loadNoticias()
                } else {
                    val body = response.errorBody()?.string()
                    val message = try {
                        JSONObject(body ?: "").optString("message", body ?: "Error desconocido")
                    } catch (_: Exception) {
                        body ?: "Error desconocido"
                    }
                    Toast.makeText(getApplication(), "Error ${response.code()}: $message", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), e.localizedMessage ?: "Error desconocido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onTituloChange(value: String) = _uiState.update { it.copy(titulo = value) }
    fun onEntradillaChange(value: String) = _uiState.update { it.copy(entradilla = value) }
    fun onCuerpoChange(value: String) = _uiState.update { it.copy(cuerpo = value) }
    fun onCategoriaChange(value: String) = _uiState.update { it.copy(categoria = value) }
    fun onFechaPublicacionChange(value: String) = _uiState.update { it.copy(fechaPublicacion = value) }
    fun onEstadoChange(value: String) = _uiState.update { it.copy(estado = value) }
    fun onImagenUrlChange(value: String) = _uiState.update { it.copy(imagenUrl = value) }

    fun showCreateDialog() = _uiState.update {
        it.copy(
            showCreateDialog = true,
            createError = null,
            editingId = null,
            titulo = "",
            entradilla = "",
            cuerpo = "",
            categoria = "",
            fechaPublicacion = "",
            estado = "activa",
            imagenUrl = ""
        )
    }

    fun openEditDialog(noticia: Noticia) = _uiState.update {
        it.copy(
            showCreateDialog = true,
            createError = null,
            editingId = noticia.id,
            titulo = noticia.titulo,
            entradilla = noticia.descripcion,
            cuerpo = noticia.cuerpo ?: "",
            categoria = noticia.categoria,
            fechaPublicacion = noticia.fechaPublicacion ?: "",
            estado = noticia.estado,
            imagenUrl = noticia.imagen ?: ""
        )
    }

    fun hideCreateDialog() {
        _uiState.update {
            it.copy(
                showCreateDialog = false,
                editingId = null,
                createError = null,
                titulo = "",
                entradilla = "",
                cuerpo = "",
                categoria = "",
                fechaPublicacion = "",
                estado = "activa",
                imagenUrl = ""
            )
        }
    }

    fun guardarNoticia() {
        viewModelScope.launch {
            val state = _uiState.value

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(createError = "No hay sesión activa") }
                return@launch
            }

            if (state.titulo.isBlank() || state.entradilla.isBlank() || state.cuerpo.isBlank() ||
                state.categoria.isBlank() || state.fechaPublicacion.isBlank()
            ) {
                _uiState.update { it.copy(createError = "Completa todos los campos obligatorios") }
                return@launch
            }

            _uiState.update { it.copy(isCreating = true, createError = null) }

            try {
                val response = if (state.editingId == null) {
                    repository.createNoticia(
                        token = token,
                        titulo = state.titulo,
                        entradilla = state.entradilla,
                        cuerpo = state.cuerpo,
                        categoria = state.categoria,
                        fechaPublicacion = state.fechaPublicacion,
                        estado = state.estado,
                        imagen = state.imagenUrl
                    )
                } else {
                    repository.updateNoticia(
                        token = token,
                        id = state.editingId,
                        titulo = state.titulo,
                        entradilla = state.entradilla,
                        cuerpo = state.cuerpo,
                        categoria = state.categoria,
                        fechaPublicacion = state.fechaPublicacion,
                        estado = state.estado,
                        imagen = state.imagenUrl
                    )
                }

                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            showCreateDialog = false,
                            editingId = null,
                            titulo = "",
                            entradilla = "",
                            cuerpo = "",
                            categoria = "",
                            fechaPublicacion = "",
                            estado = "activa",
                            imagenUrl = "",
                            createError = null
                        )
                    }
                    val mensaje = if (state.editingId == null) "Noticia creada" else "Noticia actualizada"
                    Toast.makeText(getApplication(), mensaje, Toast.LENGTH_SHORT).show()
                    loadNoticias()
                } else {
                    val body = response.errorBody()?.string()
                    val message = try {
                        JSONObject(body ?: "").optString("message", body ?: "Error desconocido")
                    } catch (_: Exception) {
                        body ?: "Error desconocido"
                    }
                    _uiState.update { it.copy(isCreating = false, createError = "Error ${response.code()}: $message") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isCreating = false, createError = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }
}

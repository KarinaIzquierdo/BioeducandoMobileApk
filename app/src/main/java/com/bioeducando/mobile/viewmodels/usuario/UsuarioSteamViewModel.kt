package com.bioeducando.mobile.viewmodels.usuario

import android.app.Application
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import com.bioeducando.mobile.repositories.usuario.UsuarioSteamRepository
import com.bioeducando.mobile.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UsuarioSteamViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = UsuarioSteamRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(UsuarioSteamUiState())
    val uiState: StateFlow<UsuarioSteamUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(currentUserId = tokenManager.getUserId(), currentUserPhoto = tokenManager.getUserPhotoUrl()) }
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                return@launch
            }

            try {
                val steamResponse = repository.getSteamProyectos(token)
                val noticiasResponse = repository.getNoticias(token)

                val userId = _uiState.value.currentUserId

                if (steamResponse.isSuccessful) {
                    val body = steamResponse.body()
                    val proyectos = body?.proyectos ?: emptyList()
                    val solicitudes = body?.solicitudes ?: emptyList()

                    val misPropuestas = (proyectos.filter { it.userId == userId } +
                            solicitudes.filter { it.userId == userId })
                        .sortedByDescending { it.createdAt }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            proyectos = proyectos,
                            solicitudes = solicitudes,
                            misPropuestas = misPropuestas
                        )
                    }
                } else {
                    val errorBody = steamResponse.errorBody()?.string() ?: ""
                    _uiState.update { it.copy(isLoading = false, error = "Error ${steamResponse.code()}: $errorBody") }
                    return@launch
                }

                if (noticiasResponse.isSuccessful) {
                    _uiState.update { it.copy(noticias = noticiasResponse.body() ?: emptyList()) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun createProyecto(
        titulo: String,
        categoria: String,
        descripcion: String,
        imagenUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, createError = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isCreating = false, createError = "No hay sesión activa") }
                onError("No hay sesión activa")
                return@launch
            }

            try {
                val toBody: (String) -> okhttp3.RequestBody = { text ->
                    text.toRequestBody("text/plain".toMediaTypeOrNull())
                }

                val tituloBody = toBody(titulo)
                val categoriaBody = toBody(categoria)
                val descripcionBody = toBody(descripcion)
                val imagenPart = imagenUri?.let { prepareImagenPart(it) }

                val response = repository.createSteamProyecto(
                    token,
                    tituloBody,
                    categoriaBody,
                    descripcionBody,
                    objetivos = null,
                    materiales = null,
                    impactoAmbiental = null,
                    imagen = imagenPart,
                    destacado = null
                )

                if (response.isSuccessful) {
                    _uiState.update { it.copy(isCreating = false) }
                    loadData()
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    _uiState.update { it.copy(isCreating = false, createError = "Error ${response.code()}: $errorBody") }
                    onError("Error ${response.code()}: $errorBody")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isCreating = false, createError = e.localizedMessage ?: "Error desconocido") }
                onError(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    private fun prepareImagenPart(uri: Uri): MultipartBody.Part? {
        val context = getApplication<Application>()
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "image/*"
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"
        val filename = "proyecto_${System.currentTimeMillis()}.$extension"

        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val tempFile = File(context.cacheDir, filename)
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        val mediaType = mimeType.toMediaTypeOrNull() ?: "application/octet-stream".toMediaTypeOrNull()
        val requestBody = tempFile.asRequestBody(mediaType)
        return MultipartBody.Part.createFormData("imagen", filename, requestBody)
    }
}

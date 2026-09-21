package com.bioeducando.mobile.viewmodels.usuario

import android.app.Application
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import com.bioeducando.mobile.repositories.usuario.UsuarioComunidadRepository
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

class UsuarioComunidadViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = UsuarioComunidadRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(UsuarioComunidadUiState())
    val uiState: StateFlow<UsuarioComunidadUiState> = _uiState.asStateFlow()

    init {
        loadPerfil()
        loadPublicaciones()
    }

    fun loadPublicaciones() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.getPublicaciones(token)
                if (response.isSuccessful) {
                    response.body()?.let { publicaciones ->
                        _uiState.update { it.copy(isLoading = false, publicaciones = publicaciones) }
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

    fun loadPerfil() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingUser = true, userError = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoadingUser = false, userError = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.getPerfil(token)
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _uiState.update {
                            it.copy(
                                isLoadingUser = false,
                                currentUserName = user.name,
                                currentUserPhoto = user.profile_photo_url
                            )
                        }
                    } ?: _uiState.update { it.copy(isLoadingUser = false, userError = "Respuesta vacía del servidor") }
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    _uiState.update { it.copy(isLoadingUser = false, userError = "Error ${response.code()}: $errorBody") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingUser = false, userError = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun createPublicacion(
        contenido: String,
        mediaUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isPublishing = true, publishError = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isPublishing = false, publishError = "No hay sesión activa") }
                onError("No hay sesión activa")
                return@launch
            }

            try {
                val contenidoBody = contenido.toRequestBody("text/plain".toMediaTypeOrNull())
                val mediaPart = mediaUri?.let { prepareMediaPart(it) }

                val response = repository.createPublicacion(token, contenidoBody, mediaPart)

                if (response.isSuccessful) {
                    _uiState.update { it.copy(isPublishing = false) }
                    loadPublicaciones()
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    _uiState.update { it.copy(isPublishing = false, publishError = "Error ${response.code()}: $errorBody") }
                    onError("Error ${response.code()}: $errorBody")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isPublishing = false, publishError = e.localizedMessage ?: "Error desconocido") }
                onError(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    private fun prepareMediaPart(uri: Uri): MultipartBody.Part? {
        val context = getApplication<Application>()
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "image/*"
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"
        val filename = "media_${System.currentTimeMillis()}.$extension"

        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val tempFile = File(context.cacheDir, filename)
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        val mediaType = mimeType.toMediaTypeOrNull() ?: "application/octet-stream".toMediaTypeOrNull()
        val requestBody = tempFile.asRequestBody(mediaType)
        return MultipartBody.Part.createFormData("media", filename, requestBody)
    }
}

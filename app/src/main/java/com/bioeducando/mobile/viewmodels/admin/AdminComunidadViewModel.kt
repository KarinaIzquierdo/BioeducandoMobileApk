package com.bioeducando.mobile.viewmodels.admin

import android.app.Application
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.model.admin.Publicacion
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
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AdminComunidadViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = AdminRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(AdminComunidadUiState())
    val uiState: StateFlow<AdminComunidadUiState> = _uiState.asStateFlow()

    init {
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
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                publicaciones = publicaciones
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

    fun toggleLike(publicacion: Publicacion) {
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: return@launch

            val currentlyLiked = publicacion.id in _uiState.value.likedPostIds
            val action = if (currentlyLiked) "unlike" else "like"

            try {
                val response = repository.toggleLike(token, publicacion.id, action)
                if (response.isSuccessful) {
                    response.body()?.let { likeResponse ->
                        _uiState.update { state ->
                            state.copy(
                                publicaciones = state.publicaciones.map {
                                    if (it.id == publicacion.id) it.copy(likesCount = likeResponse.likesCount) else it
                                },
                                likedPostIds = if (currentlyLiked) {
                                    state.likedPostIds - publicacion.id
                                } else {
                                    state.likedPostIds + publicacion.id
                                }
                            )
                        }
                    }
                }
            } catch (_: Exception) {
                // Silenciar errores para no interrumpir la UI
            }
        }
    }

    fun deletePublicacion(postId: Int, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) {
                onError("No hay sesión activa")
                return@launch
            }

            try {
                val response = repository.deletePublicacion(token, postId)
                if (response.isSuccessful) {
                    _uiState.update { state ->
                        state.copy(
                            publicaciones = state.publicaciones.filterNot { it.id == postId },
                            likedPostIds = state.likedPostIds - postId
                        )
                    }
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    onError("Error ${response.code()}: $errorBody")
                }
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun openComentarios(postId: Int) {
        _uiState.update { it.copy(selectedPostId = postId) }
        loadComentarios(postId)
    }

    fun closeComentarios() {
        _uiState.update { it.copy(selectedPostId = null, selectedPostComments = emptyList(), commentsError = null) }
    }

    private fun loadComentarios(postId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingComments = true, commentsError = null) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isLoadingComments = false, commentsError = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.getComentarios(token, postId)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(isLoadingComments = false, selectedPostComments = response.body() ?: emptyList()) }
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    _uiState.update { it.copy(isLoadingComments = false, commentsError = "Error ${response.code()}: $errorBody") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingComments = false, commentsError = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun addComentario(postId: Int, contenido: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) {
                onError("No hay sesión activa")
                return@launch
            }

            try {
                val response = repository.storeComentario(token, postId, contenido)
                if (response.isSuccessful) {
                    loadComentarios(postId)
                    loadPublicaciones()
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    onError("Error ${response.code()}: $errorBody")
                }
            } catch (e: Exception) {
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

package com.bioeducando.mobile.viewmodels.admin

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class AdminCrearProyectoViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = AdminRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(AdminCrearProyectoUiState())
    val uiState: StateFlow<AdminCrearProyectoUiState> = _uiState.asStateFlow()

    fun updateTitulo(value: String) = _uiState.update { it.copy(titulo = value) }
    fun updateCategoria(value: String) = _uiState.update { it.copy(categoria = value) }
    fun updateDescripcion(value: String) = _uiState.update { it.copy(descripcion = value) }
    fun updateObjetivos(value: String) = _uiState.update { it.copy(objetivos = value) }
    fun updateMateriales(value: String) = _uiState.update { it.copy(materiales = value) }
    fun updateImpactoAmbiental(value: String) = _uiState.update { it.copy(impactoAmbiental = value) }
    fun updateDestacado(value: Boolean) = _uiState.update { it.copy(destacado = value) }
    fun setImagen(uri: Uri?) = _uiState.update { it.copy(imagenUri = uri) }

    fun guardar(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, saved = false) }

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(isSaving = false, error = "No hay sesión activa") }
                onError("No hay sesión activa")
                return@launch
            }

            val state = _uiState.value
            if (state.titulo.isBlank() || state.categoria.isBlank() || state.descripcion.isBlank()) {
                _uiState.update { it.copy(isSaving = false, error = "Título, categoría y descripción son obligatorios") }
                onError("Título, categoría y descripción son obligatorios")
                return@launch
            }

            try {
                val titulo = state.titulo.toRequestBody("text/plain".toMediaTypeOrNull())
                val categoria = state.categoria.toRequestBody("text/plain".toMediaTypeOrNull())
                val descripcion = state.descripcion.toRequestBody("text/plain".toMediaTypeOrNull())
                val objetivos = state.objetivos.takeIf { it.isNotBlank() }
                    ?.toRequestBody("text/plain".toMediaTypeOrNull())
                val materiales = state.materiales.takeIf { it.isNotBlank() }
                    ?.toRequestBody("text/plain".toMediaTypeOrNull())
                val impacto = state.impactoAmbiental.takeIf { it.isNotBlank() }
                    ?.toRequestBody("text/plain".toMediaTypeOrNull())
                val destacado = if (state.destacado) "1".toRequestBody("text/plain".toMediaTypeOrNull()) else null

                val imagen = state.imagenUri?.let { uri -> createImagenPart(uri) }

                val response = repository.createSteamProyecto(
                    token,
                    titulo,
                    categoria,
                    descripcion,
                    objetivos,
                    materiales,
                    impacto,
                    imagen,
                    destacado
                )

                if (response.isSuccessful) {
                    _uiState.update { AdminCrearProyectoUiState(saved = true) }
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

    private fun createImagenPart(uri: Uri): MultipartBody.Part? {
        val contentResolver = getApplication<Application>().contentResolver
        val maxDim = 1024
        val quality = 80

        try {
            val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, boundsOptions)
            }

            val (width, height) = boundsOptions.outWidth to boundsOptions.outHeight
            if (width == -1 || height == -1) return null

            var inSampleSize = 1
            while (width / inSampleSize >= maxDim || height / inSampleSize >= maxDim) {
                inSampleSize *= 2
            }

            val decodeOptions = BitmapFactory.Options().apply { this.inSampleSize = inSampleSize }
            val bitmap = contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, decodeOptions)
            } ?: return null

            val output = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
            val bytes = output.toByteArray()
            val body = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("imagen", "imagen.jpg", body)
        } catch (e: Exception) {
            return null
        }
    }
}

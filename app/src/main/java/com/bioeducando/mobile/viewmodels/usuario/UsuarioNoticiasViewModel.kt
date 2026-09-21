package com.bioeducando.mobile.viewmodels.usuario

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import com.bioeducando.mobile.repositories.usuario.UsuarioNoticiasRepository
import com.bioeducando.mobile.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioNoticiasViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)
    private val repository = UsuarioNoticiasRepository(apiService)
    private val tokenManager = TokenManager(getApplication())

    private val _uiState = MutableStateFlow(UsuarioNoticiasUiState())
    val uiState: StateFlow<UsuarioNoticiasUiState> = _uiState.asStateFlow()

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
                            noticias = response.body() ?: emptyList(),
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

    fun toggleLike(noticiaId: Int) {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(error = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.toggleLike(token, noticiaId)
                if (response.isSuccessful) {
                    val likeResponse = response.body()
                    likeResponse?.let { r ->
                        _uiState.update { state ->
                            state.copy(
                                noticias = state.noticias.map { noticia ->
                                    if (noticia.id == noticiaId) {
                                        noticia.copy(
                                            likes_count = r.likesCount,
                                            is_liked_by_user = r.isLikedByUser
                                        )
                                    } else noticia
                                }
                            )
                        }
                    }
                } else {
                    val body = response.errorBody()?.string()?.take(200)
                    _uiState.update { it.copy(error = "Error ${response.code()}${body?.let { ": $it" } ?: ""}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun abrirComentarios(noticiaId: Int) {
        _uiState.update { it.copy(comentariosNoticiaId = noticiaId, comentarios = emptyList()) }
        loadComentarios(noticiaId)
    }

    fun cerrarComentarios() {
        _uiState.update { it.copy(comentariosNoticiaId = null, comentarios = emptyList()) }
    }

    fun loadComentarios(noticiaId: Int) {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(error = "No hay sesión activa") }
                return@launch
            }

            try {
                val response = repository.getComentarios(token, noticiaId)
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(comentarios = response.body() ?: emptyList())
                    }
                } else {
                    val body = response.errorBody()?.string()?.take(200)
                    _uiState.update { it.copy(error = "Error ${response.code()}${body?.let { ": $it" } ?: ""}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun enviarComentario(noticiaId: Int, texto: String) {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(error = "No hay sesión activa") }
                return@launch
            }

            if (texto.isBlank()) return@launch

            _uiState.update { it.copy(isEnviandoComentario = true) }

            try {
                val response = repository.comentar(token, noticiaId, texto)
                if (response.isSuccessful) {
                    response.body()?.commentsCount?.let { count ->
                        _uiState.update { state ->
                            state.copy(
                                noticias = state.noticias.map { noticia ->
                                    if (noticia.id == noticiaId) {
                                        noticia.copy(comments_count = count)
                                    } else noticia
                                }
                            )
                        }
                    }
                    loadComentarios(noticiaId)
                    Toast.makeText(getApplication(), "Comentario enviado", Toast.LENGTH_SHORT).show()
                } else {
                    val body = response.errorBody()?.string()?.take(200)
                    _uiState.update { it.copy(error = "Error ${response.code()}${body?.let { ": $it" } ?: ""}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage ?: "Error desconocido") }
            } finally {
                _uiState.update { it.copy(isEnviandoComentario = false) }
            }
        }
    }
}

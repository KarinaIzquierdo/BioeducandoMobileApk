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

class PublicComunidadViewModel : ViewModel() {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)

    private val _uiState = MutableStateFlow(PublicComunidadUiState())
    val uiState: StateFlow<PublicComunidadUiState> = _uiState.asStateFlow()

    init {
        loadPublicaciones()
    }

    fun loadPublicaciones() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val response = apiService.getPublicPublicaciones()
                if (response.isSuccessful) {
                    response.body()?.let { publicaciones ->
                        _uiState.update { it.copy(isLoading = false, publicaciones = publicaciones) }
                    } ?: _uiState.update { it.copy(isLoading = false, error = "Respuesta vacía del servidor") }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun toggleLike(publicacionId: Int) {
        val liked = _uiState.value.likedPosts.contains(publicacionId)
        val action = if (liked) "unlike" else "like"

        viewModelScope.launch {
            try {
                val response = apiService.togglePublicLike(publicacionId, action)
                if (response.isSuccessful) {
                    val newCount = response.body()?.likesCount
                    _uiState.update { state ->
                        val newLiked = if (liked) {
                            state.likedPosts - publicacionId
                        } else {
                            state.likedPosts + publicacionId
                        }
                        val newList = state.publicaciones.map { post ->
                            if (post.id == publicacionId && newCount != null) {
                                post.copy(likesCount = newCount)
                            } else post
                        }
                        state.copy(likedPosts = newLiked, publicaciones = newList)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    fun toggleComments(publicacionId: Int) {
        val expanded = _uiState.value.expandedComments.contains(publicacionId)

        _uiState.update { state ->
            val newExpanded = if (expanded) {
                state.expandedComments - publicacionId
            } else {
                state.expandedComments + publicacionId
            }
            state.copy(expandedComments = newExpanded)
        }

        if (!expanded && !_uiState.value.comentarios.containsKey(publicacionId)) {
            loadComentarios(publicacionId)
        }
    }

    private fun loadComentarios(publicacionId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingComments = it.loadingComments + publicacionId) }

            try {
                val response = apiService.getPublicComentarios(publicacionId)
                if (response.isSuccessful) {
                    val comentarios = response.body() ?: emptyList()
                    _uiState.update { state ->
                        state.copy(
                            comentarios = state.comentarios + (publicacionId to comentarios),
                            loadingComments = state.loadingComments - publicacionId
                        )
                    }
                } else {
                    _uiState.update { it.copy(loadingComments = it.loadingComments - publicacionId) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(loadingComments = it.loadingComments - publicacionId) }
            }
        }
    }
}

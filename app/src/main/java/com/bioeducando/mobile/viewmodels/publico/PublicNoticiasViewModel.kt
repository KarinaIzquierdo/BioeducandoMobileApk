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

class PublicNoticiasViewModel : ViewModel() {

    private val apiService = RetrofitClient.instance.create(AdminApiService::class.java)

    private val _uiState = MutableStateFlow(PublicNoticiasUiState())
    val uiState: StateFlow<PublicNoticiasUiState> = _uiState.asStateFlow()

    init {
        loadNoticias()
    }

    fun loadNoticias() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val response = apiService.getPublicNoticias()
                if (response.isSuccessful) {
                    response.body()?.let { noticias ->
                        _uiState.update { it.copy(isLoading = false, noticias = noticias) }
                    } ?: _uiState.update { it.copy(isLoading = false, error = "Respuesta vacía del servidor") }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
        }
    }

    fun toggleLike(noticiaId: Int) {
        val liked = _uiState.value.likedNoticias.contains(noticiaId)
        val action = if (liked) "unlike" else "like"

        viewModelScope.launch {
            try {
                val response = apiService.togglePublicNoticiaLike(noticiaId, action)
                if (response.isSuccessful) {
                    val newCount = response.body()?.likesCount
                    _uiState.update { state ->
                        val newLiked = if (liked) {
                            state.likedNoticias - noticiaId
                        } else {
                            state.likedNoticias + noticiaId
                        }
                        val newList = state.noticias.map { noticia ->
                            if (noticia.id == noticiaId && newCount != null) {
                                noticia.copy(likes_count = newCount)
                            } else noticia
                        }
                        state.copy(likedNoticias = newLiked, noticias = newList)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }
}

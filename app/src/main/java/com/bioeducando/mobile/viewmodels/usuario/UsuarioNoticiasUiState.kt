package com.bioeducando.mobile.viewmodels.usuario

import com.bioeducando.mobile.data.model.admin.Noticia
import com.bioeducando.mobile.data.model.admin.NoticiaComentario

data class UsuarioNoticiasUiState(
    val noticias: List<Noticia> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val comentarios: List<NoticiaComentario> = emptyList(),
    val comentariosNoticiaId: Int? = null,
    val isEnviandoComentario: Boolean = false
)

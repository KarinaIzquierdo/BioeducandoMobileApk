package com.bioeducando.mobile.viewmodels.usuario

import com.bioeducando.mobile.data.model.admin.Noticia
import com.bioeducando.mobile.data.model.admin.ProyectoSteam

data class UsuarioSteamUiState(
    val proyectos: List<ProyectoSteam> = emptyList(),
    val solicitudes: List<ProyectoSteam> = emptyList(),
    val noticias: List<Noticia> = emptyList(),
    val misPropuestas: List<ProyectoSteam> = emptyList(),
    val isLoading: Boolean = false,
    val isCreating: Boolean = false,
    val error: String? = null,
    val createError: String? = null,
    val currentUserId: Int = -1,
    val currentUserPhoto: String? = null
)

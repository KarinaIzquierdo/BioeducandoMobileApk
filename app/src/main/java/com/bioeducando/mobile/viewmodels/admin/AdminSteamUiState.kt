package com.bioeducando.mobile.viewmodels.admin

import com.bioeducando.mobile.data.model.admin.ProyectoSteam

data class AdminSteamUiState(
    val proyectos: List<ProyectoSteam> = emptyList(),
    val solicitudes: List<ProyectoSteam> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

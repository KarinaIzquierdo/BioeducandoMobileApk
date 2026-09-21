package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class SteamResponse(
    @SerializedName("proyectos") val proyectos: List<ProyectoSteam> = emptyList(),
    @SerializedName("solicitudes") val solicitudes: List<ProyectoSteam> = emptyList()
)

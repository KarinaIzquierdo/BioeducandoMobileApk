package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class RetoUpdateRequest(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("estado") val estado: String,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("dificultad") val dificultad: String,
    @SerializedName("puntos") val puntos: Int,
    @SerializedName("duracion") val duracion: String?,
    @SerializedName("insignia") val insignia: String,
    @SerializedName("evidencias") val evidencias: List<String>
)

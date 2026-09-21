package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class Reto(
    @SerializedName("id") val id: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("estado") val estado: String?,
    @SerializedName("categoria") val categoria: String?,
    @SerializedName("dificultad") val dificultad: String?,
    @SerializedName("puntos") val puntos: Int = 0,
    @SerializedName("duracion") val duracion: String?,
    @SerializedName("insignia") val insignia: String?,
    @SerializedName("evidencias") val evidencias: List<String>?
)

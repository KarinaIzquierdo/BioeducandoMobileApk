package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class ProyectoSteam(
    @SerializedName("id") val id: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("categoria") val categoria: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("objetivos") val objetivos: String?,
    @SerializedName("materiales") val materiales: String?,
    @SerializedName("impacto_ambiental") val impactoAmbiental: String?,
    @SerializedName("estado") val estado: String?,
    @SerializedName("destacado") val destacado: Boolean = false,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("imagen_url") val imagenUrl: String?,
    @SerializedName("autor") val autor: String?,
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("created_at") val createdAt: String?
)

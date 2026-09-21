package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class Noticia(
    val id: Int,
    @SerializedName("user_name") val userName: String? = null,
    val antetitulo: String? = null,
    val titulo: String,
    val subtitulo: String? = null,
    val descripcion: String,
    val cuerpo: String?,
    val imagen: String?,
    @SerializedName("imagen_url") val imagenUrl: String?,
    val pie_foto: String?,
    val categoria: String,
    val estado: String,
    @SerializedName("fecha_publicacion") val fechaPublicacion: String?,
    val likes_count: Int = 0,
    @SerializedName("comments_count") val comments_count: Int = 0,
    @SerializedName("is_liked_by_user") val is_liked_by_user: Boolean = false
)

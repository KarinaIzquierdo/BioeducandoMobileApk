package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class Comentario(
    @SerializedName("id") val id: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_avatar") val userAvatar: String?,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("time_ago") val timeAgo: String
)

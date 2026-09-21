package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class Publicacion(
    @SerializedName("id") val id: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_avatar") val userAvatar: String?,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("media_url") val mediaUrl: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("likes_count") val likesCount: Int,
    @SerializedName("comments_count") val commentsCount: Int,
    @SerializedName("time_ago") val timeAgo: String
)

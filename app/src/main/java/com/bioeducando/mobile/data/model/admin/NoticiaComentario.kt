package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class NoticiaComentario(
    @SerializedName("id") val id: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("comentario") val comentario: String,
    @SerializedName("time_ago") val timeAgo: String,
    @SerializedName("comments_count") val commentsCount: Int? = null
)

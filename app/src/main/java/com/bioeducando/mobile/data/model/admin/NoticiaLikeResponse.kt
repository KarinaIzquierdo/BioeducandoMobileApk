package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class NoticiaLikeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("likes_count") val likesCount: Int,
    @SerializedName("is_liked_by_user") val isLikedByUser: Boolean
)

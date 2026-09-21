package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class LikeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("likes_count") val likesCount: Int
)

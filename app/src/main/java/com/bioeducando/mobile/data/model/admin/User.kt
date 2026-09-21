package com.bioeducando.mobile.data.model.admin

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role_id: Int,
    val profile_photo_url: String? = null,
    val role_name: String? = null,
    val created_at: String? = null
)

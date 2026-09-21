package com.bioeducando.mobile.data.model.admin

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String,
    val role_id: Int = 3
)

data class AuthResponse(
    val access_token: String,
    val token_type: String,
    val user: User
)

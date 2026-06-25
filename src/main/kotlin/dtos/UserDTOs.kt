package com.example.dtos


import kotlinx.serialization.Serializable

@Serializable
data class UserCreateRequest(
    val name: String,
    val email: String,
    val passwordRaw: String
)

@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
)
@Serializable
data class TokenRequest(
    val email: String,
    val passwordRaw: String
)

@Serializable
data class TokenResponse(val token: String, val userId: Int)
@Serializable
data class UserUpdateRequest(
    val name: String,
    val role: String
)
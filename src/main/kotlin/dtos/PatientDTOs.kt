package com.example.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PatientCreateRequest(
    val rut: String,
    val name: String,
    val email: String? = null
)

@Serializable
data class PatientResponse(
    val id: Int,
    val rut: String,
    val name: String,
    val email: String?
)
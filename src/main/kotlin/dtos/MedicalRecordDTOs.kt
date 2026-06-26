package com.example.dtos

import kotlinx.serialization.Serializable

@Serializable
data class MedicalRecordRequest(
    val patientId: Int,
    val doctorId: Int,
    val diagnosis: String,
    val treatment: String,
    val observations: String,
    val createdAt: String
)

@Serializable
data class MedicalRecordResponse(
    val id: Int,
    val patientId: Int,
    val doctorName: String, // Nombre del médico para mostrar en la app
    val diagnosis: String,
    val treatment: String,
    val observations: String,
    val createdAt: String // Fecha de la atención
)
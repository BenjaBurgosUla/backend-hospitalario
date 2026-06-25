package com.example.models


import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

// Esta es la tabla de registros clínicos
object MedicalRecords : IntIdTable("medical_records") {
    // Relacionamos el paciente con la tabla Users
    val patientId = reference("patient_id", Users, onDelete = ReferenceOption.CASCADE)

    // Relacionamos el doctor con la tabla Users
    val doctorId = reference("doctor_id", Users, onDelete = ReferenceOption.CASCADE)

    val diagnosis = varchar("diagnosis", 255)
    val treatment = text("treatment")
    val observations = text("observations")
    val createdAt = long("created_at") // Guardamos el tiempo como timestamp
}

// Clase de datos para trabajar en tu código Kotlin (Backend)
data class MedicalRecord(
    val id: Int,
    val patientId: Int,
    val doctorId: Int,
    val diagnosis: String,
    val treatment: String,
    val observations: String,
    val createdAt: Long
)
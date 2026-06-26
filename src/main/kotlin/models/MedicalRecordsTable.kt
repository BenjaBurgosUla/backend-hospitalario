package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

// Esta es la tabla de registros clínicos
object MedicalRecords : IntIdTable("medical_records") {

    // CORRECCIÓN 1: El paciente ahora se relaciona con la tabla Patients (o Pacientes, según tu PacienteTable.kt)
    val patientId = reference("patient_id", Patients, onDelete = ReferenceOption.CASCADE)

    // CORRECCIÓN 2: El doctor ahora se relaciona con la tabla Personal
    val doctorId = reference("doctor_id", Personal, onDelete = ReferenceOption.CASCADE)

    val diagnosis = varchar("diagnosis", 255)
    val treatment = text("treatment")
    val observations = text("observations")

    // Varchar para coincidir con la base de datos y la App
    val createdAt = varchar("created_at", 50)
}

// Clase de datos para trabajar en tu código Kotlin (Backend)
data class MedicalRecord(
    val id: Int,
    val patientId: Int,
    val doctorId: Int,
    val diagnosis: String,
    val treatment: String,
    val observations: String,
    val createdAt: String
)
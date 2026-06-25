package com.example.repository

import com.example.models.MedicalRecords
import com.example.models.MedicalRecord
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MedicalRecordRepository {

    // Guardar una nueva nota
    fun addRecord(patientId: Int, doctorId: Int, diagnosis: String, treatment: String, observations: String, createdAt: Long): Int {
        return transaction {
            MedicalRecords.insert {
                it[MedicalRecords.patientId] = patientId
                it[MedicalRecords.doctorId] = doctorId
                it[MedicalRecords.diagnosis] = diagnosis
                it[MedicalRecords.treatment] = treatment
                it[MedicalRecords.observations] = observations
                it[MedicalRecords.createdAt] = createdAt
            } get MedicalRecords.id
        }.value
    }

    // Obtener todas las notas de un paciente específico
    fun getRecordsByPatientId(patientId: Int): List<MedicalRecord> {
        return transaction {
            MedicalRecords.select { MedicalRecords.patientId eq patientId }
                .map { row ->
                    MedicalRecord(
                        id = row[MedicalRecords.id].value,
                        patientId = row[MedicalRecords.patientId].value,
                        doctorId = row[MedicalRecords.doctorId].value,
                        diagnosis = row[MedicalRecords.diagnosis],
                        treatment = row[MedicalRecords.treatment],
                        observations = row[MedicalRecords.observations],
                        createdAt = row[MedicalRecords.createdAt]
                    )
                }
        }
    }
}
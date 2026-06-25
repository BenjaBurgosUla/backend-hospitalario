package com.example.services

import com.example.dtos.MedicalRecordRequest
import com.example.dtos.MedicalRecordResponse
import com.example.repository.MedicalRecordRepository
import com.example.repository.UserRepository

class MedicalService(
    private val medicalRepository: MedicalRecordRepository,
    private val userRepository: UserRepository
) {

    fun addRecord(request: MedicalRecordRequest): Int {
        val timestamp = System.currentTimeMillis()
        return medicalRepository.addRecord(
            request.patientId,
            request.doctorId,
            request.diagnosis,
            request.treatment,
            request.observations,
            timestamp
        )
    }

    fun getRecordsForPatient(patientId: Int): List<MedicalRecordResponse> {
        val records = medicalRepository.getRecordsByPatientId(patientId)

        // Convertimos el registro a una respuesta con nombre del médico
        return records.map { record ->
            val doctor = userRepository.getUserById(record.doctorId)
            MedicalRecordResponse(
                id = record.id,
                patientId = record.patientId,
                doctorName = doctor?.name ?: "Desconocido",
                diagnosis = record.diagnosis,
                treatment = record.treatment,
                observations = record.observations,
                createdAt = record.createdAt.toString() // O formatear a fecha legible
            )
        }
    }
}
package com.example.services

import com.example.dtos.MedicalRecordRequest
import com.example.dtos.MedicalRecordResponse
import com.example.repository.MedicalRecordRepository
import com.example.repository.PersonalRepository

class MedicalService(
    private val medicalRepository: MedicalRecordRepository,
    private val personalRepository: PersonalRepository
) {

    fun addRecord(request: MedicalRecordRequest): Int {
        return medicalRepository.addRecord(
            request.patientId,
            request.doctorId,
            request.diagnosis,
            request.treatment,
            request.observations,
            request.createdAt
        )
    }

    fun getRecordsForPatient(patientId: Int): List<MedicalRecordResponse> {
        val records = medicalRepository.getRecordsByPatientId(patientId)

        return records.map { record ->
            // Ahora llama a getUserById y recibe un PersonalData
            val doctor = personalRepository.getUserById(record.doctorId)
            MedicalRecordResponse(
                id = record.id,
                patientId = record.patientId,
                doctorName = doctor?.name ?: "Desconocido",
                diagnosis = record.diagnosis,
                treatment = record.treatment,
                observations = record.observations,
                createdAt = record.createdAt
            )
        }
    }
}
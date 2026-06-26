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
        // CORRECCIÓN: Ya no generamos el número aquí.
        // Pasamos directamente la fecha exacta (String) que la app Android acaba de crear.
        return medicalRepository.addRecord(
            request.patientId,
            request.doctorId,
            request.diagnosis,
            request.treatment,
            request.observations,
            request.createdAt // <-- Ahora pasa limpio a la base de datos
        )
    }

    fun getRecordsForPatient(patientId: Int): List<MedicalRecordResponse> {
        val records = medicalRepository.getRecordsByPatientId(patientId)

        // Convertimos el registro a una respuesta con nombre del médico
        return records.map { record ->
            val doctor = personalRepository.getUserById(record.doctorId)
            MedicalRecordResponse(
                id = record.id,
                patientId = record.patientId,
                doctorName = doctor?.name ?: "Desconocido",
                diagnosis = record.diagnosis,
                treatment = record.treatment,
                observations = record.observations,
                createdAt = record.createdAt // Ya viene en formato texto listo para leer
            )
        }
    }
}
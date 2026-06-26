package com.example.routes

import com.example.dtos.PatientCreateRequest
import com.example.dtos.PatientResponse
import com.example.repository.PatientRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.patientRoutes(patientRepository: PatientRepository) {
    route("/patients") {

        // 1. Obtener la lista de pacientes reales
        get {
            val patients = patientRepository.getAllPatients().map {
                PatientResponse(it.id, it.rut, it.name, it.email)
            }
            call.respond(patients)
        }

        // 2. Insertar un paciente con validación de existencia de RUT
        post {
            try {
                val request = call.receive<PatientCreateRequest>()

                // Validación en el servidor: Verificamos si el RUT ya existe en Neon
                val existente = patientRepository.getPatientByRut(request.rut.trim())
                if (existente != null) {
                    call.respond(HttpStatusCode.Conflict, "El RUT ya está registrado en el sistema")
                    return@post
                }

                val newId = patientRepository.addPatient(
                    rut = request.rut.trim(),
                    name = request.name.trim(),
                    email = request.email?.trim()
                )
                call.respond(HttpStatusCode.Created, mapOf("id" to newId))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Error al procesar la solicitud")
            }
        }
    }
}
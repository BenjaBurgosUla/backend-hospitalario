package com.example.routes

import com.example.dtos.MedicalRecordRequest
import com.example.services.MedicalService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.medicalRoutes(medicalService: MedicalService) {
    route("/medical-records") {

        // Protegemos todas estas rutas con el guardia JWT
        authenticate("auth-jwt") {

            // 1. Guardar una nueva nota clínica
            post {
                val request = call.receive<MedicalRecordRequest>()
                val recordId = medicalService.addRecord(request)

                if (recordId > 0) {
                    call.respond(HttpStatusCode.Created, "Nota clínica creada con ID: $recordId")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Error al guardar la nota")
                }
            }

            // 2. Obtener el historial clínico de un paciente específico
            get("/{patientId}") {
                val patientId = call.parameters["patientId"]?.toIntOrNull()

                if (patientId == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID de paciente inválido")
                    return@get
                }

                val records = medicalService.getRecordsForPatient(patientId)
                call.respond(HttpStatusCode.OK, records)
            }
        }
    }
}
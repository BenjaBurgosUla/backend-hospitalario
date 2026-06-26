package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Importaciones de Repositorios
import com.example.repository.PersonalRepository
import com.example.repository.MedicalRecordRepository
import com.example.repository.PatientRepository // <-- AGREGADO

// Importaciones de Servicios
import com.example.services.MedicalService
import com.example.services.PersonalService

// Importaciones de Rutas
import com.example.routes.userRoutes
import com.example.routes.medicalRoutes
import com.example.routes.patientRoutes // <-- AGREGADO

fun Application.configureRouting() {
    // 1. Instanciamos las capas de PERSONAL
    val personalRepository = PersonalRepository()
    val personalService = PersonalService(personalRepository)

    // 2. Instanciamos las capas MÉDICAS
    val medicalRepository = MedicalRecordRepository()
    val medicalService = MedicalService(medicalRepository, personalRepository)

    // 3. Instanciamos la capa de PACIENTES (Faltaba esto)
    val patientRepository = PatientRepository()

    routing {
        get("/") {
            call.respondText("¡El servidor del Hospital está funcionando!")
        }

        // Activamos TODAS las rutas
        userRoutes(personalService)
        medicalRoutes(medicalService)

        // ¡LA PIEZA CLAVE QUE FALTABA!
        patientRoutes(patientRepository)
    }
}
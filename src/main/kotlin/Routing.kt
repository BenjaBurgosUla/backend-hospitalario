package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Importaciones
import com.example.repository.PersonalRepository
import com.example.repository.MedicalRecordRepository // <-- Nueva
import com.example.services.MedicalService           // <-- Nueva
import com.example.services.PersonalService
import com.example.routes.userRoutes
import com.example.routes.medicalRoutes

fun Application.configureRouting() {
    // 1. Instanciamos las capas de USUARIOS
    val personalRepository = PersonalRepository()
    val personalService = PersonalService(personalRepository)

    // 2. Instanciamos las capas de MÉDICA
    val medicalRepository = MedicalRecordRepository()
    // Nota: El servicio médico necesita el repositorio médico Y el de usuarios (para buscar nombres de doctores)
    val medicalService = MedicalService(medicalRepository, personalRepository)

    routing {
        get("/") {
            call.respondText("¡El servidor del Hospital está funcionando!")
        }

        // Activamos las rutas
        userRoutes(personalService)
        medicalRoutes(medicalService)
    }
}
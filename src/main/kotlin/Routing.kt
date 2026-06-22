package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Importaciones corregidas con la ruta completa:
import com.example.repository.UserRepository
import com.example.services.UserService
import com.example.routes.userRoutes

fun Application.configureRouting() {
    // Instanciamos nuestras capas
    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    routing {
        get("/") {
            call.respondText("¡El servidor del Hospital está funcionando!")
        }

        // Activamos las rutas de usuarios
        userRoutes(userService)
    }
}
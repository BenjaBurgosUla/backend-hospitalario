package com.example.routes

import com.example.dtos.UserCreateRequest
import com.example.dtos.TokenRequest
import com.example.dtos.UserUpdateRequest
import com.example.services.UserService
import io.ktor.server.auth.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService) {
    route("/users") {

        // 1. RUTA PARA CREAR PACIENTE
        post {
            val request = call.receive<UserCreateRequest>()
            val newUser = userService.registerUser(request)

            if (newUser != null) {
                call.respond(HttpStatusCode.Created, newUser)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Error al crear")
            }
        }

        // 2. RUTA PARA INICIAR SESIÓN
        post("/login") {
            val request = call.receive<TokenRequest>()
            val response = userService.loginUser(request)

            if (response != null) {
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Correo o contraseña incorrectos")
            }
        }

        // 3. EL GUARDIA DE SEGURIDAD
        authenticate("auth-jwt") {

            // NUEVA RUTA: OBTENER TODOS LOS USUARIOS (Para el Administrador)
            get {
                // Asumimos que tienes o crearás un método getAllUsers en tu UserService
                val allUsers = userService.getAllUsers()
                call.respond(HttpStatusCode.OK, allUsers)
            }

            // RUTA PARA BUSCAR POR ID (GET)
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@get
                }

                val user = userService.getUserById(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound, "No encontrado")
                }
            }

            // RUTA PARA ACTUALIZAR (PUT)
            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@put
                }

                val request = call.receive<UserUpdateRequest>()
                val updatedUser = userService.updateUser(id, request)

                if (updatedUser != null) {
                    call.respond(HttpStatusCode.OK, updatedUser)
                } else {
                    call.respond(HttpStatusCode.NotFound, "No se pudo actualizar o usuario no encontrado")
                }
            }

            // RUTA PARA ELIMINAR (DELETE)
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@delete
                }

                val success = userService.deleteUser(id)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Usuario eliminado correctamente del sistema")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                }
            }
        }
    }
}
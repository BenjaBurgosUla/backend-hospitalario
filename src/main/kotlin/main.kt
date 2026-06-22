package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import com.example.config.DatabaseFactory

fun main() {
    // Forzamos al servidor a encender leyendo nuestra función module() directamente
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // 1. Ahora sí o sí se conectará a Neon primero
    DatabaseFactory.init()

    // 2. Encendemos los plugins que ya tenías
    configureSecurity()
    configureSerialization()
    configureRouting()
}
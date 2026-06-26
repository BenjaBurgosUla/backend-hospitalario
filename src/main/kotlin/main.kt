package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import com.example.config.DatabaseFactory

fun main() {
    // El servidor buscará el puerto que le asigne la nube, si no lo encuentra, usará el 8080 (para tu PC)
    val port = System.getenv("PORT")?.toInt() ?: 8080

    // Es CRÍTICO que el host sea "0.0.0.0" para que acepte conexiones externas
    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        // SOLUCIÓN: Llamamos a module() para que ejecute TODA la configuración, no solo las rutas
        module()
    }.start(wait = true)
}

fun Application.module() {
    // 1. Ahora sí o sí se conectará a Neon primero
    DatabaseFactory.init()

    // 2. Encendemos los plugins que ya tenías
    configureSecurity()
    configureSerialization()
    configureRouting()
}
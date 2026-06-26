package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.github.cdimascio.dotenv.dotenv

fun Application.configureSecurity() {
    // Evitamos el pánico si no encuentra el .env en la nube
    val env = dotenv {
        ignoreIfMissing = true
    }

    // Le pasamos la misma llave secreta para que pueda verificar las pulseras
    val secretKey = env["JWT_SECRET"] ?: "clave_alternativa"

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT.require(Algorithm.HMAC256(secretKey))
                    .withAudience("hospital_audience")
                    .withIssuer("hospital_issuer")
                    .build()
            )
            validate { credential ->
                // Si el token tiene un email guardado adentro, lo dejamos pasar
                if (credential.payload.getClaim("email").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                // Este es el mensaje que dará el guardia si alguien entra sin pulsera
                call.respond(HttpStatusCode.Unauthorized, "¡Alto ahí! No tienes permiso. Falta tu Token JWT.")
            }
        }
    }
}
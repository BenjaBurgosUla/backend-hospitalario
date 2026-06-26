package com.example.config

import com.example.models.MedicalRecords
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.models.Users
import io.github.cdimascio.dotenv.dotenv

object DatabaseFactory {
    fun init() {
        // Cargamos las variables de entorno, ignorando si falta el archivo .env (vital para la nube)
        val env = dotenv {
            ignoreIfMissing = true
        }

        // Jalamos los datos ocultos (los leerá del .env en tu PC, o del panel de Render en producción)
        val jdbcUrl = env["DB_URL"]
        val dbUser = env["DB_USER"]
        val dbPassword = env["DB_PASSWORD"]

        // Conectamos usando las variables guardadas bajo llave
        Database.connect(
            url = jdbcUrl ?: "",
            driver = "org.postgresql.Driver",
            user = dbUser ?: "",
            password = dbPassword ?: ""
        )

        transaction {
            SchemaUtils.create(Users)
        }

        println("¡Conexión a Neon exitosa y segura!")
    }
}
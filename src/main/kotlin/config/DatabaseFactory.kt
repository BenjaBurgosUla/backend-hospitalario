package com.example.config

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.models.Users
import io.github.cdimascio.dotenv.dotenv

object DatabaseFactory {
    fun init() {
        // Cargamos el archivo .env
        val env = dotenv()

        // Jalamos los datos ocultos
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
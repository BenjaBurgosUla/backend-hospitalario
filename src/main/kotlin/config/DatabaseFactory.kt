package com.example.config

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.models.Personal
import com.example.models.Patients
import com.example.models.MedicalRecords
// Importa aquí tus futuras tablas cuando las crees:
// import com.example.models.Salas
// import com.example.models.Insumos
// import com.example.models.Citas

import io.github.cdimascio.dotenv.dotenv

object DatabaseFactory {
    fun init() {
        // Cargamos las variables de entorno para la seguridad de la conexión
        val env = dotenv {
            ignoreIfMissing = true
        }

        val jdbcUrl = env["DB_URL"]
        val dbUser = env["DB_USER"]
        val dbPassword = env["DB_PASSWORD"]

        // Establecemos la conexión a la base de datos PostgreSQL en Neon
        Database.connect(
            url = jdbcUrl ?: "",
            driver = "org.postgresql.Driver",
            user = dbUser ?: "",
            password = dbPassword ?: ""
        )

        // Inicializamos y creamos las tablas en la base de datos
        transaction {
            // El orden es importante por las llaves foráneas (FK)
            // Primero tablas maestras, luego tablas dependientes
            SchemaUtils.create(
                Personal,
                Patients,
                MedicalRecords
            )

            // Si necesitas agregar futuras tablas, simplemente agrégalas aquí:
            // SchemaUtils.create(Salas, Insumos, Citas)
        }

        println("¡Conexión a Neon exitosa y base de datos sincronizada!")
    }
}
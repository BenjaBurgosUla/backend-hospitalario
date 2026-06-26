package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable

// Ahora apunta a tu nueva tabla "personal"
object Personal : IntIdTable("personal") {
    val name = varchar("name", 100)
    val email = varchar("email", 100).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val role = varchar("role", 50).default("NURSE")
}

// Representa a un trabajador dentro de tu código
data class PersonalData(
    val id: Int,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String
)
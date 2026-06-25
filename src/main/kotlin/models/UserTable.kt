package com.example.models
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("users") {
    val name = varchar("name", 100)
    val email = varchar("email", 100).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val role = varchar("role", 50).default("NURSE")
}

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String
)


package com.example.models


import org.jetbrains.exposed.dao.id.IntIdTable

object Patients : IntIdTable("patients") {
    val rut = varchar("rut", 20).uniqueIndex()
    val name = varchar("name", 100)
    val email = varchar("email", 100).nullable()
}

data class Patient(
    val id: Int,
    val rut: String,
    val name: String,
    val email: String?
)
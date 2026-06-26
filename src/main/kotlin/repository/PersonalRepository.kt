package com.example.repository

// Importamos los nuevos nombres desde tu archivo PersonalTable.kt
import com.example.models.PersonalData
import com.example.models.Personal

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PersonalRepository {

    // --- NUEVA FUNCIÓN PARA EL ADMIN ---
    fun getAllUsers(): List<PersonalData> {
        return transaction {
            // selectAll() trae toda la tabla automáticamente
            Personal.selectAll()
                .map { row ->
                    PersonalData(
                        id = row[Personal.id].value,
                        name = row[Personal.name],
                        email = row[Personal.email],
                        passwordHash = row[Personal.passwordHash],
                        role = row[Personal.role]
                    )
                }
        }
    }

    fun getUserById(userId: Int): PersonalData? {
        return transaction {
            Personal.select { Personal.id eq userId }
                .map { row ->
                    PersonalData(
                        id = row[Personal.id].value,
                        name = row[Personal.name],
                        email = row[Personal.email],
                        passwordHash = row[Personal.passwordHash],
                        role = row[Personal.role]
                    )
                }
                .singleOrNull()
        }
    }

    fun getUserByEmail(userEmail: String): PersonalData? {
        return transaction {
            Personal.select { Personal.email eq userEmail }
                .map { row ->
                    PersonalData(
                        id = row[Personal.id].value,
                        name = row[Personal.name],
                        email = row[Personal.email],
                        passwordHash = row[Personal.passwordHash],
                        role = row[Personal.role]
                    )
                }
                .singleOrNull()
        }
    }

    fun createUser(name: String, email: String, passwordHash: String): Int {
        return transaction {
            Personal.insertAndGetId {
                it[Personal.name] = name
                it[Personal.email] = email
                it[Personal.passwordHash] = passwordHash
            }.value
        }
    }

    fun updateUser(userId: Int, newName: String, newRole: String): Boolean {
        return transaction {
            val updatedRows = Personal.update({ Personal.id eq userId }) {
                it[name] = newName
                it[role] = newRole
            }
            // Retorna true si logró actualizar al menos 1 fila
            updatedRows > 0
        }
    }

    fun deleteUser(userId: Int): Boolean {
        return transaction {
            val deletedRows = Personal.deleteWhere { Personal.id eq userId }
            // Retorna true si logró eliminar al menos 1 fila
            deletedRows > 0
        }
    }
}
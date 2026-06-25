package com.example.repository

import com.example.models.User
import com.example.models.Users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepository {

    // --- NUEVA FUNCIÓN PARA EL ADMIN ---
    fun getAllUsers(): List<User> {
        return transaction {
            // selectAll() trae toda la tabla automáticamente
            Users.selectAll()
                .map { row ->
                    User(
                        id = row[Users.id].value,
                        name = row[Users.name],
                        email = row[Users.email],
                        passwordHash = row[Users.passwordHash],
                        role = row[Users.role]
                    )
                }
        }
    }

    fun getUserById(userId: Int): User? {
        return transaction {
            Users.select { Users.id eq userId }
                .map { row ->
                    User(
                        id = row[Users.id].value,
                        name = row[Users.name],
                        email = row[Users.email],
                        passwordHash = row[Users.passwordHash],
                        role = row[Users.role]
                    )
                }
                .singleOrNull()
        }
    }

    fun getUserByEmail(userEmail: String): User? {
        return transaction {
            Users.select { Users.email eq userEmail }
                .map { row ->
                    User(
                        id = row[Users.id].value,
                        name = row[Users.name],
                        email = row[Users.email],
                        passwordHash = row[Users.passwordHash],
                        role = row[Users.role]
                    )
                }
                .singleOrNull()
        }
    }

    fun createUser(name: String, email: String, passwordHash: String): Int {
        return transaction {
            Users.insertAndGetId {
                it[Users.name] = name
                it[Users.email] = email
                it[Users.passwordHash] = passwordHash
            }.value
        }
    }

    fun updateUser(userId: Int, newName: String, newRole: String): Boolean {
        return transaction {
            val updatedRows = Users.update({ Users.id eq userId }) {
                it[name] = newName
                it[role] = newRole
            }
            // Retorna true si logró actualizar al menos 1 fila
            updatedRows > 0
        }
    }

    fun deleteUser(userId: Int): Boolean {
        return transaction {
            val deletedRows = Users.deleteWhere { Users.id eq userId }
            // Retorna true si logró eliminar al menos 1 fila
            deletedRows > 0
        }
    }
}
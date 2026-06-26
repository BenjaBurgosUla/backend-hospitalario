package com.example.services

import com.example.dtos.TokenRequest
import com.example.dtos.TokenResponse
// Nota: Mantenemos los DTOs con nombre "User" por compatibilidad directa con tu app Android.
// Si los renombraste en PersonalDTOs.kt, solo ajusta estas importaciones.
import com.example.dtos.UserCreateRequest
import com.example.dtos.UserResponse
import com.example.dtos.UserUpdateRequest
import com.example.repository.PersonalRepository
import io.github.cdimascio.dotenv.dotenv
import org.mindrot.jbcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class PersonalService(private val repository: PersonalRepository) {

    fun getAllUsers(): List<UserResponse> {
        val personalList = repository.getAllUsers()

        return personalList.map { personal ->
            UserResponse(personal.id, personal.name, personal.email, personal.role)
        }
    }

    fun getUserById(id: Int): UserResponse? {
        val personal = repository.getUserById(id) ?: return null
        return UserResponse(personal.id, personal.name, personal.email, personal.role)
    }

    fun registerUser(request: UserCreateRequest): UserResponse? {
        val hashedPassword = BCrypt.hashpw(request.passwordRaw, BCrypt.gensalt())
        val newId = repository.createUser(request.name, request.email, hashedPassword)
        return getUserById(newId)
    }

    fun loginUser(request: TokenRequest): TokenResponse? {
        val personal = repository.getUserByEmail(request.email) ?: return null

        if (BCrypt.checkpw(request.passwordRaw, personal.passwordHash)) {
            val env = dotenv {
                ignoreIfMissing = true
            }
            val secretKey = env["JWT_SECRET"] ?: "clave_alternativa_por_si_falla"

            val token = JWT.create()
                .withAudience("hospital_audience")
                .withIssuer("hospital_issuer")
                .withClaim("email", personal.email)
                .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
                .sign(Algorithm.HMAC256(secretKey))

            return TokenResponse(token, personal.id)
        }
        return null
    }

    fun updateUser(id: Int, request: UserUpdateRequest): UserResponse? {
        val success = repository.updateUser(id, request.name, request.role)
        if (success) {
            return getUserById(id)
        }
        return null
    }

    fun deleteUser(id: Int): Boolean {
        return repository.deleteUser(id)
    }
}
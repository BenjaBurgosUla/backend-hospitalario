package com.example.services

import com.example.dtos.TokenRequest
import com.example.dtos.TokenResponse
import com.example.dtos.UserCreateRequest
import com.example.dtos.UserResponse
import com.example.dtos.UserUpdateRequest
import com.example.repository.UserRepository
import io.github.cdimascio.dotenv.dotenv
import org.mindrot.jbcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class UserService(private val repository: UserRepository) {

    // --- NUEVA FUNCIÓN PARA EL ADMIN ---
    fun getAllUsers(): List<UserResponse> {
        // Pedimos todos los usuarios a la base de datos
        val users = repository.getAllUsers()

        // Los mapeamos (convertimos) uno por uno a nuestra caja de respuesta segura
        return users.map { user ->
            UserResponse(user.id, user.name, user.email, user.role)
        }
    }

    fun getUserById(id: Int): UserResponse? {
        val user = repository.getUserById(id) ?: return null
        return UserResponse(user.id, user.name, user.email, user.role)
    }

    fun registerUser(request: UserCreateRequest): UserResponse? {
        val hashedPassword = BCrypt.hashpw(request.passwordRaw, BCrypt.gensalt())
        val newUserId = repository.createUser(request.name, request.email, hashedPassword)
        return getUserById(newUserId)
    }

    fun loginUser(request: TokenRequest): TokenResponse? {
        val user = repository.getUserByEmail(request.email) ?: return null

        if (BCrypt.checkpw(request.passwordRaw, user.passwordHash)) {

            val env = dotenv()
            val secretKey = env["JWT_SECRET"] ?: "clave_alternativa_por_si_falla"

            val token = JWT.create()
                .withAudience("hospital_audience")
                .withIssuer("hospital_issuer")
                .withClaim("email", user.email)
                .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
                .sign(Algorithm.HMAC256(secretKey))

            return TokenResponse(token, user.id)
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
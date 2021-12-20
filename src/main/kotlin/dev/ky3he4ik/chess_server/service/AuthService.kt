package dev.ky3he4ik.chess_server.service

import dev.ky3he4ik.chess_server.controllers.AuthController
import dev.ky3he4ik.chess_server.db.dao.UserCredentialsDAO
import dev.ky3he4ik.chess_server.db.dao.UserInfoDAO
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*

@Service
class AuthService(
    val userCredentialsDAO: UserCredentialsDAO,
    val userInfoDAO: UserInfoDAO,
) {
    private var passwordEncoder = BCryptPasswordEncoder()

    fun login(login: String, password: String): String? {
        try {
            val userCredentials = userCredentialsDAO.findByLoginLike(login) ?: return null
            if (!passwordEncoder.matches(password, userCredentials.password))
                return null
            userCredentials.token = generateToken()
            userCredentialsDAO.save(userCredentials)
            return userCredentials.token
        } catch (e: BadCredentialsException) {
            LoggerFactory.getLogger(AuthController::class.java).warn("Incorrect username or password")
            return null
        }
    }

    fun validateToken(login: String, token: String): Boolean {
        return userCredentialsDAO.findByLoginLike(login)?.token?.equals(token) ?: false
    }

    fun generateToken(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }
}
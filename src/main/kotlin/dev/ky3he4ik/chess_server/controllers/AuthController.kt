package dev.ky3he4ik.chess_server.controllers

import dev.ky3he4ik.chess_server.db.dao.UserCredentialsDAO
import dev.ky3he4ik.chess_server.db.dao.UserInfoDAO
import dev.ky3he4ik.chess_server.models.users.UserCredentials
import dev.ky3he4ik.chess_server.models.users.UserInfo
import dev.ky3he4ik.chess_server.service.AuthService
import dev.ky3he4ik.chess_server.util.Util
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("auth")
class AuthController(
    val userCredentialsDAO: UserCredentialsDAO,
    val userInfoDAO: UserInfoDAO,
    val authService: AuthService,
) {
    private var passwordEncoder = BCryptPasswordEncoder()

    @PostMapping("login")
    @Throws(Exception::class)
    fun login(@RequestBody body: UserCredentials): ResponseEntity<String> {
        if (body.login == null || body.password == null)
            return Util.badRequest()
        val token = authService.login(body.login!!, body.password!!) ?: return Util.badRequest(401)
        return ResponseEntity.ok(token)
    }

    @PostMapping("register")
    @Throws(Exception::class)
    fun register(@RequestBody body: UserCredentials): ResponseEntity<UserCredentials> {
        if (body.login == null || body.password == null || body.user == null)
            return Util.badRequest()

        if (userCredentialsDAO.existsByLoginLike(body.login))
            return Util.badRequest(409)

        val userInfo: UserInfo = userInfoDAO.save(body.user!!)
        val userCredentials = userCredentialsDAO.save(
            UserCredentials(
                null,
                body.login,
                passwordEncoder.encode(body.password),
                authService.generateToken(),
                body.role,
                userInfo
            )
        )
        return ResponseEntity.ok(userCredentials)
    }

    @GetMapping("info/{id}")
    fun getUserById(
        @PathVariable id: UUID,
        @RequestHeader("Login") login: String,
        @RequestHeader("Token") token: String
    ): ResponseEntity<UserCredentials?> {
        return ResponseEntity.ok(userCredentialsDAO.getById(id))
    }

    @GetMapping("info")
    fun getAllUsers(
        @RequestHeader("Login") login: String,
        @RequestHeader("Token") token: String
    ): ResponseEntity<List<UserCredentials>> {
        return ResponseEntity.ok(userCredentialsDAO.findAll().filterNotNull())
    }
}

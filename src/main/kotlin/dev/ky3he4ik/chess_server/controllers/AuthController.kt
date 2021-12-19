package dev.ky3he4ik.chess_server.controllers

import dev.ky3he4ik.chess_server.db.dao.UserCredentialsDAO
import dev.ky3he4ik.chess_server.db.dao.UserInfoDAO
import dev.ky3he4ik.chess_server.models.users.UserCredentials
import dev.ky3he4ik.chess_server.models.users.UserInfo
import dev.ky3he4ik.chess_server.security.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("auth")
class AuthController {
    private var passwordEncoder = BCryptPasswordEncoder()

    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @Autowired
    private lateinit var userCredentialsDAO: UserCredentialsDAO

    @Autowired
    private lateinit var userInfoDAO: UserInfoDAO

    @PostMapping("login")
    @Throws(Exception::class)
    fun login(@RequestBody body: UserCredentials): ResponseEntity<String> {
        if (body.login == null || body.password == null)
            return ResponseEntity.badRequest().body("User already exists")

        val token = login(body.login!!, body.password!!) ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(token)
    }

    @PostMapping("register")
    @Throws(Exception::class)
    fun register(@RequestBody body: UserCredentials): ResponseEntity<UserInfo?> {
        if (body.login == null || body.password == null)
            return ResponseEntity.badRequest().build()

        if (userCredentialsDAO.existsByLoginLike(body.login) == false) {
            val user: UserInfo = userInfoDAO.save(body.user)
            val userInfo = userCredentialsDAO.save(
                UserCredentials(
                    null,
                    body.login,
                    passwordEncoder.encode(body.password),
                    body.role,
                    user
                )
            )
            return ResponseEntity.ok(userInfo.user)
        } else
            return ResponseEntity.badRequest().build()
    }

    @GetMapping("info")
    fun authInfo(authentication: Authentication): ResponseEntity<UserInfo?> {
        if (!authentication.isAuthenticated)
            return ResponseEntity.status(403).body(null)
        val user = userCredentialsDAO.findByLoginLike(authentication.name)
        return ResponseEntity.ok(user?.user)
    }

    @GetMapping("auth/oauth")
    fun outAuth(@RequestParam provider: OAUTH2Provider?): ResponseEntity<OutAuthResponse> {
        return ResponseEntity.ok(OutAuthResponse("path", UUID.randomUUID()))
    }

    class OutAuthResponse(var path: String, var session_id: UUID)
    enum class OAUTH2Provider {
        VK, Google, Meta
    }

    /**
     * Log in and get jwt token
     * Return null on errors
     */
    private fun login(login: String, password: String): String? {
        return try {
//            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(login, password))
//            jwtUtils.generateToken(userDetailsService.loadUserByUsername(login) ?: return null)
            null
        } catch (e: BadCredentialsException) {
            LoggerFactory.getLogger(AuthController::class.java).warn("Incorrect username or password")
            null
        }
    }
}

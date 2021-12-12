package dev.ky3he4ik.chess_server.security.services

import dev.ky3he4ik.chess_server.models.users.UserCredentials
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function

@Service
class JWTUtils {
    private val SECRET_KEY = "secret"
    fun extractUsername(token: String?): String {
        return extractClaim(token, Function<Claims, String> { obj: Claims -> obj.getSubject() })
    }

    fun extractExpiration(token: String?): Date {
        return extractClaim(token, Function<Claims, Date> { obj: Claims -> obj.getExpiration() })
    }

    fun extractRole(token: String?): UserCredentials.Role {
        return UserCredentials.Role.valueOf(extractAllClaims(token).get("role", String::class.java))
    }

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody()
    }

    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: MutableMap<String, Any> = HashMap<String, Any>()
        claims.put("role", userDetails.authorities.iterator().next().authority)
        return createToken(claims, userDetails.username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails?): Boolean {
        userDetails ?: return false
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }
}
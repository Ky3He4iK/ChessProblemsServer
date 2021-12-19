package dev.ky3he4ik.chess_server.service

import boring.owl.parapp.entities.exceptions.TokenRefreshException
import boring.owl.parapp.entities.RefreshToken
import boring.owl.parapp.repository.UserRepo
import boring.owl.parapp.repository.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class RefreshTokenService(
    private val userRepo: UserRepo,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    @Value("\${jwt.refreshexpiration}")
    private val refreshTokenDurationMs: Long = 2592000

    fun findByToken(token: String?): RefreshToken? {
        return refreshTokenRepository.findFirstByTokenLike(token)
    }

    fun createRefreshToken(username: String): RefreshToken {
        var refreshToken = RefreshToken()
        refreshToken.user = userRepo.findUserEntityByLogin(username)
        refreshToken.expiryDate = Instant.now().plusMillis(refreshTokenDurationMs)
        refreshToken.token = UUID.randomUUID().toString()
        refreshToken = refreshTokenRepository.save(refreshToken)
        return refreshToken
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken {
        if (token.expiryDate < Instant.now()) {
            refreshTokenRepository.delete(token)
            throw TokenRefreshException(
                token.token,
                "Refresh token was expired. Please make a new signin request"
            )
        }
        return token
    }

    @Transactional
    fun deleteByUserId(userId: UUID): Int {
        return refreshTokenRepository.deleteByUser(userRepo.findById(userId).get())
    }
}
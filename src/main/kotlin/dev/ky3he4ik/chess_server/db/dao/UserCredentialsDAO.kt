package dev.ky3he4ik.chess_server.db.dao

import dev.ky3he4ik.chess_server.models.users.UserCredentials
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserCredentialsDAO : JpaRepository<UserCredentials?, UUID?> {
    fun findByUserIdLike(id: UUID?): UserCredentials?
    fun findByLoginLike(login: String?): UserCredentials?
    fun existsByLoginLike(login: String?): Boolean?
}

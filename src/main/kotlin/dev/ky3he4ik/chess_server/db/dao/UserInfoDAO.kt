package dev.ky3he4ik.chess_server.db.dao

import dev.ky3he4ik.chess_server.models.problems.ProblemInfo
import dev.ky3he4ik.chess_server.models.users.UserInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserInfoDAO : JpaRepository<UserInfo?, UUID?> {
    fun findByIdLike(id: UUID?): UserInfo?
}

package dev.ky3he4ik.chess_server.db.dao

import dev.ky3he4ik.chess_server.models.users.UserInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserInfoDAO : JpaRepository<UserInfo?, UUID?> {
    fun findByUserIdLike(id: UUID?): UserInfo?

    @Query("select u from UserInfo u where u.userId = (select c.userId from UserCredentials c where c.login = :userName)")
    fun findByUserName(@Param("userName") userName: String?): UserInfo?
}

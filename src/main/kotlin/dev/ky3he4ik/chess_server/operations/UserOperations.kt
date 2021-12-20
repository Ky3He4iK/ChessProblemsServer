package dev.ky3he4ik.chess_server.operations

import dev.ky3he4ik.chess_server.models.users.UserCredentials

object UserOperations {
    fun canAddProblems(userCredentials: UserCredentials): Boolean =
        listOf(UserCredentials.Role.PREMIUM, UserCredentials.Role.MODERATOR, UserCredentials.Role.ADMIN).contains(
            userCredentials.role
        )

    fun canDeleteProblems(userCredentials: UserCredentials): Boolean =
        listOf(UserCredentials.Role.MODERATOR, UserCredentials.Role.ADMIN).contains(userCredentials.role)

    fun canDeleteUsers(userCredentials: UserCredentials): Boolean = UserCredentials.Role.ADMIN == userCredentials.role
}
package dev.ky3he4ik.chess_server.security.services

import dev.ky3he4ik.chess_server.db.dao.UserCredentialsDAO
import dev.ky3he4ik.chess_server.models.users.UserCredentials
import org.springframework.boot.context.properties.bind.Bindable.listOf
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class MyUserDetailsService(private val userCredentialsDAO: UserCredentialsDAO) : UserDetailsService {

    override fun loadUserByUsername(userName: String): UserDetails? {
        val userCredentials = userCredentialsDAO.findByLoginLike(userName) ?: return null
        return User(
            userCredentials.login,
            userCredentials.password,
            Collections.singletonList(SimpleGrantedAuthority(userCredentials.role.toString()))
        )
    }
}
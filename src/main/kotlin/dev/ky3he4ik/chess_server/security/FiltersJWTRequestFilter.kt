package dev.ky3he4ik.chess_server.security

import dev.ky3he4ik.chess_server.models.users.UserCredentials
import dev.ky3he4ik.chess_server.security.services.JWTUtils
import dev.ky3he4ik.chess_server.security.services.MyUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class FiltersJWTRequestFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var userDetailsService: MyUserDetailsService

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    @Throws(ServletException::class, IOException::class)
    protected override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader: String? = request.getHeader("Authorization")
        var userName: String? = null
        var jwt: String? = null
        var role: UserCredentials.Role? = null
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7)
            userName = jwtUtils.extractUsername(jwt)
            role = jwtUtils.extractRole(jwt)
        }
        if (userName != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails: UserDetails? = userDetailsService.loadUserByUsername(userName)
            if (userDetails != null && role != null) {
                if (jwtUtils.validateToken(jwt, userDetails)) {
                    var usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken? = null
                    if (role === UserCredentials.Role.valueOf(userDetails.authorities.iterator().next().authority)) {
                        usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities
                        )
                    } else {
                        usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, listOf(SimpleGrantedAuthority(role.name))
                        )
                    }
                    usernamePasswordAuthenticationToken.setDetails(WebAuthenticationDetailsSource().buildDetails(request))
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken)
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}
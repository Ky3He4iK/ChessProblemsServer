package dev.ky3he4ik.chess_server.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController {
    @GetMapping("/")
    fun greetingInIndex(authentication: Authentication): ResponseEntity<String?>? {
        return ResponseEntity.ok(
            String.format(
                "Greetings to %s from Chess Server!",
                authentication.authorities.iterator().next().authority
            )
        )
    }
}

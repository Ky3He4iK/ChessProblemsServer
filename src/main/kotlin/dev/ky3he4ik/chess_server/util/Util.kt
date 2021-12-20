package dev.ky3he4ik.chess_server.util

import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException

object Util {
    fun <T> badRequest(status: Int = 400): ResponseEntity<T> {
        return ResponseEntity.status(status).build()
    }
}
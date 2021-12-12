package dev.ky3he4ik.chess_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class ChessServerApplication

fun main(args: Array<String>) {
    runApplication<ChessServerApplication>(*args)
}

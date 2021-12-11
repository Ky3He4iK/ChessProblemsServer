package dev.ky3he4ik.chess_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChessServerApplication

fun main(args: Array<String>) {
    runApplication<ChessServerApplication>(*args)
}

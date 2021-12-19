package dev.ky3he4ik.chess_server.network.chessblunders

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.serialization.responseObject
import dev.ky3he4ik.chess_server.models.problems.ProblemInfo

class ChessBlunders {
    fun getRandomProblem(): ProblemInfo? {
        val (request, response, result) = "https://chessblunders.org/api/blunder/get"
            .httpPost()
            .body("""{ "type": "explore" }""")
            .responseObject<ChessBlunderResponse>()
        return result.get().data.toProblemInfo()
    }
}

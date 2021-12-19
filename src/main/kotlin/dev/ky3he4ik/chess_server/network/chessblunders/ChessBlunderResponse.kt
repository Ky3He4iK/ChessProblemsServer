package dev.ky3he4ik.chess_server.network.chessblunders

import dev.ky3he4ik.chess_server.models.problems.ProblemInfo
import dev.ky3he4ik.chess_server.operations.ProblemOperations
import kotlinx.serialization.Serializable

@Serializable
data class ChessBlunderResponse(val data: ChessBlunderData, val status: String) {
    @Serializable
    data class ChessBlunderData(
        val blunderMove: String,
        val elo: Int,
        val fenBefore: String,
        val forcedLine: List<String>,
        val game_id: String,
        val id: String,
        val move_index: Int,
    ) {
        fun toProblemInfo(): ProblemInfo? {
            val problem = ProblemOperations.fromFenWithMoves(
                fenBefore,
                forcedLine,
                ProblemInfo(
                    null, null, "Blunder #${id}", elo,
                    true, listOf(), listOf()
                )
            )
            if (problem == null)
                println("Error: ChessBlunder/parse $fenBefore | ${forcedLine.joinToString()}")
            return problem
        }
    }
}

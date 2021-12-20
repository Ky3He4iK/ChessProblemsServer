package dev.ky3he4ik.chess_server.controllers

import dev.ky3he4ik.chess_server.db.dao.ProblemInfoDAO
import dev.ky3he4ik.chess_server.db.dao.UserCredentialsDAO
import dev.ky3he4ik.chess_server.models.problems.ProblemInfo
import dev.ky3he4ik.chess_server.network.chessblunders.ChessBlunders
import dev.ky3he4ik.chess_server.operations.UserOperations
import dev.ky3he4ik.chess_server.service.AuthService
import dev.ky3he4ik.chess_server.util.Util
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("problems")
class ProblemsController(
    val userCredentialsDAO: UserCredentialsDAO,
    val problemInfoDAO: ProblemInfoDAO,
    val authService: AuthService,
) {
    @GetMapping("random")
    fun getRandomProblem(
        @RequestHeader("Login") login: String,
        @RequestHeader("Token") token: String
    ): ResponseEntity<ProblemInfo> {
        val problem = gatherRandomProblem() ?: return ResponseEntity.internalServerError().build()
        return ResponseEntity.ok(problem)
    }

    @GetMapping("unsolved")
    fun getUnsolvedProblem(
        @RequestHeader("Login") login: String,
        @RequestHeader("Token") token: String
    ): ResponseEntity<ProblemInfo> {
        if (!authService.validateToken(login, token))
            return Util.badRequest(401)
        val uid = userCredentialsDAO.findByLoginLike(login)?.userId ?: return Util.badRequest(418)
        val problemIds = problemInfoDAO.getRandomINotSolvedIds(uid)
        if (problemIds.isEmpty())
            return getRandomProblem(login, token)
        return ResponseEntity.ok(problemInfoDAO.getById(problemIds[0]))
    }

    @PostMapping("add")
    fun addProblem(
        @RequestHeader("Login") login: String,
        @RequestHeader("Token") token: String,
        @RequestBody body: ProblemInfo
    ): ResponseEntity<ProblemInfo> {
        if (!authService.validateToken(login, token))
            return Util.badRequest(401)
        val userCredentials = userCredentialsDAO.findByLoginLike(login) ?: return Util.badRequest(418)
        if (UserOperations.canAddProblems(userCredentials)) {
            body.authorId = userCredentials.userId
            return ResponseEntity.ok(problemInfoDAO.save(body))
        }
        return ResponseEntity.status(403).build()
    }

    @GetMapping("remove/{id}")
    fun removeProblem(
        @PathVariable id: UUID,
        @RequestHeader("Login") login: String,
        @RequestHeader("Token") token: String,
    ): ResponseEntity<Boolean> {
        if (!authService.validateToken(login, token))
            return Util.badRequest(401)
        val userCredentials = userCredentialsDAO.findByLoginLike(login) ?: return Util.badRequest(418)
        if (UserOperations.canDeleteProblems(userCredentials)) {
            problemInfoDAO.deleteById(id)
            return ResponseEntity.ok(true)
        }
        return ResponseEntity.status(403).build()
    }

    fun gatherRandomProblem(): ProblemInfo? {
        return problemInfoDAO.save(ChessBlunders.getRandomProblem() ?: return null)
    }
}
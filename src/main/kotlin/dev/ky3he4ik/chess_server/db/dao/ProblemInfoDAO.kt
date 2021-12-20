package dev.ky3he4ik.chess_server.db.dao

import dev.ky3he4ik.chess_server.models.problems.ProblemInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProblemInfoDAO : JpaRepository<ProblemInfo?, UUID?> {
    fun findByProblemIdLike(problemId: UUID?): ProblemInfo?

    @Query(
        """
        select problemId from problemInfo where not exists(
            select solved_problem.problemId from solved_problem 
                where solved_problem.userId = :user_id and solved_problem.problemId = solved_problem.problemId
    ) order by random() 
        limit :count
    """
    )
    fun getRandomINotSolvedIds(@Param("user_id") userId: UUID, @Param("count") count: Int = 1): List<UUID>
}

package dev.ky3he4ik.chess_server.models.users

import dev.ky3he4ik.chess_server.models.problems.ProblemInfo
import dev.ky3he4ik.chess_server.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "solved_problem")
@Serializable
class SolvedProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long? = null
//    @Column(name = "problem_id", nullable = false)
//    @ForeignKey(value = ConstraintMode.CONSTRAINT)
//    @Id
//    @ManyToOne(
//        cascade = [CascadeType.ALL],
//        fetch = FetchType.EAGER,
//        targetEntity = ProblemInfo::class,
//        optional = false
//    )
//    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(cascade = [CascadeType.DETACH], optional = false)
    @JoinColumn(name = "problemId", referencedColumnName = "problemId", nullable = false)
    var problemInfo: ProblemInfo = ProblemInfo()

//    @Column(name = "userId", nullable = false)
//    @ManyToOne(
//        cascade = [CascadeType.ALL],
//        fetch = FetchType.EAGER,
//        targetEntity = ProblemInfo::class,
//        optional = false
//    )

    @Column(name = "userId", nullable = false)
    @Serializable(with = UUIDSerializer::class)
    var userId: UUID? = null

    @Column(name = "solving_time")
    var solvingTime: Long = 0
}
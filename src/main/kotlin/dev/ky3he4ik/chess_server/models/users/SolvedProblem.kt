package dev.ky3he4ik.chess_server.models.users

import dev.ky3he4ik.chess_server.models.problems.ProblemInfo
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import javax.persistence.*

@Entity
@Table(name = "solved_problem")
@Serializable
class SolvedProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Int? = null
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
    @ManyToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = "problem_id", referencedColumnName = "problem_id", nullable = false)
    var problemInfo: ProblemInfo = ProblemInfo()

//    @Column(name = "user_id", nullable = false)
//    @ManyToOne(
//        cascade = [CascadeType.ALL],
//        fetch = FetchType.EAGER,
//        targetEntity = ProblemInfo::class,
//        optional = false
//    )
    @ManyToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    val userInfo: UserInfo = UserInfo()

    @Column(name = "solving_time")
    var solvingTime: Long = 0
}
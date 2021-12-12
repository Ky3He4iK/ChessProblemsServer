package dev.ky3he4ik.chess_server.models.users

import dev.ky3he4ik.chess_server.models.problems.ProblemMove
import dev.ky3he4ik.chess_server.util.UUIDSerializer
import kotlinx.serialization.Serializable
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "problem_info")
@Serializable
class UserInfo {
    @Id
    @GeneratedValue
    @Column(name = "user_id", nullable = false)
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID? = null

    @Column
    var nick: String = ""

    @Column
    var image: String? = null

    @Column
    var rating: Int = 0

    @Column
    var solved: Int = 0

    @Column
    var mail: String = ""

    @OneToMany(mappedBy = "user_id",  cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var solvedProblems: List<SolvedProblem> = ArrayList()
}

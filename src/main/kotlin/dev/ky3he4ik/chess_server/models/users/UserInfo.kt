package dev.ky3he4ik.chess_server.models.users

import dev.ky3he4ik.chess_server.models.problems.ProblemMove
import dev.ky3he4ik.chess_server.util.UUIDSerializer
import kotlinx.serialization.Serializable
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_info")
@Serializable
class UserInfo {
    @Id
    @GeneratedValue
    @Column(name = "userId", nullable = false)
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

    @OneToMany(mappedBy = "userId",  cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var solvedProblems: List<SolvedProblem> = ArrayList()
}

package dev.ky3he4ik.chess_server.models.problems

import dev.ky3he4ik.chess_server.util.UUIDSerializer
import kotlinx.serialization.Serializable
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "problem_info")
@Serializable
class ProblemInfo {
    @Id
    @GeneratedValue
    @Column(name = "problem_id", nullable = false)
    @Serializable(with = UUIDSerializer::class)
    var problemId: UUID? = null

    @Column
    var title: String = ""

    @Column
    var description: String = ""

    @Column
    var difficulty: Int = 0

    @Column(name = "white_starts")
    var whiteStarts: Boolean = false

    @OneToMany(mappedBy = "problem_id",  cascade = [CascadeType.ALL], fetch = FetchType.EAGER, targetEntity = ProblemMove::class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var moves: List<ProblemMove> = ArrayList()

    @OneToMany(mappedBy = "problem_id",  cascade = [CascadeType.ALL], fetch = FetchType.EAGER, targetEntity = FigurePosition::class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var figurePosition: List<FigurePosition> = ArrayList()
}

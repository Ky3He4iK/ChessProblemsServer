package dev.ky3he4ik.chess_server.models.problems

import dev.ky3he4ik.chess_server.util.UUIDSerializer
import kotlinx.serialization.Serializable
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "problemInfo")
@Serializable
class ProblemInfo (
    @Id
    @GeneratedValue
    @Column(name = "problemId", nullable = false)
    @Serializable(with = UUIDSerializer::class)
    var problemId: UUID? = null,

    @Column(nullable = true)
    @Serializable(with = UUIDSerializer::class)
    var authorId: UUID? = null,

    @Column
    var title: String = "",

    @Column
    var difficulty: Int = 0,

    @Column(name = "whiteStarts")
    var whiteStarts: Boolean = false,

    @OneToMany(mappedBy = "problemId",  cascade = [CascadeType.ALL], targetEntity = ProblemMove::class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var moves: List<ProblemMove> = ArrayList(),

    @OneToMany(mappedBy = "problemId",  cascade = [CascadeType.ALL], targetEntity = FigurePosition::class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var figurePosition: List<FigurePosition> = ArrayList(),
)

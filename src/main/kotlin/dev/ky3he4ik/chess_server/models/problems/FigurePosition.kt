package dev.ky3he4ik.chess_server.models.problems

import dev.ky3he4ik.chess_server.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "figurePosition")
@Serializable
class FigurePosition(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(name = "problemId", nullable = false)
    @Serializable(with = UUIDSerializer::class)
    var problemId: UUID? = null,

    @Column
    var letter: Char = '0',

    @Column
    var number: Int = 0,

    @Column
    var figure: Char? = null,

    @Column
    var isWhite: Boolean = false,
)

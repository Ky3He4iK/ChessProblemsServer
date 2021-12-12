package dev.ky3he4ik.chess_server.models.problems

import dev.ky3he4ik.chess_server.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "problemMove")
@Serializable
class ProblemMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Int? = null

    @Column(name = "problemId", nullable = false)
    @Serializable(with = UUIDSerializer::class)
    var problemId: UUID? = null

    @Column(name = "letterStart")
    var letterStart: Int = 0

    @Column(name = "numberStart")
    var numberStart: Int = 0

    @Column(name = "letterEnd")
    var letterEnd: Int = 0

    @Column(name = "numberEnd")
    var numberEnd: Int = 0

    @Column
    var promotion: Char? = null

    @Column(name = "isCastling")
    var isCastling: Boolean = false

    @Column
    var move: String = ""
}
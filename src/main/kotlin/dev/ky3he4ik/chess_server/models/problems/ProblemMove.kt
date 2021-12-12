package dev.ky3he4ik.chess_server.models.problems

import kotlinx.serialization.Serializable
import javax.persistence.*

@Entity
@Table(name = "problem_move")
@Serializable
class ProblemMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Int? = null

    @Column(name = "problem_id")
    var problem_id: Int = 0

    @Column(name = "letter_start")
    var letterStart: Int = 0

    @Column(name = "number_start")
    var numberStart: Int = 0

    @Column(name = "letter_end")
    var letterEnd: Int = 0

    @Column(name = "number_end")
    var numberEnd: Int = 0

    @Column
    var promotion: Char? = null

    @Column(name = "is_castling")
    var isCastling: Boolean = false

    @Column
    var move: String = ""
}
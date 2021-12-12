package dev.ky3he4ik.chess_server.models.problems

import kotlinx.serialization.Serializable
import javax.persistence.*

@Entity
@Table(name = "figure_position")
@Serializable
class FigurePosition {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: Int? = null

    @Column(name = "problem_id")
    var problemId: Int = 0

    @Column
    var letter: Char = '0'

    @Column
    var number: Int = 0

    @Column
    var figure: Char? = null

    @Column
    var isWhite: Boolean = false
}
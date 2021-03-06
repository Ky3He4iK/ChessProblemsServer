package dev.ky3he4ik.chess_server.operations

import dev.ky3he4ik.chess_server.models.problems.FigurePosition
import dev.ky3he4ik.chess_server.models.problems.ProblemInfo
import dev.ky3he4ik.chess_server.models.problems.ProblemMove
import java.util.regex.Pattern


object ProblemOperations {
    private val letters = Array(8) { Char('a'.code + it) }

    // Парсинг одного хода
    // Не трогать и не привлекать к сатанинским ритуалам
    private const val movesRegex =
        "(((?<figure>[PNBRQK]?)((?<letstart>[a-h]?)(?<numstart>[1-8]?)[\\-x]?)(?<letend>[a-h])(?<numend>[1-8])" +
                "(?:\\=(?<promotion>[nbrqkNBRQK]?))?)|(?<castling>(0-0(-0)?)|(O-O(-O)?)))[+#?!]*"
    private val regexG = Pattern.compile(movesRegex)

    private fun fromFen(
        fenString: String
    ): Pair<Boolean, List<FigurePosition>>? {
        val fenInfo = fenString.split(' ')
        if (fenInfo.size != 6)
            return null

        val linedPositions = fenInfo[0].split('/')

        if (linedPositions.size != 8)
            return null
        val positions = ArrayList<FigurePosition>()
        for (i in 0 until 8) {
            val line = 7 - i
            var letterNum = 0
            for (pos in linedPositions[i]) {
                if ('0'.code <= pos.code && pos.code <= '9'.code)
                    letterNum += pos.digitToInt()
                else {
                    val isWhite = pos.code < 'a'.code
                    var position: Char? = pos.uppercase()[0]
                    if (position == 'P')
                        position = null
                    val letter = Char('a'.code + letterNum)
                    positions.add(FigurePosition(null, null, letter, line, position, isWhite))
                    letterNum++
                }
            }
        }
        val whiteStarts = fenInfo[1] != "w"
        // todo: castling for white and black (fenInfo[2])
        // todo: castling for pawn moves (fenInfo[3])
        // todo: 50 moves rule (fenInfo[4])
        // todo: move number (fenInfo[5])

        return Pair(whiteStarts, positions)
    }

    fun fromFenWithMoves(
        fenString: String,
        moves: List<String>,
        baseProblem: ProblemInfo
    ): ProblemInfo? {
        val (whiteStarts, positions) = fromFen(fenString) ?: return null
        val problemMoves = parseMoves(moves, whiteStarts, positions) ?: return null
        return ProblemInfo(
            baseProblem.problemId,
            baseProblem.authorId,
            baseProblem.title,
            baseProblem.difficulty,
            whiteStarts,
            problemMoves,
            positions
        )
    }

    /**
     * Parse input code in format 'Qe4' to pair like ('Q', 4, 3)
     *  Return: (figure, index_x, index_y), each in range from 0 to 7
     *      figure: letter of figure (null for pawn)
     *  Default return is (null, -1, -1)
     */
    private fun parseFigure(string: String): Triple<Char?, Int, Int> {
        if (string.length == 2)
            return Triple(null, letters.indexOf(string[0]), string[1] - '1')
        if (string.length == 3)
            return Triple(string[0], letters.indexOf(string[1]), string[2] - '1')
        return Triple(null, -1, -1)
    }

    private fun parseMoves(
        pgnMoves: List<String>,
        whiteStarts: Boolean,
        figurePositions: List<FigurePosition>
    ): ArrayList<ProblemMove>? {
        val figures: Array<Array<Char?>> = Array(8) { Array(8) { null } }
        figurePositions.forEach {
            figures[it.letter - 'a'][it.number] =
                if (it.isWhite)
                    it.figure ?: 'P'
                else
                    it.figure?.lowercaseChar() ?: 'p'
        }

        var isWhiteMove = whiteStarts

        val arr = arrayListOf<ProblemMove>()

        for (move in pgnMoves) {
            val move = move.replace('0', 'O')
            val matcher = regexG.matcher(move)
            if (!matcher.find())
                return null
            val fig = emptyToNull(matcher.group("figure"))?.get(0)
            val figure = toCasedFigure(fig ?: 'P', isWhiteMove)

            val letStart = emptyToNull(matcher.group("letstart"))
            var letterStart = letStart?.get(0)?.minus('a')
            val numStart = emptyToNull(matcher.group("numstart"))
            var numberStart = numStart?.get(0)?.minus('1')
            val letEnd = emptyToNull(matcher.group("letend"))
            var letterEnd = letEnd?.get(0)?.minus('a')
            val numEnd = emptyToNull(matcher.group("numend"))
            var numberEnd = numEnd?.get(0)?.minus('1')
            val prom = emptyToNull(matcher.group("promotion"))
            val promotion = prom?.get(0)
            val castling = emptyToNull(matcher.group("castling"))
            val isCastling = castling != null

            if (letterEnd == null || numberEnd == null)
                if (isCastling) {
                    when {
                        isWhiteMove && move == "O-O" -> {
                            letterStart = 4
                            numberStart = 0
                            letterEnd = 6
                            numberEnd = 0
                        }
                        isWhiteMove && move == "O-O-O" -> {
                            letterStart = 4
                            numberStart = 0
                            letterEnd = 2
                            numberEnd = 0
                        }
                        !isWhiteMove && move == "O-O" -> {
                            letterStart = 4
                            numberStart = 7
                            letterEnd = 6
                            numberEnd = 7
                        }
                        !isWhiteMove && move == "O-O-O" -> {
                            letterStart = 4
                            numberStart = 7
                            letterEnd = 2
                            numberEnd = 7
                        }
                        else -> return null
                    }
                } else
                    return null

            //todo: source checking
            if (letterStart == null) {
                if (numberStart == null) {
                    when (figure.uppercaseChar()) {
                        'P' -> {
                            val colored = toCasedFigure('P', isWhiteMove)
                            if (numberEnd > 1) {
                                if (figures[letterEnd][numberEnd - 1] == colored) {
                                    letterStart = letterEnd
                                    numberStart = numberEnd - 1
                                } else if (figures[letterEnd][numberEnd - 2] == colored) {
                                    letterStart = letterEnd
                                    numberStart = numberEnd - 2
                                }
                            }
                        }
                        'K' -> {
                            val colored = toCasedFigure('K', isWhiteMove)
                            for (dx in -1..1)
                                for (dy in -1..1) {
                                    val lsn = letterEnd + dx
                                    val ns = numberEnd + dy
                                    if (lsn in 0 until 8 && ns in 0 until 8 && figures[lsn][ns] == colored) {
                                        letterStart = lsn
                                        numberStart = ns
                                    }
                                }
                        }
                        'Q' -> {
                            val colored = toCasedFigure('Q', isWhiteMove)
                            for (dx in -7..7) {
                                when {
                                    letterEnd + dx in 0 until 8 && figures[letterEnd + dx][numberEnd] == colored -> {
                                        letterStart = letterEnd + dx
                                        numberStart = numberEnd
                                        break
                                    }
                                    numberEnd + dx in 0 until 8 && figures[letterEnd][numberEnd + dx] == colored -> {
                                        letterStart = letterEnd
                                        numberStart = numberEnd + dx
                                        break
                                    }
                                    letterEnd + dx in 0 until 8 && numberEnd + dx in 0 until 8 && figures[letterEnd + dx][numberEnd + dx] == colored -> {
                                        letterStart = letterEnd + dx
                                        numberStart = numberEnd + dx
                                        break
                                    }
                                    letterEnd + dx in 0 until 8 && numberEnd - dx in 0 until 8 && figures[letterEnd + dx][numberEnd - dx] == colored -> {
                                        letterStart = letterEnd + dx
                                        numberStart = numberEnd - dx
                                        break
                                    }
                                }
                            }
                        }
                        'B' -> {
                            val colored = toCasedFigure('B', isWhiteMove)
                            for (dx in -7..7) {
                                when {
                                    letterEnd + dx in 0 until 8 && numberEnd + dx in 0 until 8 && figures[letterEnd + dx][numberEnd + dx] == colored -> {
                                        letterStart = letterEnd + dx
                                        numberStart = numberEnd + dx
                                        break
                                    }
                                    letterEnd + dx in 0 until 8 && numberEnd - dx in 0 until 8 && figures[letterEnd + dx][numberEnd - dx] == colored -> {
                                        letterStart = letterEnd + dx
                                        numberStart = numberEnd - dx
                                        break
                                    }
                                }
                            }
                        }
                        'N' -> {
                            val colored = toCasedFigure('N', isWhiteMove)
                            val variants = listOf(
                                Pair(-2, 1), Pair(-2, -1), Pair(2, 1), Pair(2, -1),
                                Pair(-1, 2), Pair(-1, -2), Pair(1, 2), Pair(1, -2),
                            )
                            val found =
                                variants.any { (dx, dy) -> letterEnd + dx in 0 until 8 && numberEnd + dy in 0 until 8 && figures[letterEnd + dx][numberEnd + dy] == colored }
                            for ((dx, dy) in variants) {
                                if (letterEnd + dx in 0 until 8 && numberEnd + dy in 0 until 8 && figures[letterEnd + dx][numberEnd + dy] == colored) {
                                    letterStart = letterEnd + dx
                                    numberStart = numberEnd + dy
                                    break
                                }
                            }
                        }
                        'R' -> {
                            val colored = toCasedFigure('R', isWhiteMove)
                            for (dx in -7..7) {
                                when {
                                    letterEnd + dx in 0 until 8 && figures[letterEnd + dx][numberEnd] == colored -> {
                                        letterStart = letterEnd + dx
                                        numberStart = numberEnd
                                        break
                                    }
                                    numberEnd + dx in 0 until 8 && figures[letterEnd][numberEnd + dx] == colored -> {
                                        letterStart = letterEnd
                                        numberStart = numberEnd + dx
                                        break
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (letterF in 0 until 8)
                        if (figures[letterF][numberStart!!] == figure)
                            numberStart = letterF
                }
            } else if (numberStart == null) {
                for (numberF in 0 until 8)
                    if (figures[letterStart][numberF] == figure)
                        numberStart = numberF
            }

            if (numberStart == null || letterStart == null) {
                return null
            }

            if (!isCastling) {
                if (promotion != null)
                    figures[letterEnd][numberEnd] = toCasedFigure(promotion, isWhiteMove)
                else
                    figures[letterEnd][numberEnd] = figures[letterStart][numberStart]
                figures[letterStart][numberStart] = null
            }

            arr.add(
                ProblemMove(
                    null, null,
                    letterStart, numberStart, letterEnd,
                    numberEnd, promotion, isCastling, move
                )
            )
            isWhiteMove = !isWhiteMove
        }
        return arr
    }

    private fun emptyToNull(string: String?): String? {
        return if (string.isNullOrEmpty())
            null
        else
            string
    }

    private fun toCasedFigure(figure: Char, isWhite: Boolean): Char = if (isWhite)
        figure.uppercaseChar()
    else
        figure.lowercaseChar()

    fun parseFigure(figureInfo: String, isWhite: Boolean): FigurePosition? {
        if (figureInfo.length > 3 || figureInfo.length < 2)
            return null
        var letter = figureInfo[0]
        var figure: Char? = null
        if (figureInfo.length == 3) {
            figure = figureInfo[0].uppercaseChar()
            letter = figureInfo[1]
        }
        val number = figureInfo.last().digitToIntOrNull() ?: return null
        if (number !in 0 until 8 || letter !in 'a'..'h' || (figure != null && figure !in "KQBNR"))
            return null
        return FigurePosition(null, null, letter, number, figure, isWhite)
    }
}

package schrom.aoc2022

import java.io.File

fun main() {
    val data = File("data/day2").readLines()

    val sum = data.sumOf {
        val other = when (it[0]) {
            'A' -> Rock()
            'B' -> Paper()
            'C' -> Scissors()
            else -> throw IllegalArgumentException(it)
        }
        val me = when (it[2]) {
            'X' -> other.getWinAgainstShape()
            'Y' -> other
            'Z' -> other.getLoseAgainstShape()
            else -> throw IllegalArgumentException(it)
        }
        me.getScoreFor(other)
    }

    println(sum)
}
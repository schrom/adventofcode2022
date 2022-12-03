package schrom.aoc2022

import java.io.File

fun main() {
    val data = File("data/day2").readLines()

    val sum = data.sumOf {
        val other = when (it[0]) {
            'A' -> Shape.ROCK
            'B' -> Shape.PAPER
            'C' -> Shape.SCISSORS
            else -> throw IllegalArgumentException(it)
        }
        val me = when (it[2]) {
            'X' -> other.isWinningAgainst()
            'Y' -> other
            'Z' -> other.isLosingAgainst()
            else -> throw IllegalArgumentException(it)
        }
        me.getScoreFor(other)
    }

    println(sum)
}
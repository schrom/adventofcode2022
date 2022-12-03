package schrom.aoc2022

import java.io.File

const val loseScore = 0
const val drawScore = 3
const val winScore = 6

enum class Shape {
    ROCK {
        private val shapeScore = 1

        override fun getScoreFor(other: Shape): Int {
            return when (other) {
                ROCK -> shapeScore + drawScore
                PAPER -> shapeScore + loseScore
                SCISSORS -> shapeScore + winScore
            }
        }

        override fun isWinningAgainst() = SCISSORS
        override fun isLosingAgainst() = PAPER
    },
    PAPER {
        private val shapeScore = 2
        override fun getScoreFor(other: Shape): Int {
            return when (other) {
                ROCK -> shapeScore + winScore
                PAPER -> shapeScore + drawScore
                SCISSORS -> shapeScore + loseScore
            }
        }

        override fun isWinningAgainst() = ROCK
        override fun isLosingAgainst() = SCISSORS

    },
    SCISSORS {
        private val shapeScore = 3

        override fun getScoreFor(other: Shape): Int {
            return when (other) {
                ROCK -> shapeScore + loseScore
                PAPER -> shapeScore + winScore
                SCISSORS -> shapeScore + drawScore
            }
        }

        override fun isWinningAgainst() = PAPER
        override fun isLosingAgainst() = ROCK

    };

    abstract fun isWinningAgainst(): Shape
    abstract fun isLosingAgainst(): Shape
    abstract fun getScoreFor(other: Shape): Int
}

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
            'X' -> Shape.ROCK
            'Y' -> Shape.PAPER
            'Z' -> Shape.SCISSORS
            else -> throw IllegalArgumentException(it)
        }
        me.getScoreFor(other)
    }

    println(sum)
}
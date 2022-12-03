package schrom.aoc2022

import java.io.File

sealed interface Shape {
    fun getWinAgainstShape(): Shape
    fun getLoseAgainstShape(): Shape
    fun getScoreFor(other: Shape): Int
}

const val loseScore = 0
const val drawScore = 3
const val winScore = 6

class Rock : Shape {
    private val shapeScore = 1

    override fun getScoreFor(other: Shape): Int {
        return when (other) {
            is Rock -> shapeScore + drawScore
            is Paper -> shapeScore + loseScore
            is Scissors -> shapeScore + winScore
        }
    }

    override fun getWinAgainstShape() = Scissors()
    override fun getLoseAgainstShape() = Paper()
}

class Paper : Shape {
    private val shapeScore = 2
    override fun getScoreFor(other: Shape): Int {
        return when (other) {
            is Rock -> shapeScore + winScore
            is Paper -> shapeScore + drawScore
            is Scissors -> shapeScore + loseScore
        }
    }

    override fun getWinAgainstShape() = Rock()
    override fun getLoseAgainstShape() = Scissors()
}

class Scissors : Shape {
    private val shapeScore = 3

    override fun getScoreFor(other: Shape): Int {
        return when (other) {
            is Rock -> shapeScore + loseScore
            is Paper -> shapeScore + winScore
            is Scissors -> shapeScore + drawScore
        }
    }

    override fun getWinAgainstShape() = Paper()
    override fun getLoseAgainstShape() = Rock()
}

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
            'X' -> Rock()
            'Y' -> Paper()
            'Z' -> Scissors()
            else -> throw IllegalArgumentException(it)
        }
        me.getScoreFor(other)
    }

    println(sum)
}
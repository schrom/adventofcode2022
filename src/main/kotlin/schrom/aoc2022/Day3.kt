package schrom.aoc2022

import java.io.File

class BackPackItem(private val char: Char) {
    fun getPriority(): Int {
        return if (char.isLowerCase()) {
            char - 'a' + 1
        } else {
            char - 'A' + 27
        }
    }
}

fun main() {
    val data = File("data/day3").readLines()

    val sum =
        data
            .map {
                Pair(it.substring(0, it.length / 2), it.substring(it.length / 2))
            }
            .map {
                Pair(it.first.toSet(), it.second.toSet())
            }
            .map {
                it.first intersect it.second
            }.sumOf {
                it.sumOf { c -> BackPackItem(c).getPriority() }
            }

    println(sum)
}
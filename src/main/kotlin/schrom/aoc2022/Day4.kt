package schrom.aoc2022

import java.io.File

class CleanupRange(range: String) {

    private val from: Int
    private val to: Int

    init {
        val sections = range.split('-')
        from = sections[0].toInt()
        to = sections[1].toInt()
    }

    fun isFullyInRangeOf(other: CleanupRange): Boolean {
        return (from >= other.from && to <= other.to)
    }

    fun overlapsWith(other: CleanupRange): Boolean {
        return (other.from in from..to) || (other.to in from..to)
    }
}

fun main() {
    val data = File("data/day4").readLines()

    val sum = data
        .map {
            val ranges = it.split(',')
            Pair(
                CleanupRange(ranges[0]),
                CleanupRange(ranges[1])
            )
        }.count {
            it.first.isFullyInRangeOf(it.second) || it.second.isFullyInRangeOf(it.first)
        }

    println(sum)
}
package schrom.aoc2022

import java.io.File

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
            it.first.isFullyInRangeOf(it.second) || it.second.isFullyInRangeOf(it.first) || it.first.overlapsWith(it.second)
        }

    println(sum)
}
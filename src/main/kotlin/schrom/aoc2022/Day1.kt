package schrom.aoc2022

import java.io.File

fun main() {
    val data = File("data/day1").readLines()

    var currentMax = 0
    var current = 0

    data.forEach {
        if (it.isEmpty()) {
            if (currentMax < current) {
                currentMax = current
                println("New maximum $currentMax")
            }
            current = 0
        } else {
            current += it.toInt()
        }
    }
}
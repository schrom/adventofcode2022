package schrom.aoc2022

import java.io.File

fun main() {
    val data = File("data/day1").readLines()

    val calories = mutableListOf<Int>()
    var current = 0

    data.forEach {
        if (it.isEmpty()) {
            calories.add(current)
            current = 0
        } else {
            current += it.toInt()
        }
    }

    calories.sortDescending()

    println(calories[0] + calories[1] + calories[2])
}
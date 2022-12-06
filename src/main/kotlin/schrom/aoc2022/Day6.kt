package schrom.aoc2022

import java.io.File

fun main() {

    val data = File("data/day6").readLines().first()

    for (i in 0..data.length) {
        val sample = data.slice(i..i + 3)
        if (sample.toSet().size == 4) {
            println(i + 4)
            break
        }
    }

}
package schrom.aoc2022

import java.io.File

fun main() {

    val data = File("data/day9").readLines()

    val bridge = Bridge()
    val head = Head()
    val tails = List(9) { Tail() }

    data
        .map {
            it.split(" ")
        }
        .forEach {
            repeat(it[1].toInt()) { _ ->
                when (it[0]) {
                    "U" -> head.up()

                    "D" -> head.down()

                    "L" -> head.left()

                    "R" -> head.right()
                }
                if (tails[0].tooFarAwayFrom(head)) {
                    tails[0].moveTo(head)
                }
                for (i in 1..8) {
                    if (tails[i].tooFarAwayFrom(tails[i - 1])) {
                        tails[i].moveTo(tails[i - 1])
                    }
                }
                tails[8].touchFieldOf(bridge)
            }
        }

    val visitedFields = bridge.numberOfTouchedFields()

    println(visitedFields)
}
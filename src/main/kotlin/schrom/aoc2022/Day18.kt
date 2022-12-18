package schrom.aoc2022

import java.io.File

enum class Directions {
    UP, DOWN, LEFT, RIGHT, FRONT, BACK;
}

data class DropletFacing(val x: Int, val y: Int, val z: Int, val direction: Directions) {
    fun adjacent() =
        when (direction) {
            Directions.UP -> DropletFacing(x, y + 1, z, Directions.DOWN)
            Directions.DOWN -> DropletFacing(x, y - 1, z, Directions.UP)
            Directions.LEFT -> DropletFacing(x - 1, y, z, Directions.RIGHT)
            Directions.RIGHT -> DropletFacing(x + 1, y, z, Directions.LEFT)
            Directions.FRONT -> DropletFacing(x, y, z - 1, Directions.BACK)
            Directions.BACK -> DropletFacing(x, y, z + 1, Directions.FRONT)
        }
}

fun main() {

    val data = File("data/day18").readLines()

    val facings = mutableSetOf<DropletFacing>()

    data
        .map { it.split(",") }
        .flatMap {
            Directions.values().map { d ->
                DropletFacing(it[0].toInt(), it[1].toInt(), it[2].toInt(), d)
            }
        }
        .forEach { df ->
            if (facings.contains(df.adjacent())) {
                facings.remove(df.adjacent())
            } else {
                facings.add(df)
            }
        }

    val surface = facings.size

    println(surface)
}
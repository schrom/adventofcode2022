package schrom.aoc2022

import java.io.File
import kotlin.math.max

interface Day22Map {
    fun nextField(x: Int, y: Int, facing: Facing): Triple<Int, Int, Facing>?
}

class BoardMap : Day22Map {

    enum class Content { VOID, AIR, ROCK }

    private val lines = mutableListOf<String>()
    private var maxWidth = 0

    fun addRow(row: String) {
        lines.add(row)
        maxWidth = max(maxWidth, lines.size)
    }

    operator fun get(x: Int, y: Int): Content {
        return if (x > lines[y].lastIndex) {
            Content.VOID
        } else {
            when (lines[y][x]) {
                '.' -> Content.AIR
                ' ' -> Content.VOID
                '#' -> Content.ROCK
                else -> throw IllegalArgumentException("$x $y")
            }
        }
    }

    private fun height(): Int {
        return lines.size
    }

    private fun width(): Int {
        return maxWidth
    }

    override fun nextField(x: Int, y: Int, facing: Facing) : Triple<Int, Int, Facing>? {// xdiff: Int, ydiff: Int): Pair<Int, Int>? {
        var newX: Int
        var newY: Int
        var offsetx = facing.xyOffset.first // xdiff
        var offsety = facing.xyOffset.second // ydiff
        do {
            newX = (x + offsetx).mod(this.width())
            newY = (y + offsety).mod(this.height())
            offsetx +=  facing.xyOffset.first // xdiff
            offsety += facing.xyOffset.second // ydiff
        } while (this[newX, newY] == Content.VOID)

        return if (this[newX, newY] == Content.AIR) {
            Triple(newX, newY, facing)
        } else {
            null
        }
    }
}

enum class Facing(val value: Int, val xyOffset: Pair<Int, Int>) {
    UP(3, Pair(0,-1)),
    RIGHT(0, Pair(1,0)),
    DOWN(1, Pair(0,1)),
    LEFT(2, Pair(-1,0));

    fun turn(direction: Char): Facing {
        when (this) {
            UP -> {
                if (direction == 'R') {
                    return RIGHT
                }
                if (direction == 'L') {
                    return LEFT
                }
            }

            RIGHT -> {
                if (direction == 'R') {
                    return DOWN
                }
                if (direction == 'L') {
                    return UP
                }
            }

            DOWN -> {
                if (direction == 'R') {
                    return LEFT
                }
                if (direction == 'L') {
                    return RIGHT
                }
            }

            LEFT -> {
                if (direction == 'R') {
                    return UP
                }
                if (direction == 'L') {
                    return DOWN
                }
            }
        }
        throw IllegalArgumentException("$direction")
    }
}

class Person(var facing: Facing) {

    var x: Int = 0
    var y: Int = 0

    fun turn(direction: Char) {
        facing = facing.turn(direction)
    }

    fun walk(steps: Int, boardMap: Day22Map) {
        for (s in 0 until steps) {
            val nextField = boardMap.nextField(x, y, facing) // facing.xyOffset.first, facing.xyOffset.second)
            if (nextField == null) {
                break
            } else {
                x = nextField.first
                y = nextField.second
                facing = nextField.third
            }
        }
    }
}

fun main() {

    val data = File("data/day22").readLines().iterator()

    val regex = """^(\d+)(.*)""".toRegex()

    val boardMap = BoardMap()
    while (true) {
        val row = data.next()
        if (row.isEmpty()) {
            break
        }
        boardMap.addRow(row)
    }

    var startingX = 0
    while (boardMap[startingX, 0] != BoardMap.Content.AIR) {
        startingX++
    }
    val person = Person(Facing.RIGHT)
    person.x = startingX
    person.y = 0

    var instruction = data.next()

    while (true) {

        if (instruction.isEmpty()) {
            break
        }

        if (instruction[0] == 'R' || instruction[0] == 'L') {
            person.turn(instruction[0])
            instruction = instruction.substring(1)
        } else {
            val matches = regex.matchEntire(instruction)!!
            val steps = matches.groupValues[1].toInt()
            person.walk(steps, boardMap)

            instruction = matches.groupValues[2]
        }
    }

    println(1000 * (person.y + 1) + 4 * (person.x + 1) + person.facing.value)
}
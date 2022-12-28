package schrom.aoc2022

import java.io.File


class CubeBoardMap : Day22Map {

    enum class Content { VOID, AIR, ROCK }

    private val lines = mutableListOf<String>()

    fun addRow(row: String) {
        lines.add(row)
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

    override fun nextField(x: Int, y: Int, facing: Facing): Triple<Int, Int, Facing>? {
        val newFacing: Facing

        var newX: Int = x + facing.xyOffset.first
        var newY: Int = y + facing.xyOffset.second
        val folding = checkForFolding(newX, newY, facing)
        newX = folding.first
        newY = folding.second
        newFacing = folding.third

        return when(this[newX, newY]){
            Content.VOID -> throw IllegalStateException("Oh dear, you fell off the map $newX $newY $newFacing")
            Content.AIR -> Triple(newX, newY, newFacing)
            Content.ROCK -> null
        }
    }

    private fun checkForFolding(x: Int, y: Int, facing: Facing): Triple<Int, Int, Facing> {

        if (y == -1 && facing == Facing.UP ) {
            if (x in 50..99) { // F up
                return Triple(0, 100 + x, Facing.RIGHT)
            }
            if (x in 100..149) { // G up
                return Triple(x - 100, 199, Facing.UP)
            }
        }
        if (y == 50 && facing == Facing.DOWN) {
            if (x in 100..149) { // C down
                return Triple(99, x - 50, Facing.LEFT)
            }
        }
        if (y == 99 && facing == Facing.UP) {
            if (x in 0..49) { // D up
                return Triple(50, 50 + x, Facing.RIGHT)
            }
        }
        if (y == 150 && facing == Facing.DOWN) {
            if (x in 50..99) { // A down
                return Triple(49, 100 + x, Facing.LEFT)
            }
        }
        if (y == 200 && facing == Facing.DOWN) {
            if (x in 0..49) { // G down
                return Triple(x + 100, 0, Facing.DOWN)
            }
        }
        if (x == -1 && facing == Facing.LEFT) {
            if (y in 100..149) { // lower E left
                return Triple(50, 149 - y, Facing.RIGHT)
            }
            if (y in 150..199) { // F left
                return Triple(y - 100, 0, Facing.DOWN)
            }
        }
        if (x == 49 && facing == Facing.LEFT) {
            if (y in 0..49) { // upper E left
                return Triple(0, 149 - y, Facing.RIGHT)
            }
            if (y in 50..99) { // D left
                return Triple(y - 50, 100, Facing.DOWN)
            }
        }
        if (x == 50 && facing == Facing.RIGHT) {
            if (y in 150..199) { // A right
                return Triple(y - 100, 149, Facing.UP)
            }
        }
        if (x == 100 && facing == Facing.RIGHT) {
            if (y in 50..99) { // C right
                return Triple(50 + y, 49, Facing.UP)
            }
            if (y in 100..149) { // lower B right
                return Triple(149, 149 - y, Facing.LEFT)
            }
        }
        if (x == 150 && facing == Facing.RIGHT) {
            if (y in 0..49) { // upper B right
                return Triple(99, 149 - y, Facing.LEFT)
            }
        }
        return Triple(x, y, facing)
    }
}

fun main() {

    val data = File("data/day22").readLines().iterator()

    val regex = """^(\d+)(.*)""".toRegex()

    val boardMap = CubeBoardMap()
    while (true) {
        val row = data.next()
        if (row.isEmpty()) {
            break
        }
        boardMap.addRow(row)
    }

    var startingX = 0
    while (boardMap[startingX, 0] != CubeBoardMap.Content.AIR) {
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
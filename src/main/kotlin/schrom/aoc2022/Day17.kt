package schrom.aoc2022

import java.io.File

abstract class Rock(var bottomLeftX: Int, var bottomLeftY: Long) {

    abstract fun canMoveLeft(chamber: TallNarrowChamber): Boolean
    abstract fun canMoveRight(chamber: TallNarrowChamber): Boolean
    abstract fun canMoveDown(chamber: TallNarrowChamber): Boolean

    fun moveLeft() {
        bottomLeftX--
    }

    fun moveRight() {
        bottomLeftX++
    }

    fun moveDown() {
        bottomLeftY--
    }

    abstract fun settle(chamber: TallNarrowChamber)
}

class MinusShaped(bottomLeftX: Int, bottomLeftY: Long) : Rock(bottomLeftX, bottomLeftY) {
    override fun canMoveLeft(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX - 1, bottomLeftY] == TallNarrowChamber.Content.AIR
    }

    override fun canMoveRight(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX + 4, bottomLeftY] == TallNarrowChamber.Content.AIR
    }

    override fun canMoveDown(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX, bottomLeftY - 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 1, bottomLeftY - 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 2, bottomLeftY - 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 3, bottomLeftY - 1] == TallNarrowChamber.Content.AIR
    }

    override fun settle(chamber: TallNarrowChamber) {
        chamber[bottomLeftX, bottomLeftY] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 1, bottomLeftY] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 2, bottomLeftY] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 3, bottomLeftY] = TallNarrowChamber.Content.ROCK
    }

}

class PlusShaped(bottomLeftX: Int, bottomLeftY: Long) : Rock(bottomLeftX, bottomLeftY) {
    override fun canMoveLeft(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX, bottomLeftY] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX - 1, bottomLeftY + 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX, bottomLeftY + 2] == TallNarrowChamber.Content.AIR
    }

    override fun canMoveRight(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX + 2, bottomLeftY] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 3, bottomLeftY + 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 2, bottomLeftY + 2] == TallNarrowChamber.Content.AIR
    }

    override fun canMoveDown(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX, bottomLeftY] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 1, bottomLeftY - 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 2, bottomLeftY] == TallNarrowChamber.Content.AIR
    }

    override fun settle(chamber: TallNarrowChamber) {
        chamber[bottomLeftX, bottomLeftY + 1] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 1, bottomLeftY + 1] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 2, bottomLeftY + 1] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 1, bottomLeftY] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 1, bottomLeftY + 2] = TallNarrowChamber.Content.ROCK
    }

}

class LShaped(bottomLeftX: Int, bottomLeftY: Long) : Rock(bottomLeftX, bottomLeftY) {
    override fun canMoveLeft(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX - 1, bottomLeftY] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 1, bottomLeftY + 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 1, bottomLeftY + 2] == TallNarrowChamber.Content.AIR
    }

    override fun canMoveRight(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX + 3, bottomLeftY] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 3, bottomLeftY + 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 3, bottomLeftY + 2] == TallNarrowChamber.Content.AIR
    }

    override fun canMoveDown(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX, bottomLeftY - 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 1, bottomLeftY - 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 2, bottomLeftY - 1] == TallNarrowChamber.Content.AIR
    }

    override fun settle(chamber: TallNarrowChamber) {
        chamber[bottomLeftX, bottomLeftY] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 1, bottomLeftY] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 2, bottomLeftY] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 2, bottomLeftY + 1] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 2, bottomLeftY + 2] = TallNarrowChamber.Content.ROCK
    }

}

class IShaped(bottomLeftX: Int, bottomLeftY: Long) : Rock(bottomLeftX, bottomLeftY) {
    override fun canMoveLeft(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX - 1, bottomLeftY] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX - 1, bottomLeftY + 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX - 1, bottomLeftY + 2] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX - 1, bottomLeftY + 3] == TallNarrowChamber.Content.AIR
    }

    override fun canMoveRight(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX + 1, bottomLeftY] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 1, bottomLeftY + 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 1, bottomLeftY + 2] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 1, bottomLeftY + 3] == TallNarrowChamber.Content.AIR
    }

    override fun canMoveDown(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX, bottomLeftY - 1] == TallNarrowChamber.Content.AIR
    }

    override fun settle(chamber: TallNarrowChamber) {
        chamber[bottomLeftX, bottomLeftY] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX, bottomLeftY + 1] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX, bottomLeftY + 2] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX, bottomLeftY + 3] = TallNarrowChamber.Content.ROCK
    }

}

class OShaped(bottomLeftX: Int, bottomLeftY: Long) : Rock(bottomLeftX, bottomLeftY) {
    override fun canMoveLeft(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX - 1, bottomLeftY] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX - 1, bottomLeftY + 1] == TallNarrowChamber.Content.AIR
    }

    override fun canMoveRight(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX + 2, bottomLeftY] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 2, bottomLeftY + 1] == TallNarrowChamber.Content.AIR
    }

    override fun canMoveDown(chamber: TallNarrowChamber): Boolean {
        return chamber[bottomLeftX, bottomLeftY - 1] == TallNarrowChamber.Content.AIR &&
                chamber[bottomLeftX + 1, bottomLeftY - 1] == TallNarrowChamber.Content.AIR
    }

    override fun settle(chamber: TallNarrowChamber) {
        chamber[bottomLeftX, bottomLeftY] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX, bottomLeftY + 1] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 1, bottomLeftY] = TallNarrowChamber.Content.ROCK
        chamber[bottomLeftX + 1, bottomLeftY + 1] = TallNarrowChamber.Content.ROCK
    }

}

class TallNarrowChamber {

    var highestRock = 0L
    private var floorLevel = 0L

    val map = mutableMapOf<Long, MutableMap<Int, Content>>()

    enum class Content(val char: Char) { AIR('.'), WALL('|'), ROCK('#'), FLOOR('-') }

    operator fun get(col: Int, row: Long): Content {
        if (col == 0 || col == 8) {
            return Content.WALL
        }
        if (row == floorLevel) {
            return Content.FLOOR
        }
        return map[row]?.get(col) ?: Content.AIR
    }

    operator fun set(col: Int, row: Long, value: Content) {
        if (row > highestRock) {
            highestRock = row
        }
        val rowData = map.getOrPut(row) { mutableMapOf() }
        rowData[col] = value
    }

    override fun toString(): String {
        return (highestRock downTo 0).joinToString("\n") { row ->
            (0..8).map { col ->
                this[col, row].char
            }.joinToString("")
        }
    }
}

fun main() {

    val data = File("data/day17").readLines()
    val jetBlow = data.first().map { it }.toCollection(ArrayDeque())
    val chamber = TallNarrowChamber()

    (1..2022).forEach { i ->
        var rock: Rock? = null
        if (i.mod(5) == 1) {
            rock = MinusShaped(3, chamber.highestRock + 4)
        } else if (i.mod(5) == 2) {
            rock = PlusShaped(3, chamber.highestRock + 4)
        } else if (i.mod(5) == 3) {
            rock = LShaped(3, chamber.highestRock + 4)
        } else if (i.mod(5) == 4) {
            rock = IShaped(3, chamber.highestRock + 4)
        } else if (i.mod(5) == 0) {
            rock = OShaped(3, chamber.highestRock + 4)
        }
        checkNotNull(rock)

        while (true) {
            if (jetBlow.first() == '<' && rock.canMoveLeft(chamber)) {
                rock.moveLeft()
            }
            if (jetBlow.first() == '>' && rock.canMoveRight(chamber)) {
                rock.moveRight()
            }
            jetBlow.addLast(jetBlow.removeFirst())

            if (rock.canMoveDown(chamber)) {
                rock.moveDown()
            } else {
                rock.settle(chamber)
                break
            }
        }
    }
    println(chamber.highestRock)
}
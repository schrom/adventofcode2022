package schrom.aoc2022

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

class Bridge {
    private val touchedFields = mutableSetOf<Pair<Int, Int>>()

    fun touch(x: Int, y: Int) {
        touchedFields.add(Pair(x, y))
    }

    fun numberOfTouchedFields() = touchedFields.size
}

open class Knot(var x: Int, var y: Int)

class Head : Knot(0, 0) {
    fun up() = y++

    fun down() = y--

    fun left() = x--

    fun right() = x++
}

class Tail : Knot(0, 0) {

    fun tooFarAwayFrom(knot: Knot): Boolean {
        return max(abs(x - knot.x), abs(y - knot.y)) > 1
    }

    fun moveTo(knot: Knot) {
        y += (1 * (knot.y - y).sign)
        x += (1 * (knot.x - x).sign)
    }

    fun touchFieldOf(map: Bridge) {
        map.touch(x, y)
    }
}

fun main() {

    val data = File("data/day9").readLines()

    val bridge = Bridge()
    val head = Head()
    val tail = Tail()

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
                if (tail.tooFarAwayFrom(head)) {
                    tail.moveTo(head)
                }
                tail.touchFieldOf(bridge)
            }
        }

    val visitedFields = bridge.numberOfTouchedFields()

    println(visitedFields)
}
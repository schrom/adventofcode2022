package schrom.aoc2022

import java.io.File

class CaveMapWithFloor(maxY: Int) {

    private val floorLevel = maxY + 2

    enum class Content(val char: Char) {
        AIR('.'), ROCK('#'), SAND('s');
    }

    private val cave = mutableMapOf<Int, MutableMap<Int, Content>>()

    fun stoneAt(pos: Pair<Int, Int>) {
        this[pos] = Content.ROCK
    }

    fun sandAt(pos: Pair<Int, Int>) {
        this[pos] = Content.SAND
    }

    operator fun set(pos: Pair<Int, Int>, content: Content) {
        if (!cave.containsKey(pos.second)) {
            cave[pos.second] = mutableMapOf()
        }
        cave[pos.second]!![pos.first] = content
    }

    operator fun get(pos: Pair<Int, Int>): Content =
        cave[pos.second]?.get(pos.first) ?: if (pos.second == floorLevel) {
            Content.ROCK
        } else {
            Content.AIR
        }
}

class PilingSand(private val cave: CaveMapWithFloor) {

    fun fallDownFrom(pos: Pair<Int, Int>) {
        val down = Pair(pos.first, pos.second + 1)
        if (cave[down] == CaveMapWithFloor.Content.AIR) {
            fallDownFrom(down)
            return
        }

        val downLeft = Pair(pos.first - 1, pos.second + 1)
        if (cave[downLeft] == CaveMapWithFloor.Content.AIR) {
            fallDownFrom(downLeft)
            return
        }

        val downRight = Pair(pos.first + 1, pos.second + 1)
        if (cave[downRight] == CaveMapWithFloor.Content.AIR) {
            fallDownFrom(downRight)
            return
        }
        cave.sandAt(pos)
    }
}

fun main() {

    val data = File("data/day14").readLines()

    val rocks = data
        .map { SolidRock(it) }
        .toList()

    val maxY = rocks.maxOf { it.getMaxY() }

    val caveMap = CaveMapWithFloor(maxY)

    rocks.flatMap { it.getFullPath() }.forEach { caveMap.stoneAt(it) }

    var fallingSand = 0

    while (caveMap[Pair(500, 0)] != CaveMapWithFloor.Content.SAND) {
        PilingSand(caveMap).fallDownFrom(Pair(500, 0))
        fallingSand++
    }

    println(fallingSand)
}
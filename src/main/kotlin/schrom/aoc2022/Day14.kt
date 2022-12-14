package schrom.aoc2022

import java.io.File
import java.lang.Integer.max
import kotlin.math.min

class SolidRock(input: String) {

    private val path: List<Pair<Int, Int>>

    init {
        path = input
            .split(" -> ")
            .map { it.split(",") }
            .map { Pair(it[0].toInt(), it[1].toInt()) }
            .toList()
    }

    fun getMinX() = path.minOf { it.first }
    fun getMaxX() = path.maxOf { it.first }
    fun getMaxY() = path.maxOf { it.second }

    fun getFullPath(): List<Pair<Int, Int>> {
        return path.windowed(2).flatMap {
            val from = it[0]
            val to = it[1]

            (min(from.first, to.first)..max(from.first, to.first)).flatMap { x ->
                (min(from.second, to.second)..max(from.second, to.second)).map { y ->
                    Pair(x, y)
                }
            }
        }
    }
}

class CaveMap(minX: Int, maxX: Int, maxY: Int) {

    enum class Content(val char: Char) {
        AIR('.'), ROCK('#'), SAND('s'), VOID(' ');
    }

    private val cave = mutableMapOf<Int, MutableMap<Int, Content>>()

    init {
        for (row in 0..maxY) {
            cave[row] = mutableMapOf()
            for (col in minX..maxX) {
                cave[row]!![col] = Content.AIR
            }
        }
    }

    fun stoneAt(pos: Pair<Int, Int>) {
        this[pos] = Content.ROCK
    }

    fun sandAt(pos: Pair<Int, Int>) {
        this[pos] = Content.SAND
    }

    operator fun set(pos: Pair<Int, Int>, content: Content) {
        cave[pos.second]!![pos.first] = content
    }

    operator fun get(pos: Pair<Int, Int>): Content = cave[pos.second]?.get(pos.first) ?: Content.VOID
}

class Sand(private val cave: CaveMap) {

    fun fallDownFrom(pos: Pair<Int, Int>): Boolean {
        val down = Pair(pos.first, pos.second + 1)
        return when (cave[down]) {
            CaveMap.Content.AIR -> fallDownFrom(down)
            CaveMap.Content.VOID -> false
            else -> {
                val downLeft = Pair(pos.first - 1, pos.second + 1)
                return when (cave[downLeft]) {
                    CaveMap.Content.AIR -> fallDownFrom(downLeft)
                    CaveMap.Content.VOID -> false
                    else -> {
                        val downRight = Pair(pos.first + 1, pos.second + 1)
                        return when (cave[downRight]) {
                            CaveMap.Content.AIR -> fallDownFrom(downRight)
                            CaveMap.Content.VOID -> false
                            else -> {
                                cave.sandAt(pos)
                                true
                            }
                        }
                    }
                }
            }
        }
    }
}

fun main() {

    val data = File("data/day14").readLines()

    val rocks = data
        .map { SolidRock(it) }
        .toList()

    val minX = rocks.minOf { it.getMinX() }
    val maxX = rocks.maxOf { it.getMaxX() }
    val maxY = rocks.maxOf { it.getMaxY() }

    val caveMap = CaveMap(minX = minX, maxX = maxX, maxY = maxY)

    rocks.flatMap { it.getFullPath() }.forEach { caveMap.stoneAt(it) }

    var fallingSand = 0

    while (Sand(caveMap).fallDownFrom(Pair(500, 0))) {
        fallingSand++
    }

    println(fallingSand)
}
package schrom.aoc2022

import java.io.File
import kotlin.math.max
import kotlin.math.min

abstract class Day23Map<T> {

    abstract val defaultContent: () -> T

    protected val mapData = mutableMapOf<Int, MutableMap<Int, T>>()

    protected var minX = Int.MAX_VALUE
    protected var maxX = Int.MIN_VALUE
    protected var minY = Int.MAX_VALUE
    protected var maxY = Int.MIN_VALUE

    operator fun get(x: Int, y: Int): T {
        return mapData.getOrPut(y) { mutableMapOf() }.getOrPut(x, defaultContent)
    }

    operator fun set(x: Int, y: Int, value: T) {
        mapData.getOrPut(y) { mutableMapOf() }[x] = value
        if (value != defaultContent) {
            minX = min(minX, x)
            minY = min(minY, y)
            maxX = max(maxX, x)
            maxY = max(maxY, y)
        }
    }
}

class ProposeMoveMap : Day23Map<MutableList<Proposal>>() {
    override val defaultContent = { mutableListOf<Proposal>() }

    fun forEachDo(f: (x: Int, y: Int) -> Unit) {
        for (y in mapData.keys) {
            for (x in mapData[y]!!.keys) {
                f.invoke(x, y)
            }
        }
    }

    fun cleanupCollisions(): ProposeMoveMap {
        val cleanedMoves = ProposeMoveMap()
        var collision = false
        this.forEachDo { x, y ->
            if (this[x, y].size == 1) {
                val move = this[x, y].first()
                cleanedMoves[move.getTarget().first, move.getTarget().second].add(move)
            }
            if (this[x, y].size > 1) {
                this[x, y].forEach { move ->
                    when (move) {
                        is ElfMove -> {
                            cleanedMoves[move.getSource().first, move.getSource().second].add(
                                ElfStay(move.getSource().first, move.getSource().second)
                            )
                            collision = true
                        }

                        is ElfStay -> {
                            cleanedMoves[move.getTarget().first, move.getTarget().second].add(move)
                        }
                    }
                }
            }
        }
        return if (collision) {
            cleanedMoves.cleanupCollisions()
        } else {
            cleanedMoves
        }
    }
}

class GroveMap : Day23Map<GroveMap.Content>() {

    enum class Content { GROUND, ELF }

    override val defaultContent = { Content.GROUND }

    fun forEachDo(f: (x: Int, y: Int) -> Unit) {
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                f.invoke(x, y)
            }
        }
    }

    fun forEachElfDo(f: (x: Int, y: Int) -> Unit) {
        forEachDo { x, y ->
            if (this[x, y] == Content.ELF) {
                f.invoke(x, y)
            }
        }
    }
}

class Considerations {

    var rules = listOf(
        ::considerStay,
        ::considerNorth,
        ::considerSouth,
        ::considerWest,
        ::considerEast,
        ::finallyStay
    )

    fun rotate() {
        rules = listOf(rules[0], rules[2], rules[3], rules[4], rules[1], rules[5])

    }

    private fun considerStay(x: Int, y: Int, grove: GroveMap): Proposal? {
        return if (grove[x, y - 1] == GroveMap.Content.GROUND
            && grove[x - 1, y - 1] == GroveMap.Content.GROUND
            && grove[x + 1, y - 1] == GroveMap.Content.GROUND
            && grove[x - 1, y] == GroveMap.Content.GROUND
            && grove[x + 1, y] == GroveMap.Content.GROUND
            && grove[x, y + 1] == GroveMap.Content.GROUND
            && grove[x - 1, y + 1] == GroveMap.Content.GROUND
            && grove[x + 1, y + 1] == GroveMap.Content.GROUND
        ) {
            ElfStay(x, y)
        } else {
            null
        }
    }

    private fun considerNorth(x: Int, y: Int, grove: GroveMap): Proposal? {
        return if (grove[x, y - 1] == GroveMap.Content.GROUND
            && grove[x - 1, y - 1] == GroveMap.Content.GROUND
            && grove[x + 1, y - 1] == GroveMap.Content.GROUND
        ) {
            ElfMove(x, y, x, y - 1)
        } else {
            null
        }
    }

    private fun considerSouth(x: Int, y: Int, grove: GroveMap): Proposal? {
        return if (grove[x, y + 1] == GroveMap.Content.GROUND
            && grove[x - 1, y + 1] == GroveMap.Content.GROUND
            && grove[x + 1, y + 1] == GroveMap.Content.GROUND
        ) {
            ElfMove(x, y, x, y + 1)
        } else {
            null
        }
    }

    private fun considerWest(x: Int, y: Int, grove: GroveMap): Proposal? {
        return if (grove[x - 1, y - 1] == GroveMap.Content.GROUND
            && grove[x - 1, y] == GroveMap.Content.GROUND
            && grove[x - 1, y + 1] == GroveMap.Content.GROUND
        ) {
            ElfMove(x, y, x - 1, y)
        } else {
            null
        }
    }

    private fun considerEast(x: Int, y: Int, grove: GroveMap): Proposal? {
        return if (grove[x + 1, y - 1] == GroveMap.Content.GROUND
            && grove[x + 1, y] == GroveMap.Content.GROUND
            && grove[x + 1, y + 1] == GroveMap.Content.GROUND
        ) {
            ElfMove(x, y, x + 1, y)
        } else {
            null
        }
    }

    private fun finallyStay(x: Int, y: Int, grove: GroveMap): Proposal? {
        return ElfStay(x, y)
    }
}

interface Proposal {
    fun getTarget(): Pair<Int, Int>
    fun getSource(): Pair<Int, Int>
    fun executeMove(map: GroveMap)
}

data class ElfMove(val fromX: Int, val fromY: Int, val toX: Int, val toY: Int) : Proposal {
    override fun getTarget(): Pair<Int, Int> {
        return Pair(toX, toY)
    }

    override fun getSource(): Pair<Int, Int> {
        return Pair(fromX, fromY)
    }

    override fun executeMove(map: GroveMap) {
        map[toX, toY] = GroveMap.Content.ELF
    }
}

data class ElfStay(val x: Int, val y: Int) : Proposal {
    override fun getTarget(): Pair<Int, Int> {
        return Pair(x, y)
    }

    override fun getSource(): Pair<Int, Int> {
        return Pair(x, y)
    }

    override fun executeMove(map: GroveMap) {
        map[x, y] = GroveMap.Content.ELF
    }
}

fun main() {

    val data = File("data/day23").readLines()

    var map = GroveMap()
    val considerations = Considerations()

    data
        .forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c == '#') {
                    map[x, y] = GroveMap.Content.ELF
                }
            }
        }

    repeat(10) {

        var moves = ProposeMoveMap()
        map.forEachElfDo { x, y ->
            for (r in 0..considerations.rules.lastIndex) {
                val proposal = considerations.rules[r].invoke(x, y, map)
                if (proposal != null) {
                    val target = proposal.getTarget()
                    moves[target.first, target.second].add(proposal)
                    break
                }
            }
        }
        moves = moves.cleanupCollisions()

        map = GroveMap()
        moves.forEachDo { x, y ->
            if (moves[x, y].size == 1) {
                moves[x, y].first().executeMove(map)
            }
        }

        considerations.rotate()
    }

    var result = 0
    map.forEachDo { x, y ->
        if (map[x, y] == GroveMap.Content.GROUND) {
            result++
        }
    }
    println(result)
}
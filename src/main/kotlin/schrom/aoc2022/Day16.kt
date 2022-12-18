package schrom.aoc2022

import java.io.File

class Valve(val id: String, val flowRate: Int, val leadsTo: List<String>) {

    override fun toString(): String = id
}

class FloydWalsh(allValves: Map<String, Valve>) {

    private val dist = Matrix<String, Int>(Int.MAX_VALUE)
    private val next = Matrix<String, String>("")

    internal class Matrix<T, V>(private val empty: V) {
        private val matrix = mutableMapOf<T, MutableMap<T, V>>()

        operator fun get(i: T, j: T): V {
            return matrix.getOrPut(i) { mutableMapOf() }[j] ?: empty
        }

        operator fun set(i: T, j: T, value: V) {
            matrix.getOrPut(i) { mutableMapOf() }[j] = value
        }
    }

    init {
        val edges = allValves.flatMap { it.value.leadsTo.map { to -> Pair(it.key, to) } }
        edges.forEach {
            dist[it.first, it.second] = 1
            next[it.first, it.second] = it.second
        }
        allValves.keys.forEach {
            dist[it, it] = 0
            next[it, it] = it
        }
        allValves.keys.forEach { k ->
            allValves.keys.forEach { i ->
                allValves.keys.forEach { j ->
                    if (dist[i, j] > sum(dist[i, k], dist[j, k])) {
                        dist[i, j] = sum(dist[i, k], dist[j, k])
                        next[i, j] = next[i, k]
                    }
                }
            }
        }
    }

    private fun sum(i: Int, j: Int): Int {
        return if (i == Int.MAX_VALUE || j == Int.MAX_VALUE) {
            Int.MAX_VALUE
        } else {
            i + j
        }
    }

    fun path(from: String, to: String): List<String> {
        if (next[from, to] == "") {
            return emptyList()
        }
        val path = mutableListOf(from)
        var nextFrom = from
        while (nextFrom != to) {
            nextFrom = next[nextFrom, to]
            path.add(nextFrom)
        }
        return path
    }

    fun distance(from: String, to: String): Int {
        return dist[from, to]
    }
}

class ValveMap(private val maxMinutes: Int, private val pathsAndLengths: FloydWalsh) {

    var maximumPressure = 0

    fun findMaximumPressure(
        current: Valve,
        valvesLeft: Set<Valve>,
        minuteCount: Int,
        currentPath: List<Valve>,
        pressurePath: MutableList<Int>,
    ) {
        val openValveTime =
            if (current.flowRate > 0) {
                1
            } else {
                0
            }
        pressurePath.add((maxMinutes - minuteCount - openValveTime) * current.flowRate)

        if (valvesLeft.isEmpty()) {
            // end of path, no more valves left
            endOfPath(pressurePath.sum(), currentPath)
        }

        valvesLeft.forEach {
            if (minuteCount + openValveTime + pathsAndLengths.distance(current.id, it.id) < maxMinutes) {
                findMaximumPressure(
                    it,
                    valvesLeft - it,
                    minuteCount + openValveTime + pathsAndLengths.distance(current.id, it.id),
                    currentPath + it,
                    pressurePath
                )
            } else {
                // end of path, no more time left
                endOfPath(pressurePath.sum(), currentPath)
            }
        }
        pressurePath.removeLast()
    }

    private fun endOfPath(pressureSum: Int, currentPath: List<Valve>) {
        if (pressureSum > maximumPressure) {
            maximumPressure = pressureSum
//            println("new maxPressure $maximumPressure at $currentPath")
//            currentPath.windowed(2).forEach {
//                print(pathsAndLengths.path(it[0].id, it[1].id))
//            }
//            println()
        }
    }
}

fun main() {

    val data = File("data/day16").readLines()

    val regex = """Valve (..) has flow rate=(\d+); tunnel.? lead.? to valve.? (.*)""".toRegex()

    val allValves = data.map {
        val matchResult = regex.matchEntire(it)!!
        Valve(
            matchResult.groupValues[1],
            matchResult.groupValues[2].toInt(),
            matchResult.groupValues[3].split(", ").toList()
        )
    }.associateBy { it.id }

    val valveMap = ValveMap(30, FloydWalsh(allValves))
    val start = allValves["AA"]!!

    valveMap.findMaximumPressure(
        current = start,
        valvesLeft = allValves.values.filter { it.flowRate > 0 }.toSet() - start,
        minuteCount = 0,
        currentPath = listOf(start),
        mutableListOf()
    )

    println(valveMap.maximumPressure)
}

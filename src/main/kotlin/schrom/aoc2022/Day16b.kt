package schrom.aoc2022

import java.io.File
import java.util.stream.IntStream

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

    val floydWalsh = FloydWalsh(allValves)

    val start = allValves["AA"]!!
    var combinedMaxPressure = 0

    val valvesToOpenList = allValves.values
        .filter { it.flowRate > 0 }

    IntStream.range(0, (2 shl valvesToOpenList.size))
        .parallel()
        .forEach { i ->

            val myValves = mutableListOf<Valve>()
            val elephantValves = mutableListOf<Valve>()

            (0..valvesToOpenList.lastIndex).forEach { j ->
                if ((i and (1 shl j)) != 0) {
                    myValves.add(valvesToOpenList[j])
                } else {
                    elephantValves.add(valvesToOpenList[j])
                }
            }

            var valveMap = ValveMap(26, floydWalsh)
            valveMap.findMaximumPressure(
                current = start,
                valvesLeft = myValves.toSet(),
                minuteCount = 0,
                currentPath = listOf(start),
                mutableListOf()
            )
            val myMaxPressure = valveMap.maximumPressure

            valveMap = ValveMap(26, floydWalsh)
            valveMap.findMaximumPressure(
                current = start,
                valvesLeft = elephantValves.toSet(),
                minuteCount = 0,
                currentPath = listOf(start),
                mutableListOf()
            )
            val elephantMaxPressure = valveMap.maximumPressure

            combinedMaxPressure = combinedMaxPressure.coerceAtLeast(myMaxPressure + elephantMaxPressure)
        }

    println(combinedMaxPressure)
}
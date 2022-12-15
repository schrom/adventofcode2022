package schrom.aoc2022

import java.io.File
import kotlin.math.abs

class SignalMap2(val rowForResult: Int) {

    private val row = mutableListOf<Pair<Int, Int>>()

    fun markAsSignal(from: Int, to: Int) {
        row.add(Pair(from, to))
    }

    fun getSortedRow(): List<Pair<Int, Int>> {
        return row.sortedBy { it.first }
    }

}

class Sensor2(x: Int, y: Int) : Coord(x, y) {

    fun distanceTo(beacon: Beacon): Int {
        return abs(this.x - beacon.x) + abs(this.y - beacon.y)
    }

    fun fillSignalMapRow(signalMap: SignalMap2, distance: Int) {
        if (signalMap.rowForResult !in this.y - distance..this.y + distance) {
            return
        }
        val rowDistanceFromSignal = signalMap.rowForResult - this.y

        if (rowDistanceFromSignal < 0) {
            signalMap.markAsSignal(this.x - distance - rowDistanceFromSignal, this.x + distance + rowDistanceFromSignal)
        } else {
            signalMap.markAsSignal(this.x - distance + rowDistanceFromSignal, this.x + distance - rowDistanceFromSignal)
        }
    }
}

fun main() {

    val regex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

    val data = File("data/day15").readLines()

    val sensorData = data.map {
        val matchEntire = regex.matchEntire(it)!!
        val sensor = Sensor2(matchEntire.groupValues[1].toInt(), matchEntire.groupValues[2].toInt())
        val beacon = Beacon(matchEntire.groupValues[3].toInt(), matchEntire.groupValues[4].toInt())
        Pair(sensor, beacon)
    }.toList()

    run loop@{
        (0..4000000).forEach { y ->
            val signalMap = SignalMap2(y)

            sensorData.forEach {
                it.first.fillSignalMapRow(signalMap, it.first.distanceTo(it.second))
            }

            val row = signalMap.getSortedRow()

            var x = 0
            row.forEach {
                if (x in it.first..it.second) {
                    x = it.second + 1
                } else if (x < it.first) {
                    println("$x $y")
                    return@loop
                }
            }
            if (x <= 4000000) {
                println("$x $y")
                return@loop
            }
        }
    }
}

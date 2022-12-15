package schrom.aoc2022

import java.io.File
import kotlin.math.abs

class SignalMap(val rowForResult: Int) {

    enum class Content(val char: Char) {
        NOTHING('.'), BEACON('B'), SENSOR('S'), SIGNAL('#');
    }

    private val row = mutableMapOf<Int, Content>()

    fun getRow(): List<Content> {
        return row.values.toList()
    }

    operator fun set(pos: Coord, content: Content) {
        if (pos.y == rowForResult) {
            row[pos.x] = content
        }
    }

    operator fun get(posX: Int): Content = row[posX] ?: Content.NOTHING
}

abstract class Coord(val x: Int, val y: Int)

class Sensor(x: Int, y: Int) : Coord(x, y) {
    fun distanceTo(beacon: Beacon): Int {
        return abs(this.x - beacon.x) + abs(this.y - beacon.y)
    }

    fun fillSignalMapRow(signalMap: SignalMap, distance: Int) {
        if (signalMap.rowForResult !in this.y - distance..this.y + distance) {
            return
        }
        val rowDistanceFromSignal = signalMap.rowForResult - this.y

        if (rowDistanceFromSignal < 0) {
            for (x in (-distance - rowDistanceFromSignal)..(distance + rowDistanceFromSignal)) {
                signalMap[Signal(this.x + x, signalMap.rowForResult)] = SignalMap.Content.SIGNAL
            }
        } else {
            for (x in (-distance + rowDistanceFromSignal)..(distance - rowDistanceFromSignal)) {
                signalMap[Signal(this.x + x, signalMap.rowForResult)] = SignalMap.Content.SIGNAL
            }
        }
    }
}

class Beacon(x: Int, y: Int) : Coord(x, y)

class Signal(x: Int, y: Int) : Coord(x, y)

fun main() {

    val regex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

    val data = File("data/day15").readLines()

    val signalMap = SignalMap(2000000)

    data.forEach {
        val matchEntire = regex.matchEntire(it)!!
        val sensor = Sensor(matchEntire.groupValues[1].toInt(), matchEntire.groupValues[2].toInt())
        val beacon = Beacon(matchEntire.groupValues[3].toInt(), matchEntire.groupValues[4].toInt())
        sensor.fillSignalMapRow(signalMap, sensor.distanceTo(beacon))
        signalMap[sensor] = SignalMap.Content.SENSOR
        signalMap[beacon] = SignalMap.Content.BEACON
    }

    val noBeaconPossible = signalMap.getRow().count {
        it == SignalMap.Content.SIGNAL
    }

    println(noBeaconPossible)
}

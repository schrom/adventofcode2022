package schrom.aoc2022

import java.io.File

fun main() {

    val data = File("data/day24").readLines()

    val mapHeight = data.size
    val mapWidth = data.first().length

    val blizzards = mutableListOf<Blizzard>()
    data.forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            when (c) {
                '>' -> blizzards.add(Blizzard(x, y, mapWidth, mapHeight, Blizzard.Direction.E))
                '<' -> blizzards.add(Blizzard(x, y, mapWidth, mapHeight, Blizzard.Direction.W))
                '^' -> blizzards.add(Blizzard(x, y, mapWidth, mapHeight, Blizzard.Direction.N))
                'v' -> blizzards.add(Blizzard(x, y, mapWidth, mapHeight, Blizzard.Direction.S))
            }
        }
    }

    val allBlizzardPositions = mutableListOf<BlizzardMap>()
    // blizzards will have a full cycle at most after n*m times
    for (i in 0 until (mapHeight - 2) * (mapWidth - 2)) {
        val blizzardMap = BlizzardMap(mapWidth, mapHeight, blizzards)
        allBlizzardPositions.add(blizzardMap)
        blizzards.forEach { it.move() }
    }


    var firstTrip = 0
    var bfsMap = BFSMap(allBlizzardPositions)
    bfsMap.setStart(1, 0, 0)
    bfsMap.setGoal(mapWidth - 2, mapHeight - 1)

    for (maxMinute in 1..Int.MAX_VALUE) {
        firstTrip = bfsMap.findTheWay(maxMinute)
        if (firstTrip > 0) {
            break
        }
    }

    var secondTrip = 0
    bfsMap = BFSMap(allBlizzardPositions)
    bfsMap.setStart(mapWidth - 2, mapHeight - 1, firstTrip + 1)
    bfsMap.setGoal(1, 0)
    for (maxMinute in firstTrip + 1..Int.MAX_VALUE) {
        secondTrip = bfsMap.findTheWay(maxMinute)
        if (secondTrip > 0) {
            break
        }
    }

    var thirdTrip = 0
    bfsMap = BFSMap(allBlizzardPositions)
    bfsMap.setStart(1, 0, secondTrip + 1)
    bfsMap.setGoal(mapWidth - 2, mapHeight - 1)
    for (maxMinute in secondTrip + 1..Int.MAX_VALUE) {
        thirdTrip = bfsMap.findTheWay(maxMinute)
        if (thirdTrip > 0) {
            break
        }
    }
    println(thirdTrip)
}

package schrom.aoc2022

import java.io.File

class BlizzardMap(width: Int, val height: Int, blizzards: List<Blizzard>) {

    enum class Content { WALL, AIR, BLIZZARD }

    val data = Array(height) { Array(width) { Content.AIR } }

    init {
        (2 until width).forEach { x -> this[x, 0] = Content.WALL }
        (0 until width - 2).forEach { x -> this[x, height - 1] = Content.WALL }
        (0 until height).forEach { y -> this[0, y] = Content.WALL; this[width - 1, y] = Content.WALL }

        blizzards.forEach {
            this[it.xPos, it.yPos] = Content.BLIZZARD
        }
    }

    operator fun set(col: Int, row: Int, value: Content) {
        data[row][col] = value
    }

    operator fun get(col: Int, row: Int): Content {
        return data[row][col]
    }
}

class Blizzard(
    var xPos: Int, var yPos: Int,
    private val xMax: Int, private val yMax: Int,
    private val direction: Direction
) {
    enum class Direction { N, E, S, W }

    fun move() {
        when (direction) {
            Direction.N -> {
                yPos--
                if (yPos == 0) {
                    yPos = yMax - 2
                }
            }

            Direction.E -> {
                xPos++
                if (xPos == xMax - 1) {
                    xPos = 1
                }
            }

            Direction.S -> {
                yPos++
                if (yPos == yMax - 1) {
                    yPos = 1
                }
            }

            Direction.W -> {
                xPos--
                if (xPos == 0) {
                    xPos = xMax - 2
                }
            }
        }
    }
}

class BFSMap(private val maps: List<BlizzardMap>) {

    data class Step(val x: Int, val y: Int, val minute: Int)

    private val seenInQueue = mutableSetOf<Step>()
    private val queue = ArrayDeque<Step>()
    private val laterQueue = ArrayDeque<Step>()

    private var goalX: Int = 0
    private var goalY: Int = 0

    fun setStart(startX: Int, startY: Int, atMinute: Int) {
        queue.addLast(Step(startX, startY, atMinute))
    }

    fun setGoal(goalX: Int, goalY: Int) {
        this.goalX = goalX
        this.goalY = goalY
    }

    fun findTheWay(maxMinutes: Int): Int {

        // resume search
        queue.addAll(laterQueue)
        laterQueue.clear()
        seenInQueue.clear()

        while (queue.isNotEmpty()) {
            val step = queue.removeLast()

            if (step.x == goalX && step.y == goalY) {
                return step.minute
            }

            if (step.minute < maxMinutes) {
                checkForPossibleNextStep(step, 0, 0, step.minute + 1) // WAIT
                checkForPossibleNextStep(step, 0, -1, step.minute + 1) // N
                checkForPossibleNextStep(step, -1, 0, step.minute + 1) // W
                checkForPossibleNextStep(step, 1, 0, step.minute + 1) // E
                checkForPossibleNextStep(step, 0, 1, step.minute + 1) // S
            } else {
                laterQueue.add(step)
            }
        }
        return -1
    }

    private fun checkForPossibleNextStep(
        step: Step,
        xOffset: Int,
        yOffset: Int,
        minute: Int,
    ) {
        if (step.y + yOffset >= 0 && // do not fall off the map at entrance or exit
            step.y + yOffset < maps[minute % maps.size].height &&
            maps[minute % maps.size][step.x + xOffset, step.y + yOffset] == BlizzardMap.Content.AIR
        ) {
            val newStep = Step(step.x + xOffset, step.y + yOffset, minute)
            if (!seenInQueue.contains(newStep)) {
                queue.add(newStep)
                seenInQueue.add(newStep)
            }
        }
    }
}

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

    var tripDuration = 0
    val bfsMap = BFSMap(allBlizzardPositions)
    bfsMap.setStart(1, 0, 0)
    bfsMap.setGoal(mapWidth - 2, mapHeight - 1)

    for (maxMinute in 1..Int.MAX_VALUE) {
        tripDuration = bfsMap.findTheWay(maxMinute)
        if (tripDuration > 0) {
            break
        }
    }
    println(tripDuration)
}

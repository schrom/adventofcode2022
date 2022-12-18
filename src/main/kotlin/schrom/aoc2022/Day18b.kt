package schrom.aoc2022

import java.io.File

data class Droplet(val x: Int, val y: Int, val z: Int)

class Room(x: Int, y: Int, z: Int) {

    enum class Content { AIR, STEAM, DROPLET }

    var data: Array<Array<Array<Content>>>

    init {
        data = Array(x) { Array(y) { Array(z) { Content.AIR } } }
    }

    operator fun set(x: Int, y: Int, z: Int, value: Content) {
        data[x][y][z] = value
    }

    operator fun get(x: Int, y: Int, z: Int): Content {
        return data[x][y][z]
    }

    fun fillWithSteam(x: Int, y: Int, z: Int, outerSurface: MutableSet<DropletFacing>) {
        if (this[x, y, z] != Content.AIR) {
            return
        }

        this[x, y, z] = Content.STEAM

        if (x < data.lastIndex) {
            if (this[x + 1, y, z] == Content.DROPLET) {
                outerSurface.add(DropletFacing(x + 1, y, z, Directions.LEFT))
            } else {
                fillWithSteam(x + 1, y, z, outerSurface)
            }
        }
        if (x > 0) {
            if (this[x - 1, y, z] == Content.DROPLET) {
                outerSurface.add(DropletFacing(x - 1, y, z, Directions.RIGHT))
            } else {
                fillWithSteam(x - 1, y, z, outerSurface)
            }
        }
        if (y < data[0].lastIndex) {
            if (this[x, y + 1, z] == Content.DROPLET) {
                outerSurface.add(DropletFacing(x, y + 1, z, Directions.DOWN))
            } else {
                fillWithSteam(x, y + 1, z, outerSurface)
            }
        }
        if (y > 0) {
            if (this[x, y - 1, z] == Content.DROPLET) {
                outerSurface.add(DropletFacing(x, y - 1, z, Directions.UP))
            } else {
                fillWithSteam(x, y - 1, z, outerSurface)
            }
        }
        if (z < data[0][0].lastIndex) {
            if (this[x, y, z + 1] == Content.DROPLET) {
                outerSurface.add(DropletFacing(x, y, z + 1, Directions.FRONT))
            } else {
                fillWithSteam(x, y, z + 1, outerSurface)
            }
        }
        if (z > 0) {
            if (this[x, y, z - 1] == Content.DROPLET) {
                outerSurface.add(DropletFacing(x, y, z - 1, Directions.BACK))
            } else {
                fillWithSteam(x, y, z - 1, outerSurface)
            }
        }
    }
}

// may need -Xss515m to run

fun main() {

    val data = File("data/day18").readLines()

    val droplets = data
        .map { it.split(",") }
        .map {
            val x = it[0].toInt()
            val y = it[1].toInt()
            val z = it[2].toInt()
            Droplet(x+1, y+1, z+1) // transpose 1 field so x,y,z 0 in room is always empty
        }

    val room = Room(25,25,25)
    droplets.forEach {
        room[it.x, it.y, it.z] = Room.Content.DROPLET
    }

    val outerSurfaces = mutableSetOf<DropletFacing>()
    room.fillWithSteam(0, 0, 0, outerSurfaces)

    println(outerSurfaces.size)
}
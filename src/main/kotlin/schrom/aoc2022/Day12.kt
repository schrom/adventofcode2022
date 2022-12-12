package schrom.aoc2022

import java.io.File

data class Field(val x: Int, val y: Int)

class HeightMap {

    lateinit var map: Array<Array<Int>>
    lateinit var start: Field
    lateinit var end: Field

    fun createFrom(data: List<String>) {
        map = Array(data.size) { emptyArray() }
        data.forEachIndexed { y, s ->
            map[y] = Array(s.length) { x ->
                when (val character = s[x]) {
                    'S' -> {
                        start = Field(x, y)
                        0
                    }

                    'E' -> {
                        end = Field(x, y)
                        25
                    }

                    else -> {
                        character - 'a'
                    }
                }
            }
        }
    }

    fun heightOf(field: Field): Int {
        return map[field.y][field.x]
    }
}

class Searchmap(private val heightMap: HeightMap) {

    private val searchmap = Array(heightMap.map.size) { Array<Int?>(heightMap.map.first().size) { null } }

    var pathLength = 0

    fun searchPathFromTo(from: List<Field>, to: Field, step: Int) {

        from.forEach { visit(it, step) }

        if (from.contains(to)) {
            pathLength = step
            return
        }

        val newFields = from
            .flatMap { f ->
                getNeighbours(f)
                    .filter { isNotTooSteepUpwards(f, it) }
                    .filter { hasNotBeenVisited(it) }
            }.distinct()

        searchPathFromTo(newFields, to, step + 1)
    }

    operator fun get(field : Field): Int? {
        return searchmap[field.y][field.x]
    }

    operator fun set(field: Field, value: Int) {
        searchmap[field.y][field.x] = value
    }

    private fun visit(field: Field, step: Int) {
        this[field] = step
    }

    private fun hasNotBeenVisited(field: Field): Boolean {
        return this[field] == null
    }

    private fun isNotTooSteepUpwards(current: Field, next: Field): Boolean {
        return (heightMap.heightOf(next) - heightMap.heightOf(current)) <= 1
    }

    private fun getNeighbours(from: Field): List<Field> {
        return mutableListOf<Field>().apply {
            if (from.x > 0) {
                this.add(Field(from.x - 1, from.y))
            }
            if (from.y > 0) {
                this.add(Field(from.x, from.y - 1))
            }
            if (from.x < searchmap[0].size - 1) {
                this.add(Field(from.x + 1, from.y))
            }
            if (from.y < searchmap.size - 1) {
                this.add(Field(from.x, from.y + 1))
            }
        }
    }
}


fun main() {

    val data = File("data/day12").readLines()

    val heightMap = HeightMap().also { it.createFrom(data) }

    val searchmap = Searchmap(heightMap)
    searchmap.searchPathFromTo(listOf(heightMap.start), heightMap.end, 0)
    println(searchmap.pathLength)

}
package schrom.aoc2022

import java.io.File

class SearchmapDownwards(private val heightMap: HeightMap) {

    private val searchmap = Array(heightMap.map.size) { Array<Int?>(heightMap.map.first().size) { null } }

    var pathLength = 0

    fun searchPathFromToHeightA(from: List<Field>, step: Int) {

        from.forEach { visit(it, step) }

        from.forEach {
            if (heightMap.heightOf(it) == 0) {
                pathLength = step
                return
            }
        }

        val newFields = from
            .flatMap { f ->
                getNeighbours(f)
                    .filter { isNotTooSteepDownwards(f, it) }
                    .filter { hasNotBeenVisited(it) }
            }.distinct()

        searchPathFromToHeightA(newFields, step + 1)
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

    private fun isNotTooSteepDownwards(current: Field, next: Field): Boolean {
        return (heightMap.heightOf(current) - heightMap.heightOf(next)) <= 1
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

    val searchmap = SearchmapDownwards(heightMap)
    searchmap.searchPathFromToHeightA(listOf(heightMap.end), 0)
    println(searchmap.pathLength)

}
package schrom.aoc2022

import java.io.File

fun main() {

    val data = File("data/day8").readLines()

    val treeMap = Array(data.size) { i -> data[i].toCharArray().map { it.digitToInt() }.toIntArray() }

    val visibleTreeMap = Array(data.size) { i -> BooleanArray(data[i].length) { false } }

    // all rows left to right
    for (row in treeMap.indices) {
        var treeHeight = -1
        for (col in treeMap[row].indices) {
            if (treeMap[row][col] > treeHeight) {
                treeHeight = treeMap[row][col]
                visibleTreeMap[row][col] = true
            }
        }
    }

    // all rows right to left
    for (row in treeMap.indices) {
        var treeHeight = -1
        for (col in treeMap[row].indices.reversed()) {
            if (treeMap[row][col] > treeHeight) {
                treeHeight = treeMap[row][col]
                visibleTreeMap[row][col] = true
            }
        }
    }

    // all columns top to bottom
    for (col in treeMap[0].indices) {
        var treeHeight = -1
        for (row in treeMap.indices) {
            if (treeMap[row][col] > treeHeight) {
                treeHeight = treeMap[row][col]
                visibleTreeMap[row][col] = true
            }
        }
    }

    // all columns bottom to top
    for (col in treeMap[0].indices) {
        var treeHeight = -1
        for (row in treeMap.indices.reversed()) {
            if (treeMap[row][col] > treeHeight) {
                treeHeight = treeMap[row][col]
                visibleTreeMap[row][col] = true
            }
        }
    }

    val treesVisible = visibleTreeMap.sumOf { row ->
        row.count { it }
    }

    println(treesVisible)
}
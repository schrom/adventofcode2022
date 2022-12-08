package schrom.aoc2022

import java.io.File

fun main() {

    val data = File("data/day8").readLines()

    val treeMap = Array(data.size) { i -> data[i].toCharArray().map { it.digitToInt() }.toIntArray() }

    val scenicFactorMap = Array(data.size) { i -> IntArray(data[i].length) { 0 } }

    for (row in treeMap.indices) {
        for (col in treeMap[row].indices) {

            var up = 0
            for (i in row - 1 downTo 0) {
                up++
                if (treeMap[i][col] >= treeMap[row][col]) {
                    break
                }
            }

            var down = 0
            for (i in row + 1 until treeMap.size) {
                down++
                if (treeMap[i][col] >= treeMap[row][col]) {
                    break
                }
            }

            var left = 0
            for (i in col - 1 downTo 0) {
                left++
                if (treeMap[row][i] >= treeMap[row][col]) {
                    break
                }
            }
            var right = 0
            for (i in col + 1 until treeMap[row].size) {
                right++
                if (treeMap[row][i] >= treeMap[row][col]) {
                    break
                }
            }

            scenicFactorMap[row][col] = up * down * left * right
        }
    }

    val maxScenicFactor = scenicFactorMap.maxOf { row ->
        row.max()
    }

    println(maxScenicFactor)
}
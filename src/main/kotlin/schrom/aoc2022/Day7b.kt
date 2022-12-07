package schrom.aoc2022

import java.io.File

fun main() {

    val data = File("data/day7").readLines().toMutableList()

    val root = Directory("/", null)
    root.process(data)

    val minimumSpaceToFree =  30000000 - (70000000 - root.sizeOfDirectoryAndSubDirectories())

    val toDelete = root.sizeMap()
        .map {
            it.value
        }
        .sorted()
        .find {
            it >= minimumSpaceToFree
        }

    println(toDelete)
}
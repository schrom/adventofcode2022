package schrom.aoc2022

import java.io.File

fun main() {
    val data = File("data/day5").readLines()

    val crane = Crane().createStacksFromData(data)

    val moveCommand = """move (\d+) from (\d+) to (\d+)""".toRegex()

    data
        .dropWhile { !it.startsWith("move") }
        .forEach {
            val matchResult = moveCommand.matchEntire(it)!!
            val amount = matchResult.groupValues[1].toInt()
            val source = matchResult.groupValues[2].toInt()
            val destination = matchResult.groupValues[3].toInt()

            crane.rearrange()
                .move(amount).from(source).to(destination)
                .executeWithCraneMover9001()
        }

    val topCrates = crane.stacks.getAllTopCrates()
        .map { it.crateId }
        .joinToString(separator = "")

    println(topCrates)
}
package schrom.aoc2022

import java.io.File

fun main() {

    val data = File("data/day19").readLines()

    val regex =
        """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()

    val blueprints = data
        .map {
            regex.matchEntire(it)
        }
        .map {
            Blueprint(
                it!!.groupValues[1].toInt(),
                it.groupValues[2].toInt(),
                it.groupValues[3].toInt(),
                it.groupValues[4].toInt(),
                it.groupValues[5].toInt(),
                it.groupValues[6].toInt(),
                it.groupValues[7].toInt(),
            )
        }

    val product = (0..2).map { i ->

        val robotFactory = RobotFactory(
            oreInStock = 0,
            clayInStock = 0,
            obsidianInStock = 0,
            oreCollectingRobots = 1,
            clayCollectingRobots = 0,
            obsidianCollectingRobots = 0,
            geodeCrackingRobots = 0,
            geodesCracked = 0,
            blueprint = blueprints[i]
        )

        val result = Result()
        runFactory(32, robotFactory, result)
        result.maxGeodesCracked
    }
        .reduce { acc, i -> acc * i }

    println(product)
}
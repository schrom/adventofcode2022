package schrom.aoc2022

import java.io.File

data class RobotFactory(
    var oreInStock: Int,
    var clayInStock: Int,
    var obsidianInStock: Int,

    var oreCollectingRobots: Int,
    var clayCollectingRobots: Int,
    var obsidianCollectingRobots: Int,
    var geodeCrackingRobots: Int,

    var geodesCracked: Int,

    val blueprint: Blueprint
)

data class Result(
    var maxGeodesCracked: Int = 0,
)

class Blueprint(
    val id: Int,
    val oreRobotCostOre: Int,
    val clayRobotCostOre: Int,
    val obsidianRobotCostOre: Int,
    val obsidianRobotCostClay: Int,
    val geodeRobotCostOre: Int,
    val geodeRobotCostObsidian: Int,
) {
    val maxOreCost: Int = listOf(oreRobotCostOre, clayRobotCostOre, obsidianRobotCostOre, geodeRobotCostOre).max()
}

fun runFactory(minutesRemaining: Int, factory: RobotFactory, result: Result) {

    // we can not spend more than factory.blueprint.geodeRobotCostObsidian per round
    // so stop making those robots when we get that amount per minute, it would just pile up in stock
    val buildObsidianRobots = factory.obsidianCollectingRobots < factory.blueprint.geodeRobotCostObsidian

    // same for clay
    val buildClayRobots = factory.clayCollectingRobots < factory.blueprint.obsidianRobotCostClay

    // and ore
    val buildOreRobots = factory.oreCollectingRobots < factory.blueprint.maxOreCost

    // can we even beat the high score if we build nothing but geode drones until the end?
    if (result.maxGeodesCracked - factory.geodesCracked > (minutesRemaining * factory.geodeCrackingRobots)
        + (minutesRemaining * (minutesRemaining - 1)) / 2
    ) {
        return
    }

    if (minutesRemaining == 0) {
        if (factory.geodesCracked > result.maxGeodesCracked) {
            result.maxGeodesCracked = factory.geodesCracked
        }
        return
    }

    val oreInStockBeforeCollecting = factory.oreInStock
    val obsidianInStockBeforeCollecting = factory.obsidianInStock
    val clayInStockBeforeCollecting = factory.clayInStock

    factory.oreInStock += factory.oreCollectingRobots
    factory.clayInStock += factory.clayCollectingRobots
    factory.obsidianInStock += factory.obsidianCollectingRobots
    factory.geodesCracked += factory.geodeCrackingRobots

    if (oreInStockBeforeCollecting >= factory.blueprint.geodeRobotCostOre &&
        obsidianInStockBeforeCollecting >= factory.blueprint.geodeRobotCostObsidian
    ) {
        val newFactory = factory.copy()
        newFactory.geodeCrackingRobots++
        newFactory.oreInStock -= factory.blueprint.geodeRobotCostOre
        newFactory.obsidianInStock -= factory.blueprint.geodeRobotCostObsidian
        runFactory(minutesRemaining - 1, newFactory, result)
    }
    if (buildObsidianRobots &&
        oreInStockBeforeCollecting >= factory.blueprint.obsidianRobotCostOre &&
        clayInStockBeforeCollecting >= factory.blueprint.obsidianRobotCostClay
    ) {
        val newFactory = factory.copy()
        newFactory.obsidianCollectingRobots++
        newFactory.oreInStock -= factory.blueprint.obsidianRobotCostOre
        newFactory.clayInStock -= factory.blueprint.obsidianRobotCostClay
        runFactory(minutesRemaining - 1, newFactory, result)
    }
    if (buildClayRobots &&
        oreInStockBeforeCollecting >= factory.blueprint.clayRobotCostOre
    ) {
        val newFactory = factory.copy()
        newFactory.clayCollectingRobots++
        newFactory.oreInStock -= factory.blueprint.clayRobotCostOre
        runFactory(minutesRemaining - 1, newFactory, result)
    }
    if (buildOreRobots &&
        oreInStockBeforeCollecting >= factory.blueprint.oreRobotCostOre
    ) {
        val newFactory = factory.copy()
        newFactory.oreCollectingRobots++
        newFactory.oreInStock -= factory.blueprint.oreRobotCostOre
        runFactory(minutesRemaining - 1, newFactory, result)
    }
    runFactory(minutesRemaining - 1, factory.copy(), result)
}

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

    val qualityLevels = blueprints
        .parallelStream()
        .map {
            val robotFactory = RobotFactory(
                oreInStock = 0,
                clayInStock = 0,
                obsidianInStock = 0,
                oreCollectingRobots = 1,
                clayCollectingRobots = 0,
                obsidianCollectingRobots = 0,
                geodeCrackingRobots = 0,
                geodesCracked = 0,
                blueprint = it
            )

            val result = Result()
            runFactory(24, robotFactory, result)
            println("found quality level for ${it.id} ${result.maxGeodesCracked}")
            Pair(it.id, result.maxGeodesCracked)
        }
        .toList()

    val sum = qualityLevels.sumOf {
        it.first * it.second
    }

    println(sum)
}
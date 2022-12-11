package schrom.aoc2022

import java.io.File
import java.util.stream.Stream

class BigMonkeySwarm(data: Iterator<String>) {

    private val monkeySwarm = mutableListOf<BigMonkey>()
    var divisibleFactor = 1L

    init {
        while (true) {
            data.next() // skip Monkey ID
            val items = data.next().substringAfter("  Starting items: ").split(", ").map { it.toLong() }.toCollection(
                mutableListOf()
            )
            val operation = data.next().substringAfter("  Operation: new = ").split(" ")
            val divisible = data.next().substringAfter("  Test: divisible by ").toLong()
            val trueTarget = data.next().substringAfter("    If true: throw to monkey ").toInt()
            val falseTarget = data.next().substringAfter("    If false: throw to monkey ").toInt()

            val monkey = BigMonkey(
                monkeySwarm = this,
                itemWorries = items,
                worryOperation = createFunction(operation),
                divisibleTest = divisible,
                trueTarget = trueTarget,
                falseTarget = falseTarget
            )
            monkeySwarm.add(monkey)

            if (data.hasNext()) {
                data.next() // skip empty line
            } else {
                break
            }
        }
        this.forEachMonkey { divisibleFactor *= it.divisibleTest }
    }


    private fun createFunction(ops: List<String>): (Long) -> Long {
        return when (ops[1]) {
            "+" -> when (ops[2]) {
                "old" -> { i -> i + i }
                else -> { i -> i + ops[2].toLong() }
            }

            "*" -> when (ops[2]) {
                "old" -> { i -> i * i }
                else -> { i -> i * ops[2].toLong() }
            }

            else -> throw IllegalArgumentException(ops.toString())
        }
    }

    operator fun get(monkeyId: Int) = monkeySwarm[monkeyId]
    operator fun set(monkeyId: Int, value: BigMonkey) {
        monkeySwarm[monkeyId] = value
    }

    fun forEachMonkey(function: (BigMonkey) -> Unit) {
        monkeySwarm.forEach{
            function(it)
        }
    }

    fun streamOfMonkeys(): Stream<BigMonkey> {
        return monkeySwarm.stream()
    }
}

class BigMonkey(
    val monkeySwarm: BigMonkeySwarm,
    val itemWorries: MutableList<Long>,
    val worryOperation: (Long) -> Long,
    val divisibleTest: Long,
    val trueTarget: Int,
    val falseTarget: Int,
) {

    var inspectionCount = 0L

    fun startRound() {
        while (itemWorries.isNotEmpty()) {
            startTurn(itemWorries.removeFirst())
        }
    }

    private fun startTurn(old: Long) {
        inspectionCount++
        val new = worryOperation(old).mod(monkeySwarm.divisibleFactor)
        if (new.mod(divisibleTest) == 0L) {
            monkeySwarm[trueTarget].getsItem(new)
        } else {
            monkeySwarm[falseTarget].getsItem(new)
        }
    }

    private fun getsItem(item: Long) {
        itemWorries.add(item)
    }
}

fun main() {

    val data = File("data/day11").readLines().iterator()

    val monkeySwarm = BigMonkeySwarm(data)

    repeat(10000) {
        monkeySwarm.forEachMonkey { monkey ->
            monkey.startRound()
        }
    }

    val inspectionsCounts = monkeySwarm.streamOfMonkeys().map { it.inspectionCount }.sorted().toList().asReversed()

    println(inspectionsCounts[0] * inspectionsCounts[1])
}
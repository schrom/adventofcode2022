package schrom.aoc2022

import java.io.File

data class Crate(val crateId: Char)

class Stack {
    private val crates = ArrayDeque<Crate>()

    fun setAtBottom(crate: Crate) {
        crates.addFirst(crate)
    }

    fun retrieveCratesFromTop(amount: Int): List<Crate> {
        return IntRange(1, amount)
            .map {
                crates.removeLast()
            }
    }

    fun storeCratesOnTop(newCrates: List<Crate>) {
        newCrates.forEach(crates::addLast)
    }

    fun getTopCrate() = crates.last()
}

class Stacks {
    private val stacks = mutableListOf<Stack>()

    fun stackAt(position: Int): Stack {
        val index = position - 1
        return stacks
            .elementAtOrElse(index) {
                stacks.add(index, Stack())
                stacks[index]
            }
    }

    fun getAllTopCrates(): List<Crate> = stacks.map(Stack::getTopCrate)
}

class Crane {
    val stacks = Stacks()

    fun createStacksFromData(data: List<String>): Crane {
        run loop@{
            data.forEach {
                if (!it.startsWith("[")) return@loop

                val chunks = it.chunked(4)
                chunks
                    .map(String::trim)
                    .forEachIndexed { index, s ->
                        val stack = stacks.stackAt(index + 1)
                        if (s.isNotEmpty()) {
                            stack.setAtBottom(Crate(s[1]))
                        }
                    }
            }
        }
        return this
    }

    fun rearrange(): CraneOperation {
        return CraneOperation(this)
    }

    class CraneOperation(private val crane: Crane) {

        private var amount: Int = 0
        private var from: Stack? = null
        private var to: Stack? = null

        fun move(value: Int): CraneOperation {
            this.amount = value
            return this
        }

        fun from(pos: Int): CraneOperation {
            from = crane.stacks.stackAt(pos)
            return this
        }

        fun to(pos: Int): CraneOperation {
            to = crane.stacks.stackAt(pos)
            return this
        }

        fun execute() {
            val cratesFromTop = from?.retrieveCratesFromTop(amount)
            to?.storeCratesOnTop(cratesFromTop!!)
        }

        fun executeWithCraneMover9001() {
            val cratesFromTop = from?.retrieveCratesFromTop(amount)
            to?.storeCratesOnTop(cratesFromTop!!.reversed())
        }

    }
}

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
                .execute()
        }

    val topCrates = crane.stacks.getAllTopCrates()
        .map { it.crateId }
        .joinToString(separator = "")

    println(topCrates)
}
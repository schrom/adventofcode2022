package schrom.aoc2022

import java.io.File

class Clock {

    private var cycle = 1

    fun tick() = cycle++

}

class Cpu {

    var registerX = 1

}

class SignalStrength {
    private val strength = mutableListOf<Int>()

    fun checkForStrength(cycle: Int, cpu: Cpu) {
        if ((cycle - 20).mod(40) == 0) {
            strength.add(cycle * cpu.registerX)
        }
    }

    fun sum(): Int {
        return strength.sum()
    }

}

fun main() {

    val data = File("data/day10").readLines()

    val cpu = Cpu()
    val clock = Clock()
    val signalStrength = SignalStrength()

    data
        .map { it.split(" ") }
        .forEach {
            when (it[0]) {
                "noop" -> {
                    signalStrength.checkForStrength(clock.tick(), cpu)
                }

                "addx" -> {
                    repeat(2) { signalStrength.checkForStrength(clock.tick(), cpu) }
                    cpu.registerX += it[1].toInt()
                }
            }
        }

    val sum = signalStrength.sum()

    println(sum)
}
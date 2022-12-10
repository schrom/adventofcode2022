package schrom.aoc2022

import java.io.File

class Crt {
    private val screen = Array(6) { Array(40) { false } }

    fun checkPixel(cycle: Int, cpu: Cpu){
        val x = (cycle - 1).div(40)
        val y = (cycle - 1).mod(40)
        if(y in cpu.registerX -1 .. cpu.registerX +1){
            screen[x][y] = true
        }
    }

    fun showScreen() {
        screen.forEach { row ->
            row.forEach {
                if(it) {
                    print("XX")
                }else{
                    print("  ")
                }
            }
            println()
        }
    }
}

fun main() {

    val data = File("data/day10").readLines()

    val cpu = Cpu()
    val clock = Clock()
    val crt = Crt()

    data
        .map { it.split(" ") }
        .forEach {
            when (it[0]) {
                "noop" -> {
                    crt.checkPixel(clock.tick(), cpu)
                }

                "addx" -> {
                    repeat(2) { crt.checkPixel(clock.tick(), cpu) }
                    cpu.registerX += it[1].toInt()
                }
            }
        }

    crt.showScreen()
}
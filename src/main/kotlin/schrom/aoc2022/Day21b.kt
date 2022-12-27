package schrom.aoc2022

import java.io.File

fun main() {

    val data = File("data/day21").readLines()

    val context = MonkeyMathContext()

    data.forEach {
        val id = it.substringBefore(":")
        val op = it.substringAfter(":").trim()
        when {
            op.matches("""\d+""".toRegex()) -> context[id] = ConstantMonkey(op.toLong())
            op.matches("""\w{4} \S \w{4}""".toRegex()) -> {
                val left = op.substring(0, 4)
                val right = op.substring(7, 11)
                if (id == "root") {
                    context[id] = SubtractMonkey(left, right)
                } else {
                    context[id] = when (op[5]) {
                        '+' -> AddMonkey(left, right)
                        '-' -> SubtractMonkey(left, right)
                        '*' -> MultiplyMonkey(left, right)
                        '/' -> DivideMonkey(left, right)
                        else -> throw IllegalArgumentException()
                    }
                }
            }

        }
    }

    var human = 0L
    var incrementBy = 1L
    while (true) {
        context["humn"] = ConstantMonkey(human)
        val diff = context["root"].yield(context)
        if (diff == 0L) {
            println("*** result $human")
            break
        }
        if(diff < 0 ){
            human -= (incrementBy/2)
            incrementBy = 1
        }
        human += incrementBy
        incrementBy *=2
    }
}
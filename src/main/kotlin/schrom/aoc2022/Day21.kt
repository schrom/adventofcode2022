package schrom.aoc2022

import java.io.File

class MonkeyMathContext {

    private val operationsMap = mutableMapOf<String, MathOperationMonkey>()

    operator fun get(id: String): MathOperationMonkey {
        return operationsMap[id]!!
    }

    operator fun set(id: String, value: MathOperationMonkey) {
        operationsMap[id] = value
    }
}

interface MathOperationMonkey {
    fun yield(context: MonkeyMathContext): Long
}

class ConstantMonkey(private val value: Long) : MathOperationMonkey {
    override fun yield(context: MonkeyMathContext): Long {
        return value
    }
}

class AddMonkey(private val left: String, private val right: String) : MathOperationMonkey {
    override fun yield(context: MonkeyMathContext): Long {
        return context[left].yield(context) + context[right].yield(context)
    }
}

class SubtractMonkey(private val left: String, private val right: String) : MathOperationMonkey {
    override fun yield(context: MonkeyMathContext): Long {
        return context[left].yield(context) - context[right].yield(context)
    }
}

class MultiplyMonkey(private val left: String, private val right: String) : MathOperationMonkey {
    override fun yield(context: MonkeyMathContext): Long {
        return context[left].yield(context) * context[right].yield(context)
    }
}

class DivideMonkey(private val left: String, private val right: String) : MathOperationMonkey {
    override fun yield(context: MonkeyMathContext): Long {
        return context[left].yield(context) / context[right].yield(context)
    }
}

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

    val result = context["root"].yield(context)
    println(result)
}
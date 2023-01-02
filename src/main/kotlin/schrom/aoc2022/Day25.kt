package schrom.aoc2022

import java.io.File

class Snafu(val value: String) {

    private val snafuCharMap = SnafuSymbol.values().associateBy { it.char }

    enum class SnafuSymbol(val char: Char) {
        M2('='), M1('-'), ZERO('0'), ONE('1'), TWO('2');

        fun add(vararg others: SnafuSymbol): Pair<SnafuSymbol, SnafuSymbol> {
            val sum = (this.ordinal - 2) + others.sumOf { it.ordinal - 2 }
            return if (sum < -2) {
                Pair(M1, SnafuSymbol.values()[(sum + 2).mod(5)])
            } else if (sum > 2) {
                Pair(ONE, SnafuSymbol.values()[(sum + 2).mod(5)])
            } else {
                Pair(ZERO, SnafuSymbol.values()[(sum + 2).mod(5)])
            }
        }
    }

    private fun digitFor(char: Char): SnafuSymbol {
        return snafuCharMap[char]!!
    }

    fun add(another: Snafu): Snafu {
        var result = ""

        var carry = SnafuSymbol.ZERO
        var a = value
        var b = another.value

        while (a.isNotEmpty() || b.isNotEmpty()) {
            val s1 = a.lastOrNull() ?: '0'
            val s2 = b.lastOrNull() ?: '0'
            a = a.dropLast(1)
            b = b.dropLast(1)
            val sum = digitFor(s1).add(digitFor(s2), carry)
            result = sum.second.char + result
            carry = sum.first
        }
        return if (carry != SnafuSymbol.ZERO) {
            Snafu(carry.char + result)
        } else {
            Snafu(result)
        }
    }
}

fun main() {

    val data = File("data/day25").readLines()

    val result = data
        .map { Snafu(it) }
        .reduce { acc, snafu ->
            acc.add(snafu)
        }

    println(result.value)
}

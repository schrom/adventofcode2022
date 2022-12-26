package schrom.aoc2022

import java.io.File

fun main() {

    val data = File("data/day20").readLines()

    val decryptionKey = 811589153

    val ints = data.map { LongHolder(it.toLong() * decryptionKey) }

    val mixer = CircularMixer(ints)
    repeat(10) {
        ints.forEach {
            mixer.moveValueByAmount(it, it.value)
        }
    }

    val zero = ints.findLast { it.value == 0L }!!

    val groveCoord1 = mixer.returnValueIndexAfterValue(1000, zero)
    val groveCoord2 = mixer.returnValueIndexAfterValue(2000, zero)
    val groveCoord3 = mixer.returnValueIndexAfterValue(3000, zero)

    println(groveCoord1.value + groveCoord2.value + groveCoord3.value)
}
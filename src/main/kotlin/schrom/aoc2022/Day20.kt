package schrom.aoc2022

import java.io.File

class CircularMixer<T>(values: List<T>) {

    val size: Int = values.size
    private lateinit var startingNode: Node<T>

    init {
        var pred: Node<T>? = null
        values.forEachIndexed { index, t ->
            val newNode = Node(t)

            if (pred == null) {
                startingNode = newNode
            } else {
                pred!!.next = newNode
                newNode.prev = pred!!
            }

            pred = newNode

            if (index == values.lastIndex) {
                newNode.next = startingNode
                startingNode.prev = newNode
            }
        }
    }

    internal class Node<T>(
        var value: T,
    ) {
        lateinit var prev: Node<T>
        lateinit var next: Node<T>
    }

    fun returnValueIndexAfterValue(index: Int, value: T): T {
        val startNode = findValue(value)
        val realMoves = convertIndexToCircleMoves(index)
        var endNode = startNode
        repeat(realMoves) {
            endNode = endNode.next
        }
        return endNode.value
    }

    fun moveValueByAmount(value: T, amount: Long) {
        val moves = convertStepsToCircleMoves(amount)
        if (moves == 0) {
            return
        }
        val nodeToMove = findValue(value)

        var newNext: Node<T> = nodeToMove.next
        repeat(moves) {
            newNext = newNext.next
        }

        // unlink node
        nodeToMove.prev.next = nodeToMove.next
        nodeToMove.next.prev = nodeToMove.prev

        // link node to new position
        nodeToMove.prev = newNext.prev
        nodeToMove.next = newNext
        nodeToMove.prev.next = nodeToMove
        nodeToMove.next.prev = nodeToMove
    }

    private fun convertStepsToCircleMoves(steps: Long): Int {
        val remainder = steps % (this.size-1)
        return if (remainder < 0) {
            (remainder + this.size - 1).toInt()
        } else {
            remainder.toInt()
        }
    }

    private fun convertIndexToCircleMoves(index: Int): Int {
        val remainder = index % this.size
        return if (remainder < 0) {
            remainder + this.size -1
        } else {
            remainder
        }
    }

    private fun findValue(value: T): Node<T> {
        var e = startingNode
        while (e.value !== value) { // object reference !!!
            e = e.next
        }
        return e
    }
}

// want different objects for multiple numbers with same value
class LongHolder(val value: Long) {
    override fun toString(): String {
        return value.toString()
    }
}

fun main() {

    val data = File("data/day20").readLines()

    val ints = data.map { LongHolder(it.toLong()) }

    val mixer = CircularMixer(ints)

    ints.forEach {
        mixer.moveValueByAmount(it, it.value)
    }

    val zero = ints.findLast { it.value == 0L }!!

    val groveCoord1 = mixer.returnValueIndexAfterValue(1000, zero)
    val groveCoord2 = mixer.returnValueIndexAfterValue(2000, zero)
    val groveCoord3 = mixer.returnValueIndexAfterValue(3000, zero)

    println(groveCoord1.value + groveCoord2.value + groveCoord3.value)
}
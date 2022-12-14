package schrom.aoc2022

import java.io.File

open class Packet {

    enum class Decision { YES, NO, UNKNOWN }

    fun isOrderOK(right: Packet): Decision {
        val left = this

        if (left is IntPacket && right is IntPacket) {
            if (left.value < right.value) {
                return Decision.YES
            }
            if (left.value > right.value) {
                return Decision.NO
            }
        }

        if (left is ListPacket && right is ListPacket) {
            left.packets.forEachIndexed { index, packet ->
                // If the right list runs out of items first, the inputs are not in the right order.
                if (index == right.packets.size) {
                    return Decision.NO
                }
                val orderOK = packet.isOrderOK(right.packets[index])
                if (orderOK != Decision.UNKNOWN) {
                    return orderOK
                }
            }
            // If the left list runs out of items first, the inputs are in the right order.
            if (left.packets.size < right.packets.size) {
                return Decision.YES
            }
        }

        if (left is IntPacket && right is ListPacket) {
            return ListPacket().createFrom(left).isOrderOK(right)
        }

        if (left is ListPacket && right is IntPacket) {
            return left.isOrderOK(ListPacket().createFrom(right))
        }

        return Decision.UNKNOWN
    }

}

class ListPacket : Packet() {

    var packets = mutableListOf<Packet>()

    fun createFrom(intPacket: IntPacket): ListPacket {
        packets.add(intPacket)
        return this
    }

    fun createFrom(data: String) : Packet {
        return createFromData(data).first
    }

    private fun createFromData(data: String): Pair<Packet, String> {

        var toParse = data

        while (true) {

            if (toParse[0] == '[') {
                val createFrom = ListPacket().createFromData(toParse.substring(1))
                packets.add(createFrom.first)
                toParse = createFrom.second
            }

            if (toParse.isEmpty()) {
                break
            }

            val matchResult = Regex("""^\d+""").find(toParse)

            if (matchResult != null) {
                val number = matchResult.groups[0]!!.value
                packets.add(IntPacket(number.toInt()))
                toParse = toParse.substring(number.length)
            }

            if (toParse[0] == ',') {
                toParse = toParse.substring(1)
            }

            if (toParse[0] == ']') {
                return Pair(this, toParse.substring(1))
            }

        }

        return Pair(this.packets.first(), toParse)
    }

}

class IntPacket(val value: Int) : Packet()

fun main() {

    val data = File("data/day13").readLines().iterator()

    val packetPairs = mutableListOf<Pair<Packet, Packet>>()

    while (true) {

        packetPairs.add(
            Pair(
                ListPacket().createFrom(data.next()),
                ListPacket().createFrom(data.next()),
            )
        )

        if (data.hasNext()) {
            data.next() // skip blank line
        } else {
            break
        }
    }

    val sum = packetPairs
        .mapIndexed { index, pair ->
            if (pair.first.isOrderOK(pair.second) == Packet.Decision.YES) {
                index + 1
            } else {
                0
            }
        }.sum()

    println(sum)
}
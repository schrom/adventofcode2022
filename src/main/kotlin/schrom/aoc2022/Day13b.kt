package schrom.aoc2022

import java.io.File

class PacketSorter : Comparator<Packet> {
    override fun compare(o1: Packet?, o2: Packet?): Int {
        return when (o1!!.isOrderOK(o2!!)) {
            Packet.Decision.YES -> -1
            Packet.Decision.NO -> 1
            Packet.Decision.UNKNOWN -> 0
        }
    }
}

fun main() {

    val data = File("data/day13").readLines().iterator()

    val packets = mutableListOf<Packet>()

    val divider1 = ListPacket().createFrom("[[2]]")
    val divider2 = ListPacket().createFrom("[[6]]")
    packets.add(divider1)
    packets.add(divider2)

    while (data.hasNext()) {

        val line = data.next()
        if (line.isNotEmpty()) {
            packets.add(ListPacket().createFrom(line))
        }

    }

    packets.sortWith(PacketSorter())

    val product = packets
        .mapIndexed { index, packet ->
            if (packet === divider1 || packet === divider2) {
                index + 1
            } else {
                1
            }
        }
        .reduce { accumulator, element ->
            accumulator * element
        }

    println(product)
}
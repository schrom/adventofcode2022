package schrom.aoc2022

import java.io.File
import java.util.function.BiConsumer
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collector


class ListOfListCollector<T>(private val subSize : Int) : Collector<T, MutableList<List<T>>, List<List<T>>> {

    private var subList = mutableListOf<T>()

    override fun supplier() = Supplier { mutableListOf<List<T>>() }

    override fun accumulator() : BiConsumer<MutableList<List<T>>, T> = BiConsumer { t, u ->
        subList.add(u)
        if (subList.size == subSize) {
            t.add(subList)
            subList = mutableListOf()
        }
    }

    override fun combiner(): BinaryOperator<MutableList<List<T>>> = BinaryOperator { t, u ->
        t.addAll(u)
        t
    }

    override fun finisher(): Function<MutableList<List<T>>, List<List<T>>>  = Function { it }

    override fun characteristics(): MutableSet<Collector.Characteristics> {
        return mutableSetOf(Collector.Characteristics.IDENTITY_FINISH)
    }
}

fun main() {
    val data = File("data/day3").readLines()

    val sum =
        data
            .stream()
            .collect(ListOfListCollector(3))
            .map {
               it[0].toSet() intersect it[1].toSet() intersect it[2].toSet()
            }
            .sumOf {
                it.sumOf { c -> BackPackItem(c).getPriority() }
            }

    println(sum)
}
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        return Day01(input).part1()
    }

    fun part2(input: List<String>): Int {
        return Day01(input).part2()
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

class Day01(private val input: List<String>) {
    val groups
        get() = input.map { it.lineToPair() }.unzip()

    fun part1(): Int {
        val (firstGroup, secondGroup) = groups
            .let { it.first.sorted() to it.second.sorted() }

        return firstGroup.zip(secondGroup)
            .sumOf { abs(it.first - it.second) }
    }

    fun part2(): Int {
        val (firstGroup, secondGroup) = groups
        val secondGroupLocationsToOccurrences = secondGroup.groupingBy { it }.eachCount()

        return firstGroup.sumOf { it * secondGroupLocationsToOccurrences.getOrDefault(it, 0) }
    }

    private fun String.lineToPair() = split(" ").mapNotNull { it.toInteger() }.let { Pair(it[0], it[1]) }

    private fun String.toInteger(): Int? = runCatching { toInt() }.getOrNull()
}

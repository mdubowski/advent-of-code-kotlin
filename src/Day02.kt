import kotlin.math.abs
import kotlin.ranges.contains

fun main() {
    fun part1(input: List<String>): Int {
        return Day02(input).part1()
    }

    fun part2(input: List<String>): Int {
        return Day02(input).part2()
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

class Day02(input: List<String>) {
    private val reports = input.map { it.split(" ").map { it.toLong() } }

    fun part1(): Int = reports.count { it.isSafe() }

    fun part2(): Int = reports.count { it.isSafeWithDampener() }

    private fun List<Long>.isSafe(): Boolean {
        val levelChanges = getLevelChanges()
        val levelChangesAreSafe = levelChanges.all { abs(it) in 1..3 }
        return levelChangesAreSafe && (levelChanges.allPositive || levelChanges.allNegative)
    }

    private fun List<Long>.getLevelChanges() = windowed(2).map { it[1] - it[0] }

    private fun List<Long>.isSafeWithDampener(): Boolean {
        tailrec fun List<Long>.isSafeWithDampenerRec(ignoreIndex: Int? = null): Boolean {
            val reportToCheck = ignoreIndex?.let { getWithoutElement(ignoreIndex) } ?: this
            if (reportToCheck.isSafe()) return true

            return when (ignoreIndex) {
                null -> isSafeWithDampenerRec(0)
                size -> false
                else -> isSafeWithDampenerRec(ignoreIndex + 1)
            }
        }

        return isSafeWithDampenerRec()
    }

    private val List<Long>.allPositive: Boolean
        get() = all { it > 0 }

    private val List<Long>.allNegative: Boolean
        get() = all { it < 0 }

    private fun <T> List<T>.getWithoutElement(index: Int) = filterIndexed { i, _ -> i != index }
}

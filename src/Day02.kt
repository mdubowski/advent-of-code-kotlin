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

    fun part1(): Int = reports.count { report ->
        val levelChanges = report.levelChanges
        val levelChangesAreSafe = levelChanges.all { abs(it) in 1..3 }

        levelChangesAreSafe && (levelChanges.allPositive || levelChanges.allNegative)
    }

    fun part2(): Int = reports.count { it.isSafeWithDampener() }

    private fun List<Long>.isSafeWithDampener(): Boolean {
        tailrec fun List<Long>.isSafeWithDampenerRec(ignoreIndex: Int? = null): Boolean {
            val reportToCheck = ignoreIndex?.let { getWithoutElement(ignoreIndex) } ?: this

            val levelChanges = reportToCheck.levelChanges
            val levelChangesAreSafe = levelChanges.all { abs(it) in 1..3 }

            return when {
                levelChangesAreSafe && (levelChanges.allPositive || levelChanges.allNegative) -> true
                ignoreIndex == null -> isSafeWithDampenerRec(0)
                ignoreIndex == size -> false
                else -> isSafeWithDampenerRec(ignoreIndex + 1)
            }
        }

        return isSafeWithDampenerRec()
    }

    private val List<Long>.levelChanges
        get() = windowed(2).map { it[1] - it[0] }

    private val List<Long>.allPositive: Boolean
        get() = all { it > 0 }

    private val List<Long>.allNegative: Boolean
        get() = all { it < 0 }

    private fun <T> List<T>.getWithoutElement(index: Int) = filterIndexed { i, _ -> i != index }
}

typealias Cache = MutableMap<Stone, MutableMap<Int, Long>>

fun main() {
    fun part1(input: List<String>): Long {
        return Day11(input).part1()
    }

    fun part2(input: List<String>): Long {
        return Day11(input).part2()
    }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

@JvmInline
value class Stone(val number: Long) {
    fun change(): List<Stone> = when {
        number == 0L -> listOf(Stone(1))
        number.toString().length % 2 == 0 -> number.toString().let {
            val length = it.length
            listOf(
                Stone(it.substring(0, length / 2).toLong()),
                Stone(it.substring(length / 2, length ).toLong()),
            )
        }
        else -> listOf(Stone(number * 2024))
    }
}

class Day11(input: List<String>) {
    private val cache: Cache = mutableMapOf<Stone, MutableMap<Int, Long>>()
    private val stones: List<Stone> = input.first().split(" ").map { Stone(it.toLong()) }

    fun part1(): Long = stones.sumOf { count(it, 25) }

    fun part2(): Long = stones.sumOf { count(it, 75) }

    private fun count(stone: Stone, maxIterations: Int): Long {
        fun countRec(stone: Stone, counter: Int): Long {
            if (counter > maxIterations) {
                return 1
            }

            return cache
                .getOrPut(stone) { mutableMapOf() }
                .getOrPut(counter) {
                    val newStones = stone.change()
                    when (newStones.size) {
                        1 -> {
                            countRec(newStones.first(), counter + 1)
                        }
                        2 -> {
                            val (first, second) = newStones
                            countRec(first, counter + 1) + countRec(second, counter + 1)
                        }
                        else -> error("an error occurred")
                    }
                }
        }

        return countRec(stone, 1)
    }
}

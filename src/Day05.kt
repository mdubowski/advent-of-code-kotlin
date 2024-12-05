fun main() {
    fun part1(input: List<String>): Int {
        return Day05(input).part1()
    }

    fun part2(input: List<String>): Int {
        return Day05(input).part2()
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

class Day05(input: List<String>) {
    private val pages: List<List<String>>
    private val numberToLowerOrderMap: Map<String, List<String>>

    init {
        val (rules, pages) = input.partition { it.contains("|") }
            .let { (first, second) -> first to second.filter { !it.isEmpty() } }
        this.pages = pages.map { it.split(",") }
        this.numberToLowerOrderMap = rules.asSequence()
            .map { it.split("|") }
            .map { it[0] to it[1] }
            .groupingBy { it.first }
            .fold(emptyList()) { acc, element -> acc + element.second }
    }

    fun part1(): Int = pages.sumOf { if (it == sorted(it)) it.middleNumber else 0 }

    fun part2(): Int = pages.sumOf {
        val sorted = sorted(it)
        if (it == sorted) 0 else sorted.middleNumber
    }

    private val List<String>.middleNumber
        get() = get(size / 2).toInt()

    private fun sorted(pages: List<String>) = buildList<String> {
        fun String.shouldBeBefore(anotherNumber: String): Boolean {
            val lowerOrderNumbers = numberToLowerOrderMap[this] ?: emptyList()
            return lowerOrderNumbers.contains(anotherNumber)
        }

        tailrec fun sorted(pages: List<String>) {
            if (pages.isEmpty()) return
            val (head, tail) = pages.headAndTail
            if (tail.any { it.shouldBeBefore(head) }) {
                sorted(tail + head)
            } else {
                add(head)
                sorted(tail)
            }
        }

        sorted(pages)
    }

    private val <T> List<T>.headAndTail: Pair<T, List<T>>
        get() = first() to drop(1)
}

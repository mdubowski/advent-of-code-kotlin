fun main() {
    fun part1(input: List<String>): Long {
        return Day07(input).part1()
    }

    fun part2(input: List<String>): Long {
        return Day07(input).part2()
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

data class Equation(val testValue: Long, val numbers: List<Long>)

enum class Operator {
    Add, Mul, Con
}

class Day07(input: List<String>) {
    private val equations: List<Equation> = input.asSequence()
        .map { it.split(":") }
        .map { Equation(it[0].toLong(), it[1].trim().split(" ").map { it.toLong() }) }.toList()

    fun part1(): Long = equations.filter { checkCorrectness(it) }.sumOf { it.testValue }

    fun part2(): Long = equations.filter { checkCorrectnessWithCon(it) }.sumOf { it.testValue }

    private fun checkCorrectness(equation: Equation): Boolean {
        fun check(remainingNumbers: List<Long>, operator: Operator, acc: Long): Boolean {
            if (remainingNumbers.isEmpty()) {
                return acc == equation.testValue
            }
            val (first, withoutFirst) = remainingNumbers.headAndTail
            val newAcc = when (operator) {
                Operator.Mul -> acc * first
                Operator.Add -> acc + first
                else -> error("unsupported operator $operator")
            }

            if (newAcc > equation.testValue) {
                return false
            }

            return check(withoutFirst, Operator.Add, newAcc) || check(withoutFirst, Operator.Mul, newAcc)
        }

        return check(equation.numbers, Operator.Add, 0) || check(equation.numbers, Operator.Mul, 0)
    }

    private fun checkCorrectnessWithCon(equation: Equation): Boolean {
        fun check(remainingNumbers: List<Long>, operator: Operator, acc: Long): Boolean {
            if (remainingNumbers.isEmpty()) {
                return acc == equation.testValue
            }
            val (first, withoutFirst) = remainingNumbers.headAndTail
            val newAcc = when (operator) {
                Operator.Mul -> acc * first
                Operator.Add -> acc + first
                Operator.Con -> "$acc$first".toLong()
            }

            if (newAcc > equation.testValue) {
                return false
            }

            return check(withoutFirst, Operator.Add, newAcc) ||
                    check(withoutFirst, Operator.Mul, newAcc) ||
                    check(withoutFirst, Operator.Con, newAcc)
        }

        return check(equation.numbers, Operator.Add, 0) ||
                check(equation.numbers, Operator.Mul, 0) ||
                check(equation.numbers, Operator.Con, 0)
    }

    private val <T> List<T>.headAndTail: Pair<T, List<T>>
        get() = first() to drop(1)
}

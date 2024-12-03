fun main() {
    fun part1(input: List<String>): Int {
        return Day03(input).part1()
    }

    fun part2(input: List<String>): Int {
        return Day03(input).part2()
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

class Day03(private val input: List<String>) {
    fun part1(): Int = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
        .findAll(input.joinToString())
        .map { it.groupValues.toMulInstruction() }
        .let { execute(it.toList()) }

    fun part2(): Int = """mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""".toRegex()
        .findAll(input.joinToString())
        .map { it.groupValues.toInstruction() }
        .let { execute(it.toList()) }

    private fun List<String>.toMulInstruction() = Instruction.Mul(get(1).toInt(), get(2).toInt())

    private fun List<String>.toInstruction(): Instruction = when (first())  {
        "do()" -> Instruction.Do
        "don't()" -> Instruction.Dont
        else -> toMulInstruction()
    }

    private fun execute(instructions: List<Instruction>): Int {
        tailrec fun executeRec(instructions: List<Instruction>, ignoreNext: Boolean = false, sum: Int = 0): Int = when {
            instructions.isEmpty() -> sum
            else -> when (val instruction = instructions.first()) {
                is Instruction.Mul -> executeRec(instructions.drop(1), ignoreNext, if (ignoreNext) sum else sum + instruction.result)
                is Instruction.Do -> executeRec(instructions.drop(1), ignoreNext = false, sum)
                is Instruction.Dont -> executeRec(instructions.drop(1), ignoreNext = true, sum)
            }
        }

        return executeRec(instructions)
    }
}

sealed interface Instruction {
    data object Do : Instruction
    data object Dont : Instruction
    data class Mul(val a: Int, val b: Int) : Instruction {
        val result = a * b
    }
}

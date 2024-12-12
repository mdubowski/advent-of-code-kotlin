import kotlin.time.measureTimedValue

class Day<I>(day: Int, private val test: Boolean = false) {
    private val day = day.toString().padStart(2, '0')

    operator fun invoke(inputPreparer: (List<String>) -> I, firstPart: (I) -> String, secondPart: (I) -> String) {
        println("Results of day $day:")
        val input = readInput("Day$day${if (test) "_test" else ""}")
        val (preparedInput, inputTime) = measureTimedValue { inputPreparer(input) }

        val (firstValue, firstValueTime) = measureTimedValue { firstPart(preparedInput) }
        val firstValueTimeWithInputTime = (firstValueTime + inputTime).inWholeMilliseconds
        println("First part result: $firstValue, calculated in $firstValueTimeWithInputTime ms")

        val (secondValue, secondValueTime) = measureTimedValue { secondPart(preparedInput) }
        val secondValueTimeWithInputTime = (secondValueTime + inputTime).inWholeMilliseconds
        println("Second part result: $secondValue, calculated in $secondValueTimeWithInputTime ms")
    }
}

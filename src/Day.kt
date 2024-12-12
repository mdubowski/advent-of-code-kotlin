import kotlin.time.measureTimedValue

fun <I> day(initializer: DayScope<I>.() -> Unit) {
    val dayScope = DayScope<I>()
    dayScope.initializer()

    val day = dayScope.day
    requireNotNull(day) { "Day number must be provided" }

    val inputPreparer = dayScope.inputPreparer
    requireNotNull(inputPreparer) { "Input preparer must be provided" }

    val firstPart = dayScope.firstPart
    requireNotNull(firstPart) { "Input preparer must be provided" }

    val secondPart = dayScope.secondPart
    requireNotNull(secondPart) { "Input preparer must be provided" }

    println("Results of day $day:")

    val input = readInput("Day$day${if (dayScope.test) "_test" else ""}")
    val (preparedInput, inputTime) = measureTimedValue { inputPreparer(input) }

    val (firstValue, firstValueTime) = measureTimedValue { firstPart(preparedInput) }
    val firstValueTimeWithInputTime = (firstValueTime + inputTime).inWholeMilliseconds
    println("First part result: $firstValue, calculated in $firstValueTimeWithInputTime ms")

    val (secondValue, secondValueTime) = measureTimedValue { secondPart(preparedInput) }
    val secondValueTimeWithInputTime = (secondValueTime + inputTime).inWholeMilliseconds
    println("Second part result: $secondValue, calculated in $secondValueTimeWithInputTime ms")
}

class DayScope<I> {
    var day: Int? = null
    var test: Boolean = false
    var inputPreparer: ((List<String>) -> I)? = null
    var firstPart: ((I) -> String)? = null
    var secondPart: ((I) -> String)? = null

    fun inputPreparer(action: (List<String>) -> I) {
        inputPreparer = action
    }

    fun firstPart(action: (I) -> String) {
        firstPart = action
    }

    fun secondPart(action: (I) -> String) {
        secondPart = action
    }
}

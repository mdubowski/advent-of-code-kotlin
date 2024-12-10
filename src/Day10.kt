fun main() {
    fun part1(input: List<String>): Int {
        return Day10(input).part1()
    }

    fun part2(input: List<String>): Int {
        return Day10(input).part2()
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

class Day10(input: List<String>) {
    private val map: TopographicMap = input
        .mapIndexed { y, line ->
            line.mapIndexed { x, score ->
                Position(score.digitToInt(), Coordinate(x, y))
            }
        }.let { TopographicMap(it) }

    fun part1(): Int = map.trailheads
        .map { it.findTrails(map) }
        .map { it.distinctBy { it.last() } }
        .sumOf { it.size }

    fun part2(): Int = map.trailheads
        .map { it.findTrails(map) }
        .sumOf { it.size }

    private fun Position.findTrails(map: TopographicMap): List<List<Coordinate>> = buildList<List<Coordinate>> {
        fun findTrailsRec(coordinate: Coordinate, level: Int, trail: List<Coordinate>) {
            if (level > 9) {
                add(trail)
                return
            }

            map.getPositionsAround(coordinate)
                .filter { it.score == level }
                .forEach { findTrailsRec(it.coordinate, level + 1, trail + it.coordinate) }
        }

        findTrailsRec(coordinate, score + 1, listOf(coordinate))
    }
}

private data class TopographicMap(
    private val map: List<List<Position>>,
) {
    val xLength = map.first().size
    val yLength = map.size
    val trailheads = map.flatMap { it.filter { it.score == 0 } }

    fun getPositionsAround(coordinate: Coordinate): List<Position> {
        val (x, y) = coordinate
        return sequenceOf(
            Coordinate(x, y - 1),
            Coordinate(x + 1, y),
            Coordinate(x, y + 1),
            Coordinate(x - 1, y),
        )
            .filter { (x, _) -> x >= 0 && x < xLength }
            .filter { (_, y) -> y >= 0 && y < yLength }
            .map { (x, y) -> map[y][x] }
            .toList()
    }
}

private data class Position(val score: Int, val coordinate: Coordinate)

import kotlin.collections.Map
import kotlin.math.abs

fun main() = day {
    day = 12
    inputPreparer { lines ->
        buildMap {
            lines.mapIndexed { y, yLine ->
                yLine.mapIndexed { x, plant ->
                    set(Coordinate(x, y), Plant(plant))
                }
            }
        }.let { findPlantAreas(it) }
    }
    firstPart { areas ->
        areas.sumOf { it.fencingCostByPerimeter() }.toString()
    }
    secondPart { areas ->
        areas.sumOf { it.fencingCostBySides() }.toString()
    }
}

@JvmInline
value class Plant(val name: Char)

data class PlantArea(val plants: List<Coordinate>) {

    fun fencingCostByPerimeter(): Int = perimeter() * plants.size

    fun fencingCostBySides(): Int = sides() * plants.size

    private fun perimeter(): Int = plants.sumOf { it.perimeter() }

    private fun sides(): Int {
        val sides = plants.flatMap { it.sides() }

        val ups = sides.filter { it.direction == Direction.Up }
        val downs = sides.filter { it.direction == Direction.Down }
        val lefts = sides.filter { it.direction == Direction.Left }
        val rights = sides.filter { it.direction == Direction.Right }

        return ups.countHorizontal() + downs.countHorizontal() + lefts.countVertical() + rights.countVertical()
    }

    private fun List<Side>.countHorizontal(): Int = groupBy { side -> side.coordinate.y }
        .values
        .sumOf { sides ->
            sides.asSequence()
                .sortedBy { side -> side.coordinate.x }
                .zipWithNext()
                .map { (a, b) ->
                    val difference = abs(a.coordinate.x - b.coordinate.x)
                    if (difference > 1) 1 else 0
                }.sum() + 1
        }

    private fun List<Side>.countVertical(): Int = groupBy { side -> side.coordinate.x }
        .values
        .sumOf { sides ->
            sides.asSequence()
                .sortedBy { side -> side.coordinate.y }
                .zipWithNext()
                .map { (a, b) ->
                    val difference = abs(a.coordinate.y - b.coordinate.y)
                    if (difference > 1) 1 else 0
                }.sum() + 1
        }

    private fun Coordinate.sides(): List<Side> = buildList {
        val (up, right, down, left) = getPositionsAround()
        if (up !in plants) {
            add(Side(Direction.Up, this@sides))
        }
        if (down !in plants) {
            add(Side(Direction.Down, this@sides))
        }
        if (left !in plants) {
            add(Side(Direction.Left, this@sides))
        }
        if (right !in plants) {
            add(Side(Direction.Right, this@sides))
        }
    }

    private fun Coordinate.perimeter(): Int = 4 - getPositionsAround().count { it in plants }
}

data class Side(val direction: Direction, val coordinate: Coordinate)

private fun findPlantAreas(plantsMap: Map<Coordinate, Plant>): List<PlantArea> = buildList {
    tailrec fun findPlantAreasRec(restOfTheMap: Map<Coordinate, Plant>) {
        if (restOfTheMap.isEmpty()) {
            return
        }

        val nextCoordinate = restOfTheMap.keys.first()

        val plantAreaCoordinates = findPlantArea(nextCoordinate, restOfTheMap)
        add(PlantArea(plantAreaCoordinates))

        val newMap = restOfTheMap.filterKeys { it !in plantAreaCoordinates }
        findPlantAreasRec(newMap)
    }

    findPlantAreasRec(plantsMap)
}

private fun findPlantArea(startingPoint: Coordinate, plantsMap: Map<Coordinate, Plant>) = buildList<Coordinate> {
    fun findPlantAreaRec(point: Coordinate, plant: Plant) {
        if (contains(point)) {
            return
        }

        val plantFromMap = plantsMap[point]
        when {
            plantFromMap == null -> return
            plantFromMap != plant -> return
        }

        add(point)
        point.getPositionsAround().forEach { findPlantAreaRec(it, plant) }
    }

    val plantFromStartingPoint = plantsMap[startingPoint]
    if (plantFromStartingPoint == null) {
        return@buildList
    }

    findPlantAreaRec(startingPoint, plantFromStartingPoint)
}

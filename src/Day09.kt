fun main() {
    fun part1(input: List<String>): Long {
        return Day09(input).part1()
    }

    fun part2(input: List<String>): Int {
        return Day09(input).part2()
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

sealed interface DiskBlock {
    object FreeSpace : DiskBlock
    data class FileBlock(val number: Int) : DiskBlock
}

class Day09(private val input: List<String>) {
    fun part1(): Long {
        val disk = buildList<DiskBlock> {
            (input.first() + '0').asSequence().windowed(2, 2).map { it[0] to it[1] }
                .forEachIndexed { index, (file, space) ->
                    (0 until file.digitToInt()).forEach { add(DiskBlock.FileBlock(index)) }
                    (0 until space.digitToInt()).forEach { add(DiskBlock.FreeSpace) }
                }
        }

        val processedDisk = process(disk)
        return processedDisk.calculateChecksum()
    }

    private fun List<DiskBlock>.calculateChecksum() = mapIndexed { blockId, block ->
        when (block) {
            is DiskBlock.FreeSpace -> 0
            is DiskBlock.FileBlock -> block.number.toLong() * blockId.toLong()
        }
    }.sum()

    private fun process(str: List<DiskBlock>): List<DiskBlock> {
        tailrec fun processRec(str: List<DiskBlock>): List<DiskBlock> {
            if (!str.contains(DiskBlock.FreeSpace)) {
                return str
            }
            val last = str.last()
            if (last == DiskBlock.FreeSpace) {
                return processRec(str.dropLast(1))
            }

            val firstEmptySpaceIndex = str.indexOfFirst { it == DiskBlock.FreeSpace }
            val (firstPart, secondPart) = str.subList(0, firstEmptySpaceIndex) to str.subList(
                firstEmptySpaceIndex + 1,
                str.size
            )
            val newDisk = firstPart + last + secondPart.dropLast(1)

            return processRec(newDisk)
        }

        return processRec(str)
    }

    fun part2(): Int {
        TODO()
    }
}

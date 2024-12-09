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

    private fun process(disk: List<DiskBlock>): List<DiskBlock> {
        tailrec fun processRec(disk: List<DiskBlock>): List<DiskBlock> {
            if (!disk.contains(DiskBlock.FreeSpace)) {
                return disk
            }
            val last = disk.last()
            if (last == DiskBlock.FreeSpace) {
                return processRec(disk.dropLast(1))
            }

            val firstEmptySpaceIndex = disk.indexOfFirst { it == DiskBlock.FreeSpace }
            val (firstPart, secondPart) = disk.subList(0, firstEmptySpaceIndex) to disk.subList(
                firstEmptySpaceIndex + 1,
                disk.size
            )
            val newDisk = firstPart + last + secondPart.dropLast(1)

            return processRec(newDisk)
        }

        return processRec(disk)
    }

    fun part2(): Int {
        TODO()
    }
}

package advent

import tools.ResourceReader

fun main() {
    val input = ResourceReader("day8.txt").useLines { seq ->
        seq.map { it }.toList()
    }.orEmpty()

    val columnRange = 0..<input.first().length
    val lineRange = input.indices
    val nodes = mutableSetOf<Antenna>()

    val antennaSet = input.flatMapIndexed { lineIndex, line ->
        line.mapIndexed { columnIndex, frequence ->
            Antenna(frequence, lineIndex, columnIndex).takeIf { frequence != '.' }
        }
    }.filterNotNull().groupBy { it.frequence }


    antennaSet.forEach { (_, antennas) ->
        if(antennas.size > 1) {
            antennas.forEachIndexed { index, antenna ->
                nodes.add(antenna.copy(frequence = '#'))
                //Start for the current antenna to the next antenanna
                antennas.drop(index + 1).forEach { nextAntenna ->
                    nodes.addAll(antenna.createNodes(nextAntenna, lineRange, columnRange, 1))
                    nodes.addAll(antenna.createNodes(nextAntenna, lineRange, columnRange, -1))
                }
            }
        }
    }

    //Day 8 - Part 1 result
    println(nodes.size)

}

private fun Antenna.createNodes(nextAntenna: Antenna, lineRange: IntRange, columnRange: IntRange, orientation: Int): List<Antenna> {
    val diffLine = nextAntenna.line - line
    val diffColumn = nextAntenna.column - column
    var loop = 1
    val nodes = mutableListOf<Antenna>()
    do {
        val node = Antenna(
            frequence = '#',
            line = line + diffLine * loop * orientation,
            column = column + diffColumn * loop * orientation
        )
        if(node.line in lineRange && node.column in columnRange) {
            nodes.add(node)
            loop++
        } else {
            loop = 0
        }
    } while (loop > 0)
    return nodes
}

private data class Antenna(val frequence: Char, val line: Int, val column: Int)

package advent

import tools.ResourceReader
import kotlin.system.measureTimeMillis

fun main() {
    val inputMap = ResourceReader("day12.txt").useLines { seq ->
        seq.map { line ->
            line.map { it }
        }.toList()
    }.orEmpty()

    measureTimeMillis {
        val fields = mutableListOf<Field>()
        inputMap.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { columnIndex, fieldName ->
                val position = Position(lineIndex, columnIndex)
                if (fields.none { it.positions.contains(position) }) {
                    fields.add(Field(fieldName, emptyList()).extend(lineIndex, columnIndex, inputMap))
                }
            }
        }

        //Day 12 - Part 1
        println(fields.sumOf { it.fences * it.size })

        //Day 12 - Part 2
        println(fields.sumOf { it.sides * it.size })


    }.let { println("Execution time : $it ms") }

}

private fun Field.extend(line: Int, column: Int, maps: List<List<Char>>): Field =
    if (line !in maps.indices || column !in maps[0].indices || maps[line][column] != name || this.positions.contains(
            Position(line, column)
        )
    ) {
        this
    } else {
        copy(positions = positions + listOf(Position(line, column)))
            .extend(line + 1, column, maps)
            .extend(line - 1, column, maps)
            .extend(line, column + 1, maps)
            .extend(line, column - 1, maps)
    }

private data class Field(val name: Char, val positions: List<Position>)

private data class Position(val line: Int, val column: Int)

private val Field.size
    get() = positions.size

private val Field.fences
    get() =
        positions.sumOf { position ->
            listOfNotNull(
                position.copy(line = position.line - 1).takeIf { !positions.contains(it) },
                position.copy(line = position.line + 1).takeIf { !positions.contains(it) },
                position.copy(column = position.column - 1).takeIf { !positions.contains(it) },
                position.copy(column = position.column + 1).takeIf { !positions.contains(it) }
            ).size
        }

private val Field.sides: Int
    get() {
        data class Fence(val line: Int, val column: Int, val orientation: Orientation)

        val fences = mutableSetOf<Fence>()
        var total = 0
        positions
            .sortedBy { it.column }.sortedBy { it.line } //Sort in right order to construct the field
            .forEach { position ->
                //Check if is't empty on the TOP
                position.copy(line = position.line - 1).takeIf { !positions.contains(it) }?.let {
                    with(Fence(position.line, position.column, Orientation.NORTH)) {
                        fences.add(this) //Add the TOP fence and check no
                        if (!fences.contains(copy(column = column - 1))) total++ //Chek if a previous fence in same orientation exist
                    }
                }
                position.copy(line = position.line + 1).takeIf { !positions.contains(it) }?.let {
                    with(Fence(position.line, position.column, Orientation.SOUTH)) {
                        fences.add(this)
                        if (!fences.contains(copy(column = column - 1))) total++ //Chek if a previous fence in same orientation exist
                    }
                }
                position.copy(column = position.column - 1).takeIf { !positions.contains(it) }?.let {
                    with(Fence(position.line, position.column, Orientation.EST)) {
                        fences.add(this)
                        if (!fences.contains(copy(line = line - 1))) total++ //Chek if a previous fence in same orientation exist
                    }
                }
                position.copy(column = position.column + 1).takeIf { !positions.contains(it) }?.let {
                    with(Fence(position.line, position.column, Orientation.WEST)) {
                        fences.add(this)
                        if (!fences.contains(copy(line = line - 1))) total++ //Chek if a previous fence in same orientation exist
                    }
                }
            }
        return total
    }

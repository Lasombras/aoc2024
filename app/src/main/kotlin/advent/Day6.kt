package advent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import tools.ResourceReader


fun main() {
    val mapInput = ResourceReader("day6.txt").useLines { seq ->
        seq.map { line ->
            line.toCharArray()
        }.toList()
    }.orEmpty()

    val line = mapInput.indexOfFirst { it.contains('^') }
        val column = mapInput[line].indexOf('^')
        val maxColumn = mapInput[0].size - 1
        val maxLine = mapInput.size - 1
        val guardStart = Guard(line, column, Orientation.NORTH)

    run {
        var guard = guardStart
        val positions = emptySet<Pair<Int, Int>>().toMutableSet()
        var exit = false
        do {
            positions.add(guard.line to guard.column)
            val nextPosition = guard.move()
            if(nextPosition.column in 0..maxColumn && nextPosition.line in 0..maxLine) {
                guard = if(mapInput[nextPosition.line][nextPosition.column] == '#') {
                    guard.rotate()
                } else {
                    nextPosition
                }
            } else {
                exit = true
            }
        } while (exit.not())
        //Day 6 - Part 1 result
        println(positions.size)
    }

    runBlocking(Dispatchers.Unconfined) {
        (0..maxLine).flatMap { line ->
            (0..maxColumn).map { column ->
                async { if(checkGuardLoop(mapInput, maxLine, maxColumn, guardStart, line, column )) 1 else 0 }
            }
        }.sumOf { it.await() }
        .let {
            //Day 6 - Part 2 result
            println(it)
        }
    }

}

private fun checkGuardLoop(mapInput: List<CharArray>, maxLine: Int, maxColumn: Int, guardStart: Guard, line: Int, column: Int): Boolean =
    if(mapInput[line][column] == '#' || (line == guardStart.line && guardStart.column == column)) {
        false
    } else {
        val positions = emptySet<Guard>().toMutableSet()
        var guard = guardStart
        var loop: Boolean? = null
        do {
            if(!positions.add(guard)) {
                loop = true
            } else {
                val nextPosition = guard.move()
                if(nextPosition.column in 0..maxColumn && nextPosition.line in 0..maxLine) {
                    guard = if(mapInput[nextPosition.line][nextPosition.column] == '#' || (nextPosition.line == line && nextPosition.column == column)) {
                        guard.rotate()
                    } else {
                        nextPosition
                    }
                } else {
                    loop = false
                }
            }
        } while (loop == null)
        loop
    }


private data class Guard(
    val line: Int,
    val column: Int,
    val orientation: Orientation
)

private fun Guard.move(): Guard =
    when(orientation) {
        Orientation.NORTH -> copy(line = line - 1)
        Orientation.NORTH_EST -> copy(line = line - 1, column = column + 1)
        Orientation.EST -> copy(column = column + 1)
        Orientation.SOUTH_EST -> copy(line = line + 1, column = column + 1)
        Orientation.SOUTH -> copy(line = line + 1)
        Orientation.SOUTH_WEST -> copy(line = line + 1, column = column - 1)
        Orientation.WEST -> copy(column = column - 1)
        Orientation.NORTH_WEST -> copy(line = line - 1, column = column - 1)
    }

private fun Guard.rotate(): Guard =
    when(orientation) {
        Orientation.NORTH -> copy(orientation = Orientation.EST)
        Orientation.NORTH_EST -> copy(orientation = Orientation.SOUTH_EST)
        Orientation.EST -> copy(orientation = Orientation.SOUTH)
        Orientation.SOUTH_EST -> copy(orientation = Orientation.SOUTH_WEST)
        Orientation.SOUTH -> copy(orientation = Orientation.WEST)
        Orientation.SOUTH_WEST -> copy(orientation = Orientation.NORTH_WEST)
        Orientation.WEST -> copy(orientation = Orientation.NORTH)
        Orientation.NORTH_WEST -> copy(orientation = Orientation.NORTH_EST)

    }
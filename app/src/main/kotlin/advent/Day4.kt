package advent

import tools.ResourceReader


private const val SEARCH_WORD = "XMAS"
fun main() {
    val input = ResourceReader("day4.txt").useLines {
        it.toList()
    }.orEmpty()

    input.mapIndexed { row, sentence ->
        sentence.mapIndexed { column, _ ->
            Orientation.entries.sumOf {
                if (input.search(SEARCH_WORD, column, row, it)) 1L else 0L
            }
        }.sum()
    }.sum()
        .let {
            //Day 4 - Part 1 result
            println(it)
        }

    input.mapIndexed { row, sentence ->
        sentence.mapIndexed { column, _ ->
            if (input.searchMass(column, row)) 1L else 0L
        }.sum()
    }.sum()
        .let {
            //Day 4 - Part 2 result
            println(it)
        }
}


private fun List<String>.search(
    word: String,
    column: Int,
    row: Int,
    orientation: Orientation
): Boolean {
    //Check end of word
    if (word.isEmpty())
        return true
    //Check outside
    if (row < 0 || column < 0 || row >= size || column >= this[row].length)
        return false
    val letter = word.first()
    //Check don't match
    if (this[row][column] != letter)
        return false

    val nextSearch = word.drop(1)
    return when (orientation) {
        Orientation.NORTH -> search(nextSearch, column, row - 1, orientation)
        Orientation.NORTH_EST -> search(nextSearch, column + 1, row - 1, orientation)
        Orientation.EST -> search(nextSearch, column + 1, row, orientation)
        Orientation.SOUTH_EST -> search(nextSearch, column + 1, row + 1, orientation)
        Orientation.SOUTH -> search(nextSearch, column, row + 1, orientation)
        Orientation.SOUTH_WEST -> search(nextSearch, column - 1, row + 1, orientation)
        Orientation.WEST -> search(nextSearch, column - 1, row, orientation)
        Orientation.NORTH_WEST -> search(nextSearch, column - 1, row - 1, orientation)
    }
}

private fun List<String>.searchMass(
    column: Int,
    row: Int,
): Boolean {
    //Check A match
    if (this[row][column] != 'A')
        return false

    //Check X size
    if (row - 1 < 0 || row + 1 >= this.size || column - 1 < 0 || column + 1 >= this[row].length)
        return false

    //Check first MAS
    return if (
        (this[row - 1][column - 1] == 'M' && this[row + 1][column + 1] == 'S') ||
        (this[row - 1][column - 1] == 'S' && this[row + 1][column + 1] == 'M')
    ) {
        //Check second MAS
        (this[row + 1][column - 1] == 'M' && this[row - 1][column + 1] == 'S') ||
                (this[row + 1][column - 1] == 'S' && this[row - 1][column + 1] == 'M')
    } else {
        false
    }
}

enum class Orientation {
    NORTH,
    NORTH_EST,
    EST,
    SOUTH_EST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST
}


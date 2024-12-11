package advent

import tools.ResourceReader

fun main() {
    val inputMap = ResourceReader("day10.txt").useLines { seq ->
        seq.map { line -> line.map { it.toString().toInt() }.toIntArray() }.toList().toTypedArray()
    }.orEmpty()


    inputMap.flatMapIndexed { y, line ->
         line.mapIndexed { x, item ->
             if(item == 0) inputMap.climb(x, y, -1) else emptyList()
        }
    }.let { trailheads ->
        //Day 10 - Part 1 result
        println(trailheads.sumOf { it.distinct().size })
        //Day 10 - Part 2 result
        println(trailheads.sumOf { it.size })
    }
}

//retrieve a list of valid terminal point (9)
private fun Array<out IntArray>.climb(x: Int, y: Int, previous: Int): List<Pair<Int, Int>> {
    if(x < 0 || x >= first().size || y < 0 || y >= size)
        return emptyList()
    val value = this[y][x]
    if(value - previous != 1)
        return emptyList()
    if(value == 9)
        return listOf(x to y)
    return climb(x + 1, y, value) +
           climb(x - 1, y, value) +
           climb(x , y + 1, value) +
           climb(x, y - 1, value)
}
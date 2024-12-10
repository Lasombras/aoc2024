package advent

import tools.ResourceReader

fun main() {
    val inputMap = ResourceReader("day10.txt").useLines { seq ->
        seq.map { line -> line.map { it.toString().toInt() }.toIntArray() }.toList().toTypedArray()
    }.orEmpty()


    inputMap.foldIndexed(0) { y, total, line ->
        total + line.foldIndexed(0) { x, columnTotal, item ->
            columnTotal + if(item == 0) { inputMap.moveDistinct(x, y, -1).size } else { 0 }
        }
    }.let {
        //Day 10 - Part 1 result
        println(it)
    }

    inputMap.foldIndexed(0) { y, total, line ->
        total + line.foldIndexed(0) { x, columnTotal, item ->
            columnTotal + if(item == 0) { inputMap.move(x, y, -1) } else { 0 }
        }
    }.let {
        //Day 10 - Part 1 result
        println(it)
    }

}

private fun Array<out IntArray>.moveDistinct(x: Int, y: Int, previous: Int): Set<Pair<Int, Int>> {
    if(x < 0 || x >= first().size || y < 0 || y >= size)
        return emptySet()
    val value = this[y][x]
    if(value - previous != 1)
        return emptySet()
    if(value == 9)
        return setOf(x to y)
    return moveDistinct(x + 1, y, value) + moveDistinct(x - 1, y, value) + moveDistinct(x , y + 1, value) + moveDistinct(x, y - 1, value)

}

private fun Array<out IntArray>.move(x: Int, y: Int, previous: Int): Int{
    if(x < 0 || x >= first().size || y < 0 || y >= size)
        return 0
    val value = this[y][x]
    if(value - previous != 1)
        return 0
    if(value == 9)
        return 1
    return move(x + 1, y, value) + move(x - 1, y, value) + move(x , y + 1, value) + move(x, y - 1, value)

}
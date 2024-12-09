package advent

import tools.ResourceReader

fun main() {
    val line = ResourceReader("day9.txt").useLines { seq ->
        seq.map { it }.toList()
    }?.firstOrNull().orEmpty()

    val file = line.mapIndexed { index, value ->
        Array("$value".toInt()) {if (index % 2 == 0) "${index / 2}" else "." }
    }.reduce { acc, it -> acc + it}

    //Day 9 - Part 1 result
    with(file.compress()) {
        println(checkSum())
    }

    //Day 9 - Part 2 result
    with(file.compressAlt()) {
        println(checkSum())
    }

}

private fun Array<String>.compress(): Array<String> {
    val data = toMutableList()
    for(i in data.indices) {
        val digit = data[i]
        if(digit == ".") {
            val validDigitIndex = data.indexOfLast { it != "."}
            if(validDigitIndex > i) {
                data[i] = data[validDigitIndex]
                data[validDigitIndex] = "."
            }
        }
    }
    return data.toTypedArray()
}

private fun Array<String>.compressAlt(): Array<String> {
    val data = toMutableList()
    var currentBlock = ""
    for(i in data.size-1 downTo 0) {
        val digit = data[i]
        if(digit != "." && currentBlock != digit) {
            currentBlock = digit
            //Identify the block size
            val firstIndex = data.indexOfFirst { it == digit }
            val diff = i - firstIndex
            //Identify the valid empty space
            val indexOfSpace = data.joinToString("") { if (it != ".") "X" else "." }.indexOf(".".repeat(diff + 1))
            if(indexOfSpace > -1 && (indexOfSpace + diff) < i) {
                (firstIndex..i).forEach { data[it] = "." }
                (indexOfSpace..(indexOfSpace+diff)).forEach { data[it] = digit }
            }
        }
    }
    return data.toTypedArray()
}


private fun Array<String>.checkSum(): Long =
    foldIndexed(0L) { index, acc, s ->
        if(s == ".") acc else acc + s.toInt() * index
    }
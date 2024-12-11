package advent

import tools.ResourceReader
import kotlin.system.measureTimeMillis

fun main() {
    val inputMap = ResourceReader("day11.txt").useLines { seq ->
        seq.map { it }.first().split(" ").map { it to 1L }
    }.orEmpty()

    measureTimeMillis {
        (1..75) .fold(inputMap) { stones, _ ->
            stones.blinkAll()
        }.let { result ->
            //Day 11 - Result 1
            println(result.sumOf { it.second })
        }
    }.let { println("Execution time : $it ms") }

}

private fun List<Pair<String, Long>>.blinkAll() : List<Pair<String, Long>> =
    flatMap { (stoneCode, count) ->
        when {
            stoneCode.length % 2 == 0 -> {
                val mid = stoneCode.length/2
                listOf(
                    stoneCode.take(mid) to count, //First half stone code
                    stoneCode.takeLast(mid).trimStart { it == '0' }.padStart(1 , '0') to count //Second half stone code
                )
            }
            stoneCode == "0" -> listOf("1" to count) //Change 0 to 1
            else -> listOf((stoneCode.toLong() * 2024).toString() to count) //Other case, multiply by 2024
        }
    }
        .groupBy { it.first } //Group by same stone code
        .map { (stoneCode, stoneGroup) -> stoneCode to stoneGroup.sumOf { it.second } } //Sum the nb of stone of each group
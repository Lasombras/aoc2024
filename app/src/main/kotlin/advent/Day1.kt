package advent

import tools.ResourceReader
import kotlin.math.abs


fun main() {
    val part1Result = withTeamLocation { team1, team2 ->
        val team1Sorted = team1.sorted()
        val team2Sorded = team2.sorted()

        team1Sorted.foldIndexed(0L) { index, acc, l ->
            acc + abs(l - team2Sorded[index])
        }
    }
    //Day 1 - Part 1 result
    println(part1Result)

    val part2Result = withTeamLocation { team1, team2 ->
        val team2Grouped = team2.groupBy { it }
        team1.fold(0) { acc, l ->
            acc + l * (team2Grouped[l]?.size ?: 0)
        }
    }
    //Day 1 - Part 2 result
    println(part2Result)
}

fun withTeamLocation(block : (team1: List<Long>, team2: List<Long>)->Long): Long {
    val team1 = mutableListOf<Long>()
    val team2 = mutableListOf<Long>()
    ResourceReader("day1.txt").useLines { seq ->
        seq.forEach { line ->
            line.split(" ").let {
                if(it.isNotEmpty()) {
                    team1.add(it.first().trim().toLong())
                    team2.add(it.last().trim().toLong())
                }
            }
        }
    }
    return block(team1, team2)
}

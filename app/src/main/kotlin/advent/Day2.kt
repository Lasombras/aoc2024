package advent

import tools.ResourceReader
import kotlin.math.abs

fun main() {
    val reports = ResourceReader("day2.txt").useLines { seq ->
        seq.map { line ->
            line.split(" ").map { it.trim().toInt() }
        }.toList()
    }.orEmpty()

    reports.filter {
        it.isValidOrder(asc = true) || it.isValidOrder(asc = false)
    }.let {
        //Day 2 - Part 1 result
        println(it.size)
    }

    reports.filter {
        it.isValidOrder(asc = true, allowedIssue = 1) || it.isValidOrder(asc = false, allowedIssue = 1)
    }.let {
        //Day 2 - Part 2 result
        println(it.size)
    }

}

private fun List<Int>.isValidOrder(asc: Boolean, allowedIssue: Int = 0, safeGap: Int = 3): Boolean {
    forEachIndexed { index, value ->
        val previous = this.getOrElse(index - 1) { if(asc) value - 1 else value + 1 }
        //Not safe
        if ((asc && value <= previous) || (asc.not() && value >= previous) || abs(value - previous) > safeGap) {
            //On issue, break if no more issue allowed
            return if (allowedIssue <= 0) {
                false
            } else {
                //Remove the previous value and try to continue
                if (removeLevel(index - 1).isValidOrder(asc = asc).not()) {
                    //Remove the current value and try to continue
                    removeLevel(index).isValidOrder(asc = asc)
                } else {
                    true
                }
            }
        }
    }
    return true
}

// We can only remove the level into the list
// But to optimize the number of check, we will only start the list from one index before the removed item
private fun List<Int>.removeLevel(index: Int) : List<Int> {
    val startList: List<Int> = if(index - 1 >= 0) subList(index - 1, index) else emptyList()
    val endList: List<Int> = if(index + 1 < size) subList(index + 1, size) else emptyList()
    return startList + endList
}
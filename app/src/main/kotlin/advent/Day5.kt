package advent

import tools.ResourceReader


fun main() {
    val inputOrder = ResourceReader("day5-1.txt").useLines { seq ->
        seq.map { line -> line.split("|").let { it[0].toInt() to it[1].toInt() } }.toList()
    }.orEmpty()
    val updatePages = ResourceReader("day5-2.txt").useLines { seq ->
        seq.map { line -> line.split(",").map { it.toInt() } }.toList()
    }.orEmpty()

    val after = inputOrder.groupBy({it.first}) { it.second }
    val before = inputOrder.groupBy({it.second}) { it.first }

    //Filter valid pages
    val validUpdates = updatePages.mapNotNull { pages ->
        //Control list validity
        pages.forEachIndexed { index, page ->
            before[page]?.let { beforePages ->
                //Check if the next pages aren't inside the before rules
                if(pages.drop(index + 1).any { it in beforePages }) return@mapNotNull null
            }
            after[page]?.let { afterPages ->
                //Check if the previous pages aren't inside the after rules
                if(pages.take(index).any { it in afterPages }) return@mapNotNull null
            }
        }
        pages
    }

    validUpdates.fold(0) { acc, pages ->
        //Add the middle page
        acc + pages[pages.size / 2]
    }.let {
        //Day 5 - Part 1 result
        println(it)
    }

    val invalidUpdates = updatePages - validUpdates.toSet()
    invalidUpdates.fold(0) { acc, pages ->
        acc + pages.order(before)[pages.size / 2]
    }.let {
        //Day 5 - Part 2 result
        println(it)
    }
}

private fun List<Int>.order(before: Map<Int, List<Int>>): List<Int> {
    forEachIndexed { index, page ->
        before[page]?.let { beforePages ->
            val nextPages = drop(index + 1)
            val errorPage  = nextPages.firstOrNull { it in beforePages }
            if(errorPage != null) {
                val newOrder = take(index) + errorPage + page + nextPages.filterNot { it == errorPage }
                return newOrder.order(before)
            }
        }
    }
    return this
}
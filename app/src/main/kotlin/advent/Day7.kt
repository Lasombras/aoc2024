package advent

import tools.ResourceReader

fun main() {
    val input = ResourceReader("day7.txt").useLines { seq ->
        seq.map { line ->
            line.split(" ", ": ").map { it.toLong() }
        }.toList()
    }.orEmpty()

    input.sumOf { line ->
        val target = line.first()
        val values = line.drop(1)
        if (calculate(1, values, target, Operator.MULTIPLY)) target else 0
    }.let {
        //Day 7 - Part 1 & 2 result
        println(it)
    }
}

private enum class Operator {
    SUM, MULTIPLY, CONCAT
}

private fun calculate(value: Long, values: List<Long>, target: Long, operator: Operator): Boolean {
    val total = when (operator) {
        Operator.SUM -> value + values.first()
        Operator.MULTIPLY -> value * values.first()
        Operator.CONCAT -> (value.toString() + values.first().toString()).toLong()
    }
    if(total > target)
        return false
    val nextValues = values.drop(1)
    if (nextValues.isEmpty())
        return total == target
    return calculate(total, nextValues, target, Operator.SUM) ||
        calculate(total, nextValues, target, Operator.MULTIPLY) ||
        calculate(total, nextValues, target, Operator.CONCAT)
}
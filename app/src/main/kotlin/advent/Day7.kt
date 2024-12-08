package advent

import tools.ResourceReader

fun main() {
    val input = ResourceReader("day7.txt").useLines { seq ->
        seq.map { line ->
            line.split(" ")
        }.toList()
    }.orEmpty()

    input.sumOf { line ->
        val target = line.first().dropLast(1).toLong()
        val values = line.drop(1).map { it.toLong() }
        if (calculate(1, values, target, Operator.MULTIPLY)) target else 0
    }.let {
        //Day 7 - Part 1 result
        println(it)
    }
}

private enum class Operator {
    NONE, SUM, MULTIPLY, CONCAT
}

private fun calculate(value: Long, values: List<Long>, target: Long, operator: Operator): Boolean {

    val total = when (operator) {
        Operator.SUM -> value + values.first()
        Operator.MULTIPLY -> value * values.first()
        Operator.CONCAT -> (value.toString() + values.first().toString()).toLong()
        Operator.NONE -> value
    }
    if(total > target)
        return false
    val nextValues = if (operator == Operator.NONE) values else values.drop(1)
    if (nextValues.isEmpty())
        return total == target
    return calculate(total, nextValues, target, Operator.SUM) ||
        calculate(total, nextValues, target, Operator.MULTIPLY) ||
        calculate(total, nextValues, target, Operator.CONCAT)
}
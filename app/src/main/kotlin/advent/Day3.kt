package advent

import tools.ResourceReader

fun main() {

    val input = ResourceReader("day3.txt").readText().orEmpty().trimIndent()

    val mulInstructions = """mul\(\d{1,3},\d{1,3}\)""".toRegex().findAll(input)

    mulInstructions
        .map { computeMul(it.value) }
        .sum() //Sum all mul results
        .let {
            //Day 3 - Part 1 result
            println(it)
        }

    val doInstructions = """do\(\)""".toRegex().findAll(input).map { it.range.first }
    val dontInstructions = """don't\(\)""".toRegex().findAll(input).map { it.range.first }

    //V1
    mulInstructions.map { mulOperator ->
        val index = mulOperator.range.first
        //Find the more closer instruction before the index
        val doIndex = doInstructions.lastOrNull { it <= index } ?: 0
        val dontIndex = dontInstructions.lastOrNull { it <= index } ?: -1

        if (doIndex > dontIndex) {
            computeMul(mulOperator.value)
        } else {
            0
        }
    }
        .sum() //Sum all mul results
        .let {
            //Day 3 - Part 2 result
            println(it)
        }

    //V2
    (mulInstructions.map { Token.Mul(it.range.first, computeMul(it.value)) } +
            doInstructions.map { Token.Do(it) } + dontInstructions.map { Token.Dont(it) })
        .sortedBy { it.index }
        .fold(true to 0) { (allowed, acc), token ->
            when (token) {
                is Token.Mul -> allowed to (acc + if (allowed) token.result else 0)
                is Token.Do -> true to acc //Just return the previous value with the allowed status
                is Token.Dont -> false to acc //Just return the previous value with the denied status
            }
        }.let {
            //Day 3 - Part 2 result
            print(it.second)
        }

}

private fun computeMul(mulOperator: String): Int =
    mulOperator.substring(4, mulOperator.length - 1)//Remove operator word "mul(***)"
        .split(",") //Split all values to multiply
        .fold(1) { accu, s -> accu * s.toInt() } //Multiply all values of mul by a fold

private sealed class Token(val index: Int) {
    class Mul(index: Int, val result: Int) : Token(index = index)
    class Do(index: Int) : Token(index)
    class Dont(index: Int) : Token(index)
}
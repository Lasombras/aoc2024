package advent

import tools.ResourceReader
import kotlin.math.*
import kotlin.system.measureTimeMillis

fun main() {

    //So ugly parsing file :P
    val inputMap = ResourceReader("day13.txt").useLines { seq ->
        val machines = mutableListOf<Machine>()
        var machine: Machine = Machine(0 to 0, 0 to 0, 0L to 0L)
        seq.forEach { line ->
            when {
                line.startsWith("Button A") -> {
                    val values = line.drop(9).split(",")
                    machine = machine.copy(buttonA = values[0].trim().drop(2).toInt() to values[1].trim().drop(2).toInt())
                }
                line.startsWith("Button B") -> {
                    val values = line.drop(9).split(",")
                    machine = machine.copy(buttonB = values[0].trim().drop(2).toInt() to values[1].trim().drop(2).toInt())
                }
                line.startsWith("Prize") -> {
                    val values = line.drop(6).split(",")
                    machine = machine.copy(prize = values[0].trim().drop(2).toLong() to values[1].trim().drop(2).toLong())
                }
                line.isBlank() -> machines.add(machine)
            }
        }
        machines.add(machine)
        machines
    }.orEmpty()


    measureTimeMillis {

        inputMap.sumOf { machine ->

            //Add A button until to target o be greather than X
            val buttonACount = machine.prize.first / machine.buttonA.first
            val buttonBCount = max(machine.prize.second - (machine.buttonA.second * buttonACount), 0) / machine.buttonB.second
            var path = Path(buttonACount, buttonBCount)

            while (path.getTarget(machine) != machine.prize && path.buttonA > 0) {
                //Optimize the -1 Step
                val step  = 1
                path = Path(path.buttonA - step, (machine.prize.second - (machine.buttonA.second * (path.buttonA - step))) / machine.buttonB.second)
            }
            if(path.getTarget(machine) != machine.prize)
                0
            else
                path.buttonA * 3 + path.buttonB
        }.let {
            //Day 13 - Part 1
            println(it)
        }

    }.let { println("Execution time : $it ms") }


    measureTimeMillis {

        inputMap.sumOf { wrongMachine ->

            //Fix machine
            val machine = wrongMachine.copy(prize = wrongMachine.prize.first + 10000000000000 to wrongMachine.prize.second + 10000000000000)

            val buttonBCount = (machine.prize.second * machine.buttonA.first - machine.prize.first * machine.buttonA.second).toDouble() / (machine.buttonB.second * machine.buttonA.first - machine.buttonB.first * machine.buttonA.second).toDouble()
            val buttonACount = (machine.prize.second - buttonBCount * machine.buttonB.second) / machine.buttonA.second

            if(floor(buttonBCount) == buttonBCount && floor(buttonACount) == buttonACount) {
                (buttonACount * 3 + buttonBCount).toLong()
            } else {
                0
            }
        }.let {
            //Day 13 - Part 2
            println(it)
        }

    }.let { println("Execution time : $it ms") }
}


private data class Path(val buttonA: Long, val buttonB: Long)

private fun Path.getTarget(machine: Machine) : Pair<Long, Long> =
    (buttonA * machine.buttonA.first + buttonB * machine.buttonB.first) to (buttonA * machine.buttonA.second + buttonB *  machine.buttonB.second)


private data class Machine(val buttonA: Pair<Int, Int>, val buttonB: Pair<Int, Int>, val prize: Pair<Long, Long>)
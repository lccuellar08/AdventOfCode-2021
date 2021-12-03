import java.io.File

fun main(args: Array<String>) {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_2.txt"
    val instructions = readInput(fileName)

    // Part 1: Get final position using instructions
    val naiveFinalPosition = getNaiveFinalPosition(instructions)
    println("Naive Final position is: $naiveFinalPosition. 1st Answer is: ${naiveFinalPosition.first * naiveFinalPosition.second}")

    // Part 2: Get final position using aim logic
    val finalPosition = getFinalPosition(instructions)
    println("Final position is: $finalPosition. 2nd Answer is: ${finalPosition.first * finalPosition.second}")
}

private fun getFinalPosition(instructions: List<Pair<Direction, Int>>): Pair<Int, Int> {
    var x = 0
    var y = 0
    var aim = 0

    instructions.forEach {
        val direction = it.first
        val units = it.second

        when(direction) {
            Direction.FORWARD -> {
                x += units
                y += aim * units
            }
            Direction.DOWN -> aim += units
            Direction.UP -> aim -= units
        }
    }
    return Pair(x,y)
}

private fun getNaiveFinalPosition(instructions: List<Pair<Direction, Int>>): Pair<Int,Int> {
    var x = 0
    var y = 0
    instructions.forEach {
        val direction = it.first
        val units = it.second

        when(direction) {
            Direction.FORWARD -> x += units
            Direction.DOWN -> y += units
            Direction.UP -> y -= units
        }
    }
    return Pair(x,y)
}

private fun readInput(fileName: String): List<Pair<Direction, Int>> {
    val instructions = mutableListOf<Pair<Direction, Int>>()
    File(fileName).forEachLine {
        val tokens = it.split(" ")
        var units = tokens[1].toInt()
        var pair: Pair<Direction, Int>

        when(tokens[0]) {
            "forward" -> pair = Pair(Direction.FORWARD, units)
            "down" -> pair = Pair(Direction.DOWN, units)
            "up" -> pair = Pair(Direction.UP, units)
            else -> pair = Pair(Direction.FORWARD, units)
        }

        instructions.add(pair)
    }
    return instructions
}

enum class Direction {
    FORWARD, UP, DOWN
}


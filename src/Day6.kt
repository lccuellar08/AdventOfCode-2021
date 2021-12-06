import java.io.File

fun mainDay6() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_6.txt"
    val initialState = readInput(fileName)

    // Part 1: 80 Generations
    var finalState = growFish(initialState,80)
    var totalFish = finalState.sum()
    println("After 80 generations, Total fish: $totalFish")

    // Part 1: 80 Generations
    finalState = growFish(initialState,256)
    totalFish = finalState.sum()
    println("After 256 generations, Total fish: $totalFish")
}

fun printState(state: LongArray) {
    state.forEachIndexed {i,e ->
        print("[$i]: $e\t")
    }
    print("Total Fish: ${state.sum()}")
    println()
}

fun growFish(initialState: List<Int>, generations: Int): LongArray {
    var finalState = LongArray(9) {
        0L
    }

    initialState.forEach {
        finalState[it] += 1L
    }

    for(g in 0 until generations) {
        val nextState = finalState.copyOf(finalState.size)
        finalState.forEachIndexed {i, e ->
            when(i) {
                0 -> {
                    nextState[8] = finalState[0]
                    nextState[6] = finalState[0]
                }
                7 -> {
                    nextState[6] += e
                }
                else -> {
                    nextState[i - 1] = e
                }
            }
        }
        finalState = nextState
    }

    return finalState
}

private fun readInput(fileName: String): List<Int> {
    var numFishes = mutableListOf<Int>()
    File(fileName).forEachLine {
        numFishes.addAll(it.split(",").map { e -> e.toInt() })
    }
    return numFishes
}
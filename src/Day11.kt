import java.io.File

fun mainDay11() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_11.txt"
    val octopuses = readInput(fileName)

    // Part 1: Get Total Flashes
    val totalFlashes = getFlashes(octopuses, 100)
    println("Total flashes: $totalFlashes")

    // Part 2: Find first synchronized flash
    val step = findSynchronizedFlash(readInput(fileName))
    println("First synchronized flash: $step")

}

fun printOctopuses(octopuses: Array<IntArray>) {
    octopuses.forEach {row ->
        row.forEach {e ->
            print(e)
        }
        println()
    }
    println()
}

fun findSynchronizedFlash(octopuses: Array<IntArray>): Int {
    var totalSteps = 0
    var tempOctopuses = octopuses

    while(true) {
        val (flashes, newOctopuses) = runStep(tempOctopuses)
        totalSteps += 1
        tempOctopuses = newOctopuses

        if(flashes == octopuses.size * octopuses[0].size) {
            break
        }
    }

    return totalSteps
}

fun getFlashes(octopuses: Array<IntArray>, iterations: Int): Int {
    var totalFlashes = 0
    var tempOctopuses = octopuses
    
    for(i in 1..iterations) {
        val (flashes, newOctopuses) = runStep(tempOctopuses)
        totalFlashes += flashes
        tempOctopuses = newOctopuses
//        println("Step: $i")
//        printOctopuses(tempOctopuses)
    }

    return totalFlashes
}

fun runStep(tempOctopuses: Array<IntArray>): Pair<Int, Array<IntArray>> {
    // First step, increment everything by one
    // Save the coordinates of 0's

    val listOfFlashes = mutableListOf<Pair<Int,Int>>()

    tempOctopuses.forEachIndexed {y, row ->
        row.forEachIndexed {x, col ->
            tempOctopuses[y][x] += 1
            if(tempOctopuses[y][x] > 9)
                listOfFlashes.add(Pair(x,y))
        }
    }

    // Second step, increment those close to the flashing points, until there are no more flashing points
    var totalFlashes = 0
    while(listOfFlashes.isNotEmpty()) {
        listOfFlashes.forEach {(x, y) ->
            totalFlashes += 1
            tempOctopuses[y][x] = 0

            // Change the neighbors
            val neighbors = getFlashingNeighbors(tempOctopuses, x, y)
            neighbors.filter { (nX, nY) -> tempOctopuses[nY][nX] != 0 }.
                    map { (nX, nY) -> tempOctopuses[nY][nX] += 1}
        }

        listOfFlashes.clear()

        tempOctopuses.forEachIndexed {y, row ->
            row.forEachIndexed { x, _ ->
                if(tempOctopuses[y][x] > 9)
                    listOfFlashes.add(Pair(x,y))
            }
        }
    }


    return Pair(totalFlashes, tempOctopuses)
}

fun getFlashingNeighbors(octopuses: Array<IntArray>, x: Int, y: Int): List<Pair<Int,Int>> {
    val neighbors = mutableListOf<Pair<Int,Int>>()

    if(y != 0)
        neighbors.add(Pair(x, y - 1))
    if(y != octopuses.size - 1)
        neighbors.add(Pair(x, y + 1))
    if(x != 0)
        neighbors.add(Pair(x - 1, y))
    if(x != octopuses[y].size - 1)
        neighbors.add(Pair(x + 1, y))
    if(y != 0 && x != 0)
        neighbors.add(Pair(x - 1, y - 1))
    if(y != 0 && x != octopuses[y].size - 1)
        neighbors.add(Pair(x + 1, y - 1))
    if(y != octopuses.size - 1 && x != 0)
        neighbors.add(Pair(x - 1, y + 1))
    if(y != octopuses.size - 1 && x != octopuses[y].size - 1)
        neighbors.add(Pair(x + 1, y + 1))

    return neighbors
}

private fun readInput(fileName: String): Array<IntArray> {
    val listOfIntArray = mutableListOf<IntArray>()

    File(fileName).forEachLine {
        val array = it.toCharArray().map { c -> c.toString().toInt() }.toIntArray()
        listOfIntArray.add(array)
    }

    return listOfIntArray.toTypedArray()
}
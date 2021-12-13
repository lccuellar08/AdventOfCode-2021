import java.io.File

fun mainDay9() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_9.txt"
    val matrix = readInput(fileName)
    matrix.forEach { r ->
        r.forEach { c ->
            if(c == 9)
                print("$c ")
            else
                print(". ")
        }
        println()
    }

    // Part 1
    // Find lowest points relative to position
    val listOfLowPoints = findLowPoints(matrix)
    var totalRisk = 0
    listOfLowPoints.forEach {
        val (x,y) = it
        val point = matrix[y][x]
        val risk = point + 1
        //println("($x,$y) = $point Risk: $risk")
        totalRisk += risk
    }

    println("Total Risk: $totalRisk")

    // Part 2
    // Find basin sizes using path finding algorithm
    val basinSizes = listOfLowPoints.map { p -> findBasinSize(matrix, p) + 1}
    val biggestBasins = basinSizes.sorted().takeLast(3)
    var answer = 1
    biggestBasins.forEach {b -> answer *= b}

    println("The biggest basins are: $biggestBasins")
    println("Answer is: $answer")

}

fun findBasinSize(matrix: Array<IntArray>, target: Pair<Int, Int>): Int {
    val paths = Array(matrix.size) {
        IntArray(matrix[0].size) {
            -1
        }
    }

    var basinSize = 0
    paths[target.second][target.first] = 0

    val neighbors = mutableListOf(target)

    while(neighbors.isNotEmpty()) {
        val current = neighbors.removeAt(0)
        val currentNeighbors = getNeighbors(matrix, current, paths)
        neighbors.addAll(getNeighbors(matrix, current, paths))

        currentNeighbors.forEach {
            if(paths[it.second][it.first] == -1) {
                paths[it.second][it.first] = paths[current.second][current.first] + 1
                basinSize++
            }
        }
    }

    return basinSize
}

fun printMatrix(matrix: Array<IntArray>, paths: Array<IntArray>) {
    matrix.forEachIndexed {i, r ->
        r.forEachIndexed {j, c ->
            if(c == 9)
                print("$c ")
            else if(paths[i][j] != -1)
                print("${paths[i][j]} ")
            else
                print(". ")
        }
        println()
    }
    println("---------------------------------------------------------------------------------")
}

fun getNeighbors(matrix: Array<IntArray>, target: Pair<Int, Int>, paths: Array<IntArray>): MutableList<Pair<Int,Int>> {
    val neighbors = mutableListOf<Pair<Int,Int>>()
    val (x,y) = target

    if(y != 0)
        neighbors.add(Pair(x, y - 1))
    if(y != matrix.size - 1)
        neighbors.add(Pair(x, y + 1))
    if(x != 0)
        neighbors.add(Pair(x - 1, y))
    if(x != matrix[y].size - 1)
        neighbors.add(Pair(x + 1, y))

    return neighbors.filter { p -> matrix[p.second][p.first] != 9 && paths[p.second][p.first] == -1}.toMutableList()
}

// Will return a list of coordinates
fun findLowPoints(matrix: Array<IntArray>): List<Pair<Int,Int>> {
    val listOfLowPoints = mutableListOf<Pair<Int,Int>>()

    matrix.forEachIndexed{y, r ->
        r.forEachIndexed{x, c ->
            val comparisonPoints = mutableListOf<Int>()
            if(y != 0)
                comparisonPoints.add(matrix[y-1][x])
            if(y != matrix.size - 1)
                comparisonPoints.add(matrix[y+1][x])
            if(x != 0)
                comparisonPoints.add(matrix[y][x-1])
            if(x != r.size - 1)
                comparisonPoints.add(matrix[y][x+1])

            if(comparisonPoints.none{ e -> c >= e })
                listOfLowPoints.add(Pair(x,y))
        }
    }

    return listOfLowPoints
}

private fun readInput(fileName: String): Array<IntArray> {
    val listOfIntArray = mutableListOf<IntArray>()

    File(fileName).forEachLine { line ->
        val row = line.toList().map { c -> c.toString().toInt() }
        listOfIntArray.add(row.toIntArray())
    }

    return listOfIntArray.toTypedArray()
}
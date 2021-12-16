import java.io.File
import kotlin.math.exp
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

fun mainDay15() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_15.txt"
    var matrix = readInput(fileName)

    matrix.forEach {
        it.forEach {e ->
            print(e)
        }
        println()
    }
    println()

    val shortestPath = shortestPathDijkstra(matrix, Pair(0,0), Pair(matrix.size -1, matrix[0].size -1))
    println(shortestPath)

    val newMatrix = enlargeMatrix(matrix)
    printMatrix(newMatrix)

    val shortestPathLarge = shortestPathDijkstra(newMatrix, Pair(0,0), Pair(newMatrix.size -1, newMatrix[0].size -1))
    println(shortestPathLarge)
}

fun printMatrix(matrix: Array<IntArray>) {
    matrix.forEachIndexed {y, it ->
        it.forEachIndexed {x, e ->
            if(x % 100 == 0 && x != 0)
                print("|")
            if(e == -1)
                print(".")
            else if(e > 9)
                print("x${e}x")
            else
                print(e)
        }
        println()
        if(y % 100 == 0 && y != 0)
            println("------------------------------------------------------")
    }
    println()
}

fun enlargeMatrix(matrix: Array<IntArray>): Array<IntArray> {
    var newMatrix =  Array(matrix.size * 5) {
        IntArray(matrix[0].size * 5) {
            0
        }
    }

    newMatrix.forEachIndexed {y, r ->
        r.forEachIndexed {x, _ ->
            val oldX = x % matrix.size
            val oldY = y % matrix.size

            val deltaX = x / matrix.size
            val deltaY = y / matrix.size

            newMatrix[y][x] = matrix[oldY][oldX] + deltaX + deltaY
            if(newMatrix[y][x] >= 10)
                newMatrix[y][x] -= 9
        }
    }

    return newMatrix
}

fun shortestPathDijkstra(matrix: Array<IntArray>, initPos: Pair<Int, Int>, finalPos: Pair<Int, Int>): Int {
    var nodes = mutableMapOf<Pair<Int,Int>, Boolean>()
    matrix.forEachIndexed { y, r -> r.forEachIndexed{x, _ -> nodes[Pair(x,y)] = false}}

    var distances = nodes.keys.associateWith { Int.MAX_VALUE }.toMutableMap()
    var prev = mutableMapOf<Pair<Int,Int>, Pair<Int, Int>>()

    distances[initPos] = matrix[initPos.second][initPos.first]

    var step = 1
    while(nodes.isNotEmpty()) {

        val minNode = distances.filterKeys { pair -> !(nodes[pair] ?: true) }.minBy { (k,v) -> v}

        if(minNode != null) {
            val minNodePair = minNode.key
            nodes[minNodePair] == true
            nodes.remove(minNodePair)

            if(minNodePair == finalPos) {
                break
            }

            val neighbors = findNeighbors(matrix, minNodePair)
            neighbors.filter { pair -> !(nodes[pair] ?: true)}.forEach {neighbor ->
                val alt = (distances[minNodePair] ?: 0) + matrix[neighbor.second][neighbor.first]

                if(alt < (distances[neighbor] ?: Int.MAX_VALUE) ) {
                    distances[neighbor] = alt
                    prev[neighbor] = minNodePair
                }
            }
        }
        step++
    }

    var finalRisk = 0
    var u: Pair<Int, Int>? = finalPos
    while(prev[u] != null || u == finalPos) {
        finalRisk += matrix[u!!.second][u!!.first]
        u = prev[u]
    }

    return finalRisk
}

fun findShortestPath(matrix: Array<IntArray>, initPos: Pair<Int, Int>, finalPos: Pair<Int,Int>): Int {
    val exploredPaths = Array(matrix.size) {
        IntArray(matrix[0].size) {
            -1
        }
    }

    var neighbors = mutableListOf(finalPos)
    exploredPaths[finalPos.second][finalPos.first] = 0

    while(neighbors.isNotEmpty()) {
        val current = neighbors.removeAt(0)
        val emptyNeighbors = getEmptyNeighbors(matrix, current, exploredPaths)
        emptyNeighbors.forEach {
            val exploredNeighbors = findExploredNeighbors(matrix, it, exploredPaths)
            val risks = exploredNeighbors.associateWith { (x, y) -> exploredPaths[y][x] }
            val minRisk = risks.minBy { (k, v) -> v }!!.value
            exploredPaths[it.second][it.first] = matrix[it.second][it.first] + minRisk
            neighbors.add(it)
        }

    }

    return exploredPaths[0][0]
}

fun getEmptyNeighbors(matrix: Array<IntArray>, pos: Pair<Int, Int>, paths: Array<IntArray>): List<Pair<Int,Int>> {
    val neighbors = mutableListOf<Pair<Int,Int>>()
    val (x,y) = pos

    if(y != 0)
        if(paths[y-1][x] == -1)
            neighbors.add(Pair(x, y - 1))
    if(y != matrix.size - 1)
        if(paths[y+1][x] == -1)
            neighbors.add(Pair(x, y + 1))
    if(x != 0)
        if(paths[y][x-1] == -1)
            neighbors.add(Pair(x - 1, y))
    if(x != matrix[0].size - 1)
        if(paths[y][x+1] == -1)
            neighbors.add(Pair(x + 1, y))

    return neighbors
}

fun findExploredNeighbors(matrix: Array<IntArray>, pos: Pair<Int, Int>, paths: Array<IntArray>): List<Pair<Int,Int>> {
    val neighbors = mutableListOf<Pair<Int,Int>>()
    val (x,y) = pos

    if(y != 0)
        if(paths[y-1][x] != -1)
            neighbors.add(Pair(x, y - 1))
    if(y != matrix.size - 1)
        if(paths[y+1][x] != -1)
            neighbors.add(Pair(x, y + 1))
    if(x != 0)
        if(paths[y][x-1] != -1)
            neighbors.add(Pair(x - 1, y))
    if(x != matrix[0].size - 1)
        if(paths[y][x+1] != -1)
            neighbors.add(Pair(x + 1, y))

    return neighbors
}

fun findNeighbors(matrix: Array<IntArray>, pos: Pair<Int, Int>): List<Pair<Int,Int>> {
    val neighbors = mutableListOf<Pair<Int,Int>>()
    val (x,y) = pos

    if(y != 0)
        neighbors.add(Pair(x, y - 1))
    if(y != matrix.size - 1)
        neighbors.add(Pair(x, y + 1))
    if(x != 0)
        neighbors.add(Pair(x - 1, y))
    if(x != matrix[0].size - 1)
        neighbors.add(Pair(x + 1, y))

    return neighbors
}

private fun readInput(fileName: String): Array<IntArray> {
    val matrix = mutableListOf<IntArray>()

    File(fileName).forEachLine { line ->
        val row = line.toList().map { c -> c.toString().toInt() }
        matrix.add(row.toIntArray())
    }

    return matrix.toTypedArray()
}

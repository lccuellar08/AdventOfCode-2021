import java.io.File

fun mainDay12() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_12.txt"
    val pathMap = readInput(fileName)

    val paths = caveDFS(pathMap, mutableMapOf<String, Int>(), mutableListOf("start"))
    val validPaths = paths.filter { it -> it.last() == "end"}

    val validPathsCount = validPaths.count()
    println("Total number of valid paths: $validPathsCount")


    // Part 2
    val secondPaths = caveDFS(pathMap, mutableMapOf<String, Int>(), mutableListOf("start"), true)
    val secondValidPaths = secondPaths.filter {it.last() == "end"}

    val secondValidPathsCount = secondValidPaths.count()
    println("Total number of valid second paths: $secondValidPathsCount")
}

fun caveDFS(pathMap: Map<String, List<String>>, visitedMap: MutableMap<String, Int>, path: MutableList<String>): List<List<String>> {
    //println("Current in: ${path.last()}")

    if(path.last() == "end")
        return listOf(path)

    visitedMap[path.last()] = 1

    val neighbors = getValidNeighbors(pathMap, path.last(), visitedMap)
    if(neighbors.isEmpty())
        return listOf(path)

    val paths = mutableListOf<List<String>>()
    neighbors.forEach {
        val newPathCopy = path.toMutableList()
        newPathCopy.add(it)
        val newPath = caveDFS(pathMap, visitedMap.toMutableMap(), newPathCopy)
        paths.addAll(newPath)
    }

    return paths
}

fun caveDFS(pathMap: Map<String, List<String>>, visitedMap: MutableMap<String, Int>, path: MutableList<String>, canVisitTwice: Boolean): List<List<String>> {
//    println("Current in: ${path.last()}")

    if(path.last() == "end")
        return listOf(path)

    val visitedTimes = (visitedMap[path.last()] ?: 0) + 1
    visitedMap[path.last()] = visitedTimes

    val (neighbors, visitedTwice) = getValidNeighbors(pathMap, path.last(), visitedMap, canVisitTwice)
    if(neighbors.isEmpty())
        return listOf(path)

    val paths = mutableListOf<List<String>>()
    neighbors.forEach {
        val newPathCopy = path.toMutableList()
        newPathCopy.add(it)
        if(visitedMap[it] == 1 && !it.first().isUpperCase())
            paths.addAll(caveDFS(pathMap, visitedMap.toMutableMap(), newPathCopy, false))
        else
            paths.addAll(caveDFS(pathMap, visitedMap.toMutableMap(), newPathCopy, canVisitTwice))
    }

    return paths
}

fun getValidNeighbors(pathMap: Map<String, List<String>>, current: String, visitedMap: MutableMap<String, Int>): MutableList<String> {
    val neighbors = pathMap[current]
    val validNeighbors = mutableListOf<String>()

    neighbors?.forEach {
        val visitedTimes = visitedMap[it] ?: 0
        if(it.first().isUpperCase() || visitedTimes == 0) {
            validNeighbors.add(it)
        }
    }

//    println("Raw Neighbors of $current are:\t\t$neighbors")
//    println("Valid Neighbors of $current are:\t$validNeighbors")

    return validNeighbors

}

fun getValidNeighbors(pathMap: Map<String, List<String>>, current: String, visitedMap: MutableMap<String, Int>, canVisitTwice: Boolean): Pair<MutableList<String>, Boolean> {
    val neighbors = pathMap[current]
    val validNeighbors = mutableListOf<String>()
    var visitedTwice = false

    neighbors?.forEach {
        val visitedTimes = visitedMap[it] ?: 0
        if(it.first().isUpperCase()) {
            validNeighbors.add(it)
        }
        else if(visitedTimes == 1 && canVisitTwice) {
            validNeighbors.add(it)
            visitedTwice = true
        }
        else if(visitedTimes == 0)
            validNeighbors.add(it)
    }

//    println("Raw Neighbors of $current are:\t\t$neighbors")
//    println("Valid Neighbors of $current are:\t$validNeighbors")

    return Pair(validNeighbors, visitedTwice)

}


private fun readInput(fileName: String): Map<String, List<String>> {
    val pathMap = mutableMapOf<String, MutableList<String>>()

    File(fileName).forEachLine {
        val tokens = it.split("-")
        if(pathMap.containsKey(tokens[0])) {
            pathMap[tokens[0]]?.add(tokens[1])

        } else {
            pathMap[tokens[0]] = mutableListOf(tokens[1])
        }

        if(tokens[0] != "start" && tokens[1] != "end") {
            if(pathMap.containsKey(tokens[1])) {
                pathMap[tokens[1]]?.add(tokens[0])
            }
            else {
                pathMap[tokens[1]] = mutableListOf(tokens[0])
            }
        }
    }

    return pathMap
}
import java.io.File
import kotlin.math.abs

fun mainDay7() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_7.txt"
    val crabPositions = readInput(fileName)

    val linearFuels = calculateLinearFuels(crabPositions)
    val totalLinearFuel = linearFuels.sum()
    println("Total linear fuel: $totalLinearFuel")

    val minQuadraticFuel = getMinFuel(crabPositions)
    val totalQuadraticFuel = minQuadraticFuel.sum()
    println("Total quadratic fuel: $totalQuadraticFuel")

}

private fun calculateLinearFuels(crabPositions: List<Int>): List<Int> {
    val fuelList = mutableListOf<Int>()
    val median = crabPositions.sorted()[crabPositions.size / 2]

    crabPositions.forEach { e ->
        fuelList.add(abs(e - median))
    }

    return fuelList
}

private fun getMinFuel(crabPositions: List<Int>): List<Int> {
    var minFuelList = listOf<Int>(crabPositions.size)
    var minFuel = Int.MAX_VALUE


    for(i in 0..crabPositions.max()!!) {
        val fuelList = calculateQuadraticFuel(crabPositions, i)
        val fuel = fuelList.sum()
        if(fuel < minFuel) {
            minFuelList = fuelList
            minFuel = fuel
        }
    }

    return minFuelList
}

private fun calculateQuadraticFuel(crabPositions: List<Int>, target: Int): List<Int> {
    return crabPositions.map { getQuadraticFuel(it, target)}
}

// N(N+1)/2
// Guaranteed to return an int, no odd numbers will be divided
fun getQuadraticFuel(crabPos: Int, finalPos: Int): Int {
    val n = abs(crabPos - finalPos)
    return (n * (n+1))/2
}

private fun readInput(fileName: String): List<Int> {
    val crabPos = mutableListOf<Int>()
    File(fileName).forEachLine {
        crabPos.addAll(it.split(",").map { e -> e.toInt() })
    }
    return crabPos
}
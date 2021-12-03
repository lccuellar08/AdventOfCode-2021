import java.io.File
import kotlin.math.pow

fun mainDay3() {
    val filename = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_3.txt"
    val numbers = readInput(filename)

    // Part 1:
    val (gamma, epsilon) = getGammaAndEpsilon(numbers)
    println("Gamma: $gamma, epsilon: $epsilon. Power: ${gamma * epsilon}")

    val oxygen = getOxygen(numbers)
    val co2 = getCO2(numbers)
    println("Oxygen: $oxygen, CO2: $co2. Life Support Ratting: ${oxygen * co2}")
}

fun getGammaAndEpsilon(numbers: Array<IntArray>): Pair<Int,Int> {
    var gamma = 0.0
    var epsilon = 0.0

    // Classic for loop in order to iterate by column
    // 0 <= col < numbers[0].size
    for(col in 0 until numbers[0].size) {
        val ones = numbers.count { it[col] == 1 }

        if((ones.toDouble() / numbers.size) > 0.50) {
            gamma += 2.0.pow(numbers[0].size - 1 - col)
        }
        else {
            epsilon += 2.0.pow(numbers[0].size - 1 - col)
        }
    }

    return Pair(gamma.toInt(), epsilon.toInt())
}

private fun getOxygen(numbers: Array<IntArray>): Int {
    var searchArray = numbers
    var finalNumber = numbers.first()

    for(col in 0 until numbers[0].size) {
        val ones = searchArray.count { it[col] == 1}

        if((ones.toDouble()) / searchArray.size >= 0.50) {
            searchArray = searchArray.filter { it[col] == 1}.toTypedArray()

        }
        else {
            searchArray = searchArray.filter { it[col] == 0}.toTypedArray()
        }

        if(searchArray.size == 1) {
            finalNumber = searchArray.first()
            break
        }
    }

    var res = 0.0
    finalNumber.forEachIndexed { i, e ->
        res += 2.0.pow(finalNumber.size - 1 - i) * e
    }

    return res.toInt()
}

private fun getCO2(numbers: Array<IntArray>): Int {
    var searchArray = numbers
    var finalNumber = numbers.first()

    for(col in 0 until numbers[0].size) {
        val ones = searchArray.count { it[col] == 1}

        if((ones.toDouble()) / searchArray.size < 0.50) {
            searchArray = searchArray.filter { it[col] == 1}.toTypedArray()
        }
        else {
            searchArray = searchArray.filter { it[col] == 0}.toTypedArray()
        }

        if(searchArray.size == 1) {
            finalNumber = searchArray.first()
            break
        }
    }

    var res = 0.0
    finalNumber.forEachIndexed { i, e ->
        res += 2.0.pow(finalNumber.size - 1 - i) * e
    }

    return res.toInt()
}

private fun readInput(fileName: String): Array<IntArray> {
    val rows = mutableListOf<IntArray>()


    File(fileName).forEachLine { it ->
        val array = it.toCharArray().map{ c -> c.toString().toInt() }.toIntArray()
        rows.add(array)
    }

    return rows.toTypedArray<IntArray>()
}
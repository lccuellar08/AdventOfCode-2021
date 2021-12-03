import java.io.File

fun mainDay1() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_1.txt"
    val numbers = readInput(fileName)

    // Part 1: Get number of increases
    // 1602
    val numOfIncreases = getNumberOfIncreases(numbers)
    println("Number of increases in: $numOfIncreases")


    // Part 2: Get number of increases in window
    val windows = getWindows(numbers)
    val numOfWindowIncreases = getNumberOfIncreases(windows)
    println("Number of increases in windows: $numOfWindowIncreases")
}

fun getNumberOfIncreases(nums: IntArray): Int {
    var total = 0
    nums.forEachIndexed {index, element ->
        if(index > 0) {
            if(element > nums[index - 1])
                total += 1
        }
    }
    return total
}

fun getWindows(nums: IntArray): IntArray {
    val intList = mutableListOf<Int>()
    nums.forEachIndexed { index, _ ->
        if(index >= 2) {
            val window = nums[index] + nums[index - 1] + nums[index - 2]
            intList.add(window)
        }
    }
    return intList.toIntArray()
}

private fun readInput(fileName: String): IntArray {
    val numbers = mutableListOf<Int>()
    File(fileName).forEachLine {
        numbers.add(it.toInt())
    }
    return numbers.toIntArray()
}
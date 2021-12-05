import java.io.File
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

fun mainDay5() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_5.txt"
    val vectors = readInput(fileName)
    val board = getBoardPart1(vectors, 1000, 1000)
    //printBoard(board)

    // Part 1
    // Get points with at least a 2
    var atLeast2 = board.sumBy { row -> row.filter { e -> e >= 2}.count()}
    println("Points with at least 2: $atLeast2")


    val board2 = getBoardPart2(vectors, 1000, 1000)
    // printBoard(board2)
    atLeast2 = board2.sumBy { row -> row.filter { e -> e >= 2}.count()}
    println("Points with at least 2: $atLeast2")
}

fun printBoard(board: Array<IntArray>) {
    board.forEach {
        it.forEach { e ->
            run {
                when (e) {
                    0 -> print(".")
                    else -> print(e)
                }
            }
        }
        println()
    }
}

fun getBoardPart1(vectors: List<Vector>, n_rows: Int, n_cols: Int): Array<IntArray> {
    val board = Array(n_rows) {
        IntArray(n_cols) {
            0
        }
    }

    vectors.filter { it.isVertical || it.isHorizontal }.forEach {
        val xDelta = sign((it.x2 - it.x1).toDouble()).toInt()
        val yDelta = sign((it.y2 - it.y1).toDouble()).toInt()

        var x = it.x1
        var y = it.y1

        while(x != it.x2 || y != it.y2) {
            board[y][x] += 1
            x += xDelta
            y += yDelta
        }
        board[y][x] += 1
    }

    return board
}

fun getBoardPart2(vectors: List<Vector>, n_rows: Int, n_cols: Int): Array<IntArray> {
    // Initialize a 0 2D array
    val board = Array(n_rows) {
        IntArray(n_cols) {
            0
        }
    }

    vectors.forEach {
        val xDelta = sign((it.x2 - it.x1).toDouble()).toInt()
        val yDelta = sign((it.y2 - it.y1).toDouble()).toInt()

        var x = it.x1
        var y = it.y1

        while(x != it.x2 || y != it.y2) {
            board[y][x] += 1
            x += xDelta
            y += yDelta
        }
        board[y][x] += 1
    }

    return board
}

private fun readInput(fileName: String): List<Vector> {
    val vectors = mutableListOf<Vector>()

    File(fileName).forEachLine {
        val points = it.split("->").map { e -> e.trim()}
        val point1 = points[0].split(",").map {e -> e.trim().toInt()}
        val point2 = points[1].split(",").map {e -> e.trim().toInt()}

        val isVertical = point1[0] == point2[0]
        val isHorizontal = point1[1] == point2[1]

        if(point1[0] < point2[0])
            vectors.add(Vector(point1[0], point1[1], point2[0], point2[1], isVertical, isHorizontal))
        else
            vectors.add(Vector(point2[0], point2[1], point1[0], point1[1], isVertical, isHorizontal))
    }

    return vectors
}

data class Vector(val x1: Int, val y1: Int, val x2: Int, val y2: Int, val isVertical: Boolean, val isHorizontal: Boolean)
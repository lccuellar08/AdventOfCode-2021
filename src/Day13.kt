import com.sun.org.apache.xpath.internal.operations.Bool
import java.io.File
import kotlin.math.abs

fun mainDay13() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_13.txt"
    val (points, folds) = readInput(fileName)
    val board = createBoard(points)
    //printFoldableBoard(board)

    // First Instruction
    val firstFold = folds.first()
    var newBoard = if(firstFold.first == 'y')
        foldBoardY(board, firstFold.second)
    else
        foldBoardX(board, firstFold.second)

    //printFoldableBoard(newBoard)
    var numOfPoints = 0
    newBoard.forEach { r ->
        r.forEach {c ->
            numOfPoints += c
        }
    }
    println("Number of points after 1 fold: $numOfPoints")

    newBoard = board
    folds.forEach {(axis, alongBy) ->
        newBoard = if(axis == 'y')
            foldBoardY(newBoard, alongBy)
        else
            foldBoardX(newBoard, alongBy)
    }

    printFoldableBoard(newBoard)

}

fun foldBoardY(board: Array<IntArray>, alongBy: Int): Array<IntArray> {
    val newY = board.size / 2

    val newBoard = Array(newY) {
        IntArray(board[0].size) {
            0
        }
    }

    board.forEachIndexed {y, row ->
        row.forEachIndexed{x, col ->
            if(col == 1) {
                if(y < alongBy)
                    newBoard[y][x] = 1
                else {
                    val distance = abs(y - alongBy)
                    val newY = y - (distance * 2)
                    newBoard[newY][x] = 1
                }
            }
        }
    }

    return newBoard
}

fun foldBoardX(board: Array<IntArray>, alongBy: Int): Array<IntArray> {
    val newX = board[0].size / 2

    val newBoard = Array(board.size) {
        IntArray(newX) {
            0
        }
    }

    board.forEachIndexed {y, row ->
        row.forEachIndexed{x, col ->
            if(col == 1) {
                if(x < alongBy)
                    newBoard[y][x] = 1
                else {
                    val distance = abs(x - alongBy)
                    val newX = x - (distance * 2)
                    newBoard[y][newX] = 1
                }
            }
        }
    }

    return newBoard
}

fun printFoldableBoard(board: Array<IntArray>) {
    board.forEach {
        it.forEach {e ->
            when(e) {
                0 -> print("  ")
                1 -> print("█ ")
            }
        }
        println()
    }
    println()
}

fun createBoard(points: List<Pair<Int,Int>>): Array<IntArray> {
    val maxX = points.maxBy{it.first}?.first ?: 10
    val maxY = points.maxBy{it.second}?.second ?: 10

    println("Max x: $maxX, MaxY: $maxY")

    val board = Array(maxY + 2) {
        IntArray(maxX + 2) {
            0
        }
    }

    points.forEach {(x, y) ->
        board[y][x] = 1
    }

    return board
}

// Return 2 things
// List of Points
// List of folds
private fun readInput(fileName: String): Pair<List<Pair<Int,Int>>, List<Pair<Char, Int>>> {
    val points = mutableListOf<Pair<Int,Int>>()
    val folds = mutableListOf<Pair<Char,Int>>()

    File(fileName).forEachLine {
        if(it.contains(',')) {
            val tokens = it.split(",")
            points.add(Pair(tokens[0].toInt(), tokens[1].toInt()))
        }
        else if(it.contains('f')) {
            val tokens = it.split(" ")
            val fold = tokens[2].split("=")
            val axis = fold[0].first()
            val value = fold[1].toInt()
            folds.add(Pair(axis, value))
        }
    }

    return Pair(points, folds)
}
import java.io.File

fun mainDay4() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_4.txt"
    val (numbers, boards) = readInput(fileName)

    val (winningPoints, lastNumber) = getFirstWinner(numbers, boards)
    println("Board won with $winningPoints points. Last number was: $lastNumber. Answer is: ${winningPoints * lastNumber}")

    val (lastWinningPoints, lastWinningNumber) = getLastWinner(numbers, boards)
    println("Last Board won with $lastWinningPoints points. Last number was: $lastWinningNumber. Answer is: ${lastWinningPoints * lastWinningNumber}")
}

fun getFirstWinner(numbers: List<Int>, boards: List<Board>): Pair<Int, Int> {
    // Use for loops in order to be able to break
    for(number in numbers) {
        for(board in boards) {
            if(board.markTile(number)) {
                return Pair(board.getWinningPoints(), number)
            }
        }
    }
    return Pair(0,0)
}

fun getLastWinner(numbers: List<Int>, boards: List<Board>): Pair<Int, Int> {
    var lastWinner = boards[0]
    var lastNumber = numbers[0]
    for(number in numbers) {
        for(board in boards) {
            if(!board.hasWon && board.markTile(number)) {
                lastWinner = board
                lastNumber = number
            }
        }
    }
    lastWinner.printBoard()
    return Pair(lastWinner.getWinningPoints(), lastNumber)
}

private fun readInput(fileName: String): Pair<List<Int>, List<Board>> {
    var numbers = listOf<Int>()
    var boards = mutableListOf<Board>()

    var boardNumbers = mutableListOf<Int>()

    var lineNum = 1
    File(fileName).forEachLine {
        if(lineNum == 1) {
            numbers = it.split(",").map { e -> e.toInt() }
        }
        else {
            val tokens = it.split(" ").filter {e -> e.isNotBlank()}
            if(boardNumbers.isNotEmpty() && tokens.size < 5) {
                val board = Board(5,5,boardNumbers.toList())
                boards.add(board)
                boardNumbers.clear()
            }
            else if(tokens.size == 5) {
                boardNumbers.addAll(tokens.map { e -> e.toInt()} )
            }
            else {
                // Do nothing
            }
        }
        lineNum += 1
    }

    if(boardNumbers.isNotEmpty())
        boards.add(Board(5,5,boardNumbers))

    return Pair(numbers, boards)
}

class Board(private val n_rows: Int = 5, private val n_cols: Int = 5, private val numbers: List<Int>) {
    var board: Array<Array<Tile>>
    var hasWon = false

    init {
        board = Array(n_rows) { r ->
            Array(n_cols) { c ->
                Tile(numbers[r * n_cols + c], r, c, false)
            }
        }
    }

    fun printBoard() {
        board.forEach {
            it.forEach {e ->
                if(e.marked)
                    print(" X")
                else
                    print(" ${e.number}")
            }
            println()
        }
        println()
    }

    fun getWinningPoints(): Int {
        var total = 0
        board.forEach {
            it.forEach {
                if(!it.marked)
                    total += it.number
            }
        }
        return total
    }

    fun markTile(number: Int): Boolean {
        val tile = findTile(number)
        if(tile == null) {
            return false
        }
        else {
            tile.marked = true
            if(isWinner()) {
                hasWon = true
            }
        }
        return hasWon
    }

    fun isWinner(): Boolean {
        return isVerticalWinner() || isHorizontalWinner()
    }

    private fun isVerticalWinner(): Boolean {
        var isWinner = true
        for(j in 0 until n_cols) {
            isWinner = true
            for(i in 0 until n_rows) {
                if(!board[i][j].marked) {
                    isWinner = false
                }
            }
            if(isWinner)
                break
        }
        return isWinner
    }

    private fun isHorizontalWinner(): Boolean {
        var isWinner = true
        for (i in 0 until n_rows) {
            isWinner = true
            for (j in 0 until n_cols) {
                if(!board[i][j].marked) {
                    isWinner = false
                }
            }
            if(isWinner)
                break
        }
        return isWinner
    }

    fun findTile(number: Int): Tile? {
        val index = numbers.indexOf(number)
        if(index == -1)
            return null
        // Convert from flat index to 2D Matrix
        // Row = number / n_rows
        // Col = number % n_cols
        val row = index / n_rows
        val col = index % n_cols

        return board[row][col]
    }
}

data class Tile(val number: Int, val row: Int, val col: Int, var marked: Boolean = false)

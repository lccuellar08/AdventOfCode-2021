import java.io.File

fun mainDay10() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_10.txt"
    val allLines = readInput(fileName)

    // Part 1: Find corrupted chunks and add up their respective scores
    val corruptedChunks = allLines.map {s -> findCorruptedChunk(s)}

    val syntaxScore = corruptedChunks.map {
        when(it.first) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }.sum()

    println("Total syntax score: $syntaxScore")


    // Part 2: Remove corrupted lines, and complete them
    val notCorruptedLines = allLines.filterIndexed {i, s -> corruptedChunks[i].second == -1 }
    val completeLines = notCorruptedLines.map { s -> completeChunk(s)}

    val scores = completeLines.map {s -> getCompleteScore(s)}
    val median= scores.sorted()[scores.size / 2]

    println("Median completion score is: $median")
}

fun getCompleteScore(line: String): Long {
    var score = 0L
    line.forEach {c ->
        score *= 5L
        score += when(c) {
            ')' -> 1L
            ']' -> 2L
            '}' -> 3L
            '>' -> 4L
            else -> 0L
        }
    }
    return score
}

fun completeChunk(line: String): String {
    val openStack = mutableListOf<Char>()
    line.forEachIndexed {i, c ->
        when(c) {
            '(' -> openStack.add(0, c)
            '[' -> openStack.add(0, c)
            '{' -> openStack.add(0, c)
            '<' -> openStack.add(0, c)
            ')' -> openStack.removeAt(0)
            ']' -> openStack.removeAt(0)
            '>' -> openStack.removeAt(0)
            '}' -> openStack.removeAt(0)
        }
    }

    return if(openStack.isNotEmpty()) {
        openStack.map {c ->
            when(c) {
                '(' -> ')'
                '[' -> ']'
                '{' -> '}'
                '<' -> '>'
                else -> 'x'
            }
        }.joinToString("")
    }
    else
        ""

}

fun findCorruptedChunk(line: String): Pair<Char?, Int> {
    val openStack = mutableListOf<Char>()
    line.forEachIndexed {i, c ->
        when(c) {
            '(' -> openStack.add(0, c)
            '[' -> openStack.add(0, c)
            '{' -> openStack.add(0, c)
            '<' -> openStack.add(0, c)
            ')' -> {
                if(openStack.removeAt(0) != '(')
                    return Pair(c, i)
            }
            ']' -> {
                if(openStack.removeAt(0) != '[')
                    return Pair(c, i)
            }
            '}' -> {
                if(openStack.removeAt(0) != '{')
                    return Pair(c, i)
            }
            '>' -> {
                if(openStack.removeAt(0) != '<')
                    return Pair(c, i)
            }
        }
    }

    return Pair(null, -1)
}

private fun readInput(fileName: String): List<String> {
    val allLines = mutableListOf<String>()

    File(fileName).forEachLine { line ->
        allLines.add(line)
    }

    return allLines
}
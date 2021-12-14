import java.io.File

fun mainDay14() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_14.txt"
    var (template, insertionRules) = readInput(fileName)

    template = "X${template}X"

    val (maxChar, minChar) = insertPairsFast(template, insertionRules, 10)
    println("After 10 steps: Max value is: $maxChar, and min value is: $minChar")
    println("Difference is: ${maxChar - minChar}")

    val (maxChar2, minChar2) = insertPairsFast(template, insertionRules, 40)
    println("After 40 steps: Max value is: $maxChar2, and min value is: $minChar2")
    println("Difference is: ${maxChar2 - minChar2}")

}

fun insertPairsFast(template: String, insertionRules: Map<String, Char>, steps: Int): Pair<Long, Long> {
    var permutations = generatePermutationsMap(insertionRules)
    template.windowed(2).forEach {
        val currentQuantity = permutations[it] ?: 0
        permutations[it] = currentQuantity + 1
    }

    for(i in 1..steps) {
        var newPermutations = permutations.toMutableMap()
        permutations.filterValues { it > 0 }.forEach { (k,v) ->
            val rule = insertionRules[k]
            if(rule != null) {
                val perm1 = k.first() + rule.toString()
                val perm2 = rule.toString() + k.last()
                newPermutations[perm1] = newPermutations[perm1]?.plus(v) ?: 0L
                newPermutations[perm2] = newPermutations[perm2]?.plus(v) ?: 0L
                newPermutations[k] = newPermutations[k]?.minus(v) ?: 0L
            }
        }
        permutations = newPermutations
    }

    val uniqueChars = permutations.keys.toList().joinToString("").groupBy{it}.keys.toList()
    val charsCount = uniqueChars.associateWith {
        permutations.filterKeys { k -> k.contains(it) }.mapValues {(k,v) ->
            if(k.first() == k.last())
                2L * v
            else
                v
        }.values.sum() / 2L
    }

    var maxChar = charsCount.maxBy {it.value}!!.value
    var minChar = charsCount.filterKeys {it != 'X'}.filterValues {it > 0}.minBy { it.value}!!.value

    return Pair(maxChar, minChar)
}

fun generatePermutationsMap(insertionRules: Map<String, Char>): MutableMap<String, Long> {
    val listOfPatterns = insertionRules.map{ (k,v) -> k}
    val uniqueChars = listOfPatterns.joinToString("").groupBy{it}.keys.toList()

    val permutations = mutableListOf<String>()
    for(i in 0 until uniqueChars.size) {
        for(j in i until uniqueChars.size) {
            val permutation1 = "${uniqueChars[i]}${uniqueChars[j]}"
            val permutation2 = "${uniqueChars[j]}${uniqueChars[i]}"
            permutations.add(permutation1)
            permutations.add(permutation2)
        }
    }

    return permutations.associateWith { 0L }.toMutableMap()
}

private fun readInput(fileName: String): Pair<String, Map<String, Char>> {
    var template = ""
    var insertionRules = mutableMapOf<String, Char>()

    File(fileName).forEachLine { line ->
        val tokens = line.split("->")
        if(tokens.size == 1 && tokens[0].isNotBlank()) {
            template = tokens[0]
        }
        else if(tokens.size == 2){
            var mappings = tokens.map { it.trim() }
            insertionRules[mappings[0]] = mappings[1].first()
        }
    }

    return Pair(template, insertionRules)
}
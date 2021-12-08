import java.io.File

fun mainDay8() {
    val fileName = "/Users/lc2377/IdeaProjects/AdeventOfCode-2021/src/input_8.txt"
    val inputs = readInput(fileName)

    // Part 1: Get number of simple digits: 1, 4, 7, and 8
    val numOfSimpleDigits = countSimpleDigits(inputs)
    println("Number of simple digits: $numOfSimpleDigits")

    // Part 2: Get all digits and sum them up
    val allDigits = getAllDigits(inputs)
    val finalSum = allDigits.sum()

    println("Total sum: $finalSum")
}

private fun getAllDigits(inputs: List<Pair<String,String>>): List<Int> {
    var digits = mutableListOf<Int>()

    inputs.forEach {
        val display = Display()
        val tokens = it.first.split(" ")

        // Get 1, which will be the only one with input of size 2
        val oneInput = tokens.first { s -> s.length == 2 }
        //println("One input: $oneInput")

        // Get 7, which will be the only one with input of size 3
        // The character in 7 not in one, will be the topmost character 'a'
        val sevenInput = tokens.first { s -> s.length == 3 }
        //println("Seven input: $sevenInput")
        val aInput = sevenInput.toList().filterNot { c -> oneInput.contains(c) }.first()
        //println("A input: $aInput")


        display.addMapping(aInput, 'a')

        // Get 4, which will be the only one with input of size 4
        val fourInput = tokens.first { s -> s.length == 4 }
        //println("Four input: $fourInput")

        // Get 3, which will be the only one with input of size 5
        // Where all the characters in 7 will be in there
        val threeInput = tokens.filter { s -> s.length == 5 }.
            first { s -> s.toList().containsAll(sevenInput.toList()) }
        //println("Three input: $threeInput")

        // If we intersect 3 and 4, and removes the letters from the number 1, we shall get the middle character 'd'
        val dInput = fourInput.toList().intersect(threeInput.toList()).toList().
                filterNot {c -> oneInput.contains(c)}.first()
        display.addMapping(dInput, 'd')
        //println("D input: $dInput")

        // If we we remove the inputs from 1 from 4, and we remove the middle character 'd'
        // We'll get the top left character 'b'

        val bInput = fourInput.filterNot {c -> oneInput.contains(c)}.first { c -> c != dInput}
        //println("B input: $bInput")
        display.addMapping(bInput, 'b')

        // Get 2, which will be the only one with input of size 5
        // Where it does not contain the top left character 'b' and is not input 3
        val twoInput = tokens.filterNot{s -> s == threeInput}.filter { s -> s.length == 5}.filterNot { s -> s.contains(bInput)}.first()
        //println("Two input: $twoInput")

        // If we intersect 1 and 2, we will be left with only the top right character 'c'
        val cInput = oneInput.toList().intersect(twoInput.toList()).first()
        //println("C input: $cInput")
        display.addMapping(cInput, 'c')

        // The character that is not c in 1, will be the bottom right character 'f'
        val fInput = oneInput.toList().first { c -> c != cInput}
        //println("F input: $fInput")
        display.addMapping(fInput, 'f')

        // If we remove 'a', 'c', 'd', and 'f' from 3 we'll be left with the bottom character 'g'
        val gInput = threeInput.toList().filterNot {c -> listOf(aInput,cInput,dInput,fInput).contains(c)}.first()
        //println("G input: $gInput")
        display.addMapping(gInput, 'g')

        // If we remove 'a', 'c', 'd', and 'g' from 2 we'll be left with the bottom left character 'e'
        val eInput = twoInput.toList().filterNot {c -> listOf(aInput,cInput,dInput,gInput).contains(c)}.first()
        //println("E input: $eInput")
        display.addMapping(eInput, 'e')



        var finalDigit = ""
        it.second.split(" ").forEach {s ->
            val digit = display.getDigit(s.toList())
            finalDigit += digit
        }
        digits.add(finalDigit.toInt())
    }

    return digits
}

private fun countSimpleDigits(inputs: List<Pair<String,String>>): Int {
    var numDigits = 0

    inputs.forEach {
        val tokens = it.second.split(" ")
        tokens.forEach { digit ->
            when(digit.length) {
                2 -> numDigits++
                3 -> numDigits++
                4 -> numDigits++
                7 -> numDigits++
            }
        }
    }

    return numDigits
}

private fun readInput(fileName: String): List<Pair<String, String>> {
    val inputsList = mutableListOf<Pair<String,String>>()

    File(fileName).forEachLine {
        val tokens = it.split("|").map { s -> s.trim() }
        inputsList.add( Pair(tokens[0], tokens[1]) )
    }

    return inputsList
}

class Display() {
    private val digits: MutableMap<Char, Char?> = mutableMapOf (
        'a' to null,
        'b' to null,
        'c' to null,
        'd' to null,
        'e' to null,
        'f' to null,
        'g' to null
    )

    fun getDigit(inputs: List<Char>): Int {
        val mappedInputs = inputs.map {
            digits[it] ?: 'a'
        }.sorted()

        /*
              0:      1:      2:      3:      4:
             aaaa    ....    aaaa    aaaa    ....
            b    c  .    c  .    c  .    c  b    c
            b    c  .    c  .    c  .    c  b    c
             ....    ....    dddd    dddd    dddd
            e    f  .    f  e    .  .    f  .    f
            e    f  .    f  e    .  .    f  .    f
             gggg    ....    gggg    gggg    ....


              5:      6:      7:      8:      9:
             aaaa    aaaa    aaaa    aaaa    aaaa
            b    .  b    .  .    c  b    c  b    c
            b    .  b    .  .    c  b    c  b    c
             dddd    dddd    ....    dddd    dddd
            .    f  e    f  .    f  e    f  .    f
            .    f  e    f  .    f  e    f  .    f
             gggg    gggg    ....    gggg    gggg
         */

        // Case 0
        //
        if(mappedInputs == listOf('a','b','c','e','f','g')) {
            return 0
        }

        // Case 1
        if(mappedInputs == listOf('c','f')) {
            return 1
        }

        // Case 2
        if(mappedInputs == listOf('a','c','d','e','g')) {
            return 2
        }

        // Case 3
        if(mappedInputs == listOf('a','c','d','f','g')) {
            return 3
        }

        // Case 4
        if(mappedInputs == listOf('b','c','d','f')) {
            return 4
        }

        // Case 5
        if(mappedInputs == listOf('a','b','d','f','g')) {
            return 5
        }

        // Case 6
        if(mappedInputs == listOf('a','b','d','e','f','g')) {
            return 6
        }

        // Case 7
        if(mappedInputs == listOf('a','c','f')) {
            return 7
        }

        // Case 8
        if(mappedInputs == listOf('a','b','c','d','e','f','g')) {
            return 8
        }
        if(mappedInputs == listOf('a','b','c','d','f','g')) {
            return 9
        }
        else
            return -1
    }

    fun addMapping(fromDigit: Char, toDigit: Char) {
        digits[fromDigit] = toDigit
    }
}
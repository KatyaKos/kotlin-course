package ru.spbau.mit

import java.util.*

class PatternFiller(private val k: Int, private val pattern: String,
                    private val symbolsUsed: BooleanArray = BooleanArray(k, {false}),
                    private val length: Int = pattern.length) {

    private fun Char.toIndex(): Int = this - 'a'

    private fun getLargestNotUsedLetter(): Char {
        val index: Int = symbolsUsed.lastIndexOf(false)
        if (index == -1) return 'a'
        symbolsUsed[index] = true
        return 'a' + index
    }

    fun solve(): String {
        for (letter in pattern) {
            if (letter !='?') symbolsUsed[letter.toIndex()] = true
        }
        val result: CharArray = pattern.toCharArray()
        for (iL in (length / 2 - 1 + length % 2) downTo 0) {
            val iR = length - iL - 1
            if (result[iL] == '?') {
                if (result[iR] != '?') result[iL] = result[iR]
                else {
                    val newLetter = getLargestNotUsedLetter()
                    result[iL] = newLetter
                    result[iR] = newLetter
                }
            } else {
                if (result[iR] == '?') result[iR] = result[iL]
                else if (result[iR] != result[iL]) return "IMPOSSIBLE"
            }
        }
        if (symbolsUsed.contains(false)) return "IMPOSSIBLE"
        return result.joinToString(separator = "")
    }
}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    println(PatternFiller(input.nextInt(), input.next()).solve())
    input.close()
}
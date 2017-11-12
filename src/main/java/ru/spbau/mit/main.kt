package ru.spbau.mit

import java.util.*

fun Char.toIndex(): Int = this - 'a'

fun solve(k: Int, pattern: String): String {
    val symbolsUsed = BooleanArray(k, { false })
    val length: Int = pattern.length
    for (letter in pattern) {
        if (letter !='?') {
            symbolsUsed[letter.toIndex()] = true
        }
    }
    val result: CharArray = pattern.toCharArray()
    for (iL in (length / 2 - 1 + length % 2) downTo 0) {
        val iR = length - iL - 1
        if (result[iL] == '?') {
            if (result[iR] != '?') result[iL] = result[iR]
            else {
                var newLetter: Char
                val index: Int = symbolsUsed.lastIndexOf(false)
                if (index == -1) {
                    newLetter = 'a'
                } else {
                    symbolsUsed[index] = true
                    newLetter = 'a' + index
                }
                result[iL] = newLetter
                result[iR] = newLetter
            }
        } else {
            if (result[iR] == '?') result[iR] = result[iL]
            else if (result[iR] != result[iL]) return "IMPOSSIBLE"
        }
    }
    if (!symbolsUsed.all{ s -> s }) return "IMPOSSIBLE"
    return result.joinToString(separator = "")
}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    println(solve(input.nextInt(), input.next()))
    input.close()
}

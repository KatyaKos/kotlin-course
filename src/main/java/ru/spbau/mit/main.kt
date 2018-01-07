package ru.spbau.mit

import java.io.BufferedReader
import java.io.InputStreamReader

fun main(args: Array<String>) {
    DebuggerCommands(BufferedReader(InputStreamReader(System.`in`)), System.out).start()
}
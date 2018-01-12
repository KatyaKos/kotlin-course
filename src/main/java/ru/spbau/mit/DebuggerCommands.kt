package ru.spbau.mit

import ru.spbau.mit.exceptions.DebugException
import ru.spbau.mit.exceptions.ParsingException
import ru.spbau.mit.exceptions.ScopeException
import ru.spbau.mit.interpreter.Debugger
import java.io.BufferedReader
import java.io.PrintStream

class DebuggerCommands(private val inn: BufferedReader, private val out: PrintStream) {
    fun start() {
        while (true) {
            val args = inn.readLine()?.split(' ') ?: break
            if (args[0] == "finish") {
                break
            }
            try {
                startCommand(args)
            } catch (e: DebugException) {
                System.err.println(e.message)
            } catch (e: ParsingException) {
                System.err.println(e.message)
            } catch (e: ScopeException) {
                System.err.println(e.message)
            }
        }
    }

    private fun startCommand(args: List<String>) {
        val debugger = Debugger(out)
        when (args[0]) {
            "load" -> debugger.load(args[1])
            "breakpoint" -> debugger.breakpoint(args[1].toInt())
            "condition" -> debugger.condition(args[1].toInt(), args[2])
            "list" -> debugger.list()
            "remove" -> debugger.remove(args[1].toInt())
            "run" -> debugger.run()
            "evaluate" -> debugger.evaluate(args[1])
            "stop" -> debugger.stop()
            "continue" -> debugger.continueProg()
            else -> System.err.println("No such command ${args[0]}")
        }
    }
}
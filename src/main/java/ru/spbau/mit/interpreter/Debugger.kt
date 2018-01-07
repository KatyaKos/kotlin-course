package ru.spbau.mit.interpreter

import kotlinx.coroutines.experimental.runBlocking
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.spbau.mit.exceptions.DebugException
import ru.spbau.mit.exceptions.ParsingException
import ru.spbau.mit.parser.FunLexer
import ru.spbau.mit.parser.FunParser
import java.io.PrintStream
import kotlin.coroutines.experimental.*

class Debugger(private val out: PrintStream) {

    private var ast: Ast? = null

    private var state: State = State(null, null, null, false, null)

    private val breakpoints: MutableMap<Int, Expression?> = hashMapOf()

    fun pause(line: Int, scope: Scope, continuation: Continuation<Unit>) {
        state = State(line, scope, continuation, true, null)
    }

    fun load(file: String) {
        ast = buildAst(CharStreams.fromFileName(file))
        breakpoints.clear()
    }

    fun breakpoint(line: Int) {
        breakpoints.put(line, null)
    }

    fun condition(line: Int, condition: String) {
        breakpoints.put(line, buildExpr(CharStreams.fromString(condition)))
    }

    fun list() {
        out.print("List of breakpoints:\n")
        breakpoints.forEach { line, condition ->
            run {
                if (condition == null) out.print("On line $line\n")
                else out.print("On line $line with condition $condition\n")
            }
        }
    }

    fun remove(line: Int) {
        breakpoints.remove(line)
    }

    fun run() {
        checkNotRunning()
        val func: suspend () -> Int? = {ast!!.accept(AstEvaluator(Scope(), breakpoints, this))}
        val continuation = func.createCoroutine(object : Continuation<Int?> {
            override val context: CoroutineContext = EmptyCoroutineContext

            override fun resume(value: Int?) {
                state = State(null, null, null, false, value)
            }

            override fun resumeWithException(exception: Throwable) {}
        })
        state = State(0, Scope(), continuation, true, null)
        continueProg()
    }

    fun evaluate(expression: String) {
        checkRunning()
        val expr = buildExpr(CharStreams.fromString(expression))
        val evaluator = AstEvaluator(state.scope!!, hashMapOf(), this)
        val res = runBlocking { expr.accept(evaluator) }
        out.print("Your result is $res\n")
    }

    fun stop() {
        checkRunning()
        state = State(null, null, null, false, null)
    }

    fun continueProg() {
        checkRunning()
        state.continuation!!.resume(Unit)
        if (!state.isRunning) {
            val res = state.result
            if (res == null) {
                out.print("Program finished. No value returned\n")
            } else {
                out.print("Program finished. Returned $res\n")
            }
        }
    }

    private fun buildAst(charStream: CharStream): Ast {
        val lexer = FunLexer(charStream)
        val parser = FunParser(CommonTokenStream(lexer))
        parser.buildParseTree = true
        if (parser.numberOfSyntaxErrors > 0) {
            throw ParsingException("Parsing problems occurred.")
        }
        return Visitor().visit(parser.file())
    }

    private fun buildExpr(charStream: CharStream): Expression {
        val ast = buildAst(charStream) as File
        val statements = ast.body.statements
        if (statements.size != 1) {
            throw ParsingException("Provided string is not expression.")
        }
        val statement = statements.first()
        if (statement is Expression) {
            return statement
        }
        throw ParsingException("Provided string is not expression.")
    }

    private fun checkRunning() {
        if (ast == null) {
            throw DebugException("Load file at first.")
        }
        if (!state.isRunning) {
            throw DebugException("Program is not running.")
        }
    }

    private fun checkNotRunning() {
        if (ast == null) {
            throw DebugException("Load file at first.")
        }
        if (state.isRunning) {
            throw DebugException("Program is already running.")
        }
    }
}

data class State(
        val line: Int?,
        val scope: Scope?,
        val continuation: Continuation<Unit>?,
        val isRunning: Boolean,
        val result: Int?)
package ru.spbau.mit

import kotlinx.coroutines.experimental.runBlocking
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.Test
import org.junit.After
import java.io.PrintStream
import org.junit.Before
import ru.spbau.mit.exceptions.ParsingException
import ru.spbau.mit.interpreter.*
import ru.spbau.mit.parser.FunLexer
import ru.spbau.mit.parser.FunParser
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class ProgramRunningTestClass {
    val name0 = "src/test/resources/easy.fun"
    val name1 = "src/test/resources/easy1.fun"
    val name2 = "src/test/resources/fib.fun"
    val name3 = "src/test/resources/foo.fun"

    private val out = ByteArrayOutputStream()

    @Before
    fun setUp() {
        System.setOut(PrintStream(out))
    }

    @After
    fun cleanUp() {
        System.setOut(null)
    }

    @Test
    fun testEasy1() {
        val ast = build(name0)
        assertEquals(ExampleAsts.easyAst0, ast)
        interpret(ast)
        assertEquals("1\n", out.toString())
    }

    @Test
    fun testEasy2() {
        val ast = build(name1)
        assertEquals(ExampleAsts.easyAst1, ast)
        interpret(ast)
        assertEquals("0\n", out.toString())
    }

    @Test
    fun testFib() {
        val ast = build(name2)
        assertEquals(ExampleAsts.fibAst, ast)
        interpret(ast)
        assertEquals("""
                    |1, 1
                    |2, 2
                    |3, 3
                    |4, 5
                    |5, 8
                    |""".trimMargin(), out.toString())
    }

    @Test
    fun testFoo() {
        val ast = build(name3)
        assertEquals(ExampleAsts.fooAst, ast)
        interpret(ast)
        assertEquals("42\n", out.toString())
    }

    fun build(file: String): Ast {
        val lexer = FunLexer(CharStreams.fromFileName(file))
        val parser = FunParser(CommonTokenStream(lexer))
        parser.buildParseTree = true
        if (parser.numberOfSyntaxErrors > 0) {
            throw ParsingException("Unable to parse file. Something is wrong with syntax")
        }
        return Visitor().visit(parser.file())
    }

    fun interpret(ast: Ast) {
        runBlocking { ast.accept(AstEvaluator(Scope(), hashMapOf(), Debugger(System.out))) }
    }
}
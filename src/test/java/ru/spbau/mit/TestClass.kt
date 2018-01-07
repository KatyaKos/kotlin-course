package ru.spbau.mit

import org.junit.Test
import org.junit.After
import java.io.PrintStream
import org.junit.Before
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class TestClass {
    val name0 = "src/test/resources/easy.fun"
    val name1 = "src/test/resources/easy1.fun"
    val name2 = "src/test/resources/fib.fun"
    val name3 = "src/test/resources/foo.fun"

    private val outContent = ByteArrayOutputStream()
    private val errContent = ByteArrayOutputStream()

    @Before
    fun setUpStreams() {
        System.setOut(PrintStream(outContent))
        System.setErr(PrintStream(errContent))
    }

    @After
    fun cleanUpStreams() {
        System.setOut(null)
        System.setErr(null)
    }

    @Test
    fun testEasy1() {
        val ast = build(name0)
        assertEquals(ExampleAsts.easyAst0, ast)
        interpret(ast)
        assertEquals("1\n", outContent.toString())
    }

    @Test
    fun testEasy2() {
        val ast = build(name1)
        assertEquals(ExampleAsts.easyAst1, ast)
        interpret(ast)
        assertEquals("0\n", outContent.toString())
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
                    |""".trimMargin(), outContent.toString())
    }

    @Test
    fun testFoo() {
        val ast = build(name3)
        assertEquals(ExampleAsts.fooAst, ast)
        interpret(ast)
        assertEquals("42\n", outContent.toString())
    }
}
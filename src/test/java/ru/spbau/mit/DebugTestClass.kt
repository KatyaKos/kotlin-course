package ru.spbau.mit

import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.spbau.mit.exceptions.DebugException
import ru.spbau.mit.interpreter.Debugger
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class DebuggerTest {

    private val out = ByteArrayOutputStream()
    private var byteOutputStream = ByteArrayOutputStream()
    private var printStream = PrintStream(byteOutputStream)
    private var debugger = Debugger(printStream)

    @Before
    fun setUp() {
        System.setOut(PrintStream(out))
        byteOutputStream = ByteArrayOutputStream()
        printStream = PrintStream(byteOutputStream)
        debugger = Debugger(printStream)
    }

    @After
    fun cleanUp() {
        System.setOut(null)
    }

    @Test
    fun listTest() {
        debugger.load("src/test/resources/easy1.fun")
        debugger.breakpoint(2)
        debugger.condition(3, "b<=10")
        debugger.breakpoint(6)
        debugger.list()
        val condition = "BinaryExpression(leftExpr=IdentifierExpression(name=b, line=1)," +
                " operation=<=, rightExpr=LiteralExpression(value=10, line=1), line=1)"
        assertEquals("""
                    |List of breakpoints:
                    |On line 2
                    |On line 3 with condition $condition
                    |On line 6
                    |""".trimMargin(), byteOutputStream.toString())
        debugger.remove(6)
        debugger.list()
        assertEquals("""
                    |List of breakpoints:
                    |On line 2
                    |On line 3 with condition $condition
                    |On line 6
                    |List of breakpoints:
                    |On line 2
                    |On line 3 with condition $condition
                    |""".trimMargin(), byteOutputStream.toString())
    }

    @Test
    fun evaluatorTest() {
        debugger.load("src/test/resources/fib.fun")
        debugger.condition(10, "b==4")
        debugger.run()
        debugger.evaluate("b")
        assertEquals("Your result is 4\n", byteOutputStream.toString())
        debugger.stop()
    }

    @Test
    fun breakpointTest() {
        debugger.load("src/test/resources/fib.fun")
        debugger.breakpoint(2)
        debugger.run()
        breakpointsFibHelper(3, """
                    |1, 1
                    |""")
        breakpointsFibHelper(5, """
                    |1, 1
                    |2, 2
                    |""")
    }

    @Test
    fun conditionTest() {
        debugger.load("src/test/resources/fib.fun")
        debugger.condition(10, "b==4")
        debugger.run()
        assertEquals("""
                    |1, 1
                    |2, 2
                    |3, 3
                    |""".trimMargin(), out.toString())
        debugger.continueProg()
        assertEquals("""
                    |1, 1
                    |2, 2
                    |3, 3
                    |4, 5
                    |5, 8
                    |""".trimMargin(), out.toString())
    }

    @Test(expected = DebugException::class)
    fun exceptionTest() {
        debugger.load("src/test/resources/easy.fun")
        debugger.run()
        debugger.continueProg()
    }

    @Test
    fun noExceptionTest() {
        debugger.load("src/test/resources/easy.fun")
        debugger.breakpoint(2)
        debugger.run()
        debugger.stop()
        debugger.run()
    }

    private fun breakpointsFibHelper(n: Int, res: String) {
        for (i in 1..n) {
            debugger.continueProg()
            assertEquals(res.trimMargin(), out.toString())
        }
    }
}
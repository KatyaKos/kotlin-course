package ru.spbau.mit

import ru.spbau.mit.elements.TexException
import org.junit.Test
import kotlin.test.assertEquals

class TestClass {
    @Test
    fun testNoDocument() {
        val res =
                start {
                    documentClass("letsplay")
                    usePackage("babel", "key=value")
                    usePackage("test")
                    usePackage("best", "Neil=Gaiman", "Mister=Sandman")
                }.toString()
        assertEquals("""
                    |\documentclass{letsplay}
                    |\usepackage[key=value]{babel}
                    |\usepackage{test}
                    |\usepackage[Neil=Gaiman,Mister=Sandman]{best}
                    |""".trimMargin(), res)
    }

    @Test(expected = TexException::class)
    fun testEmpty() {
        start {  }.toString()
    }

    @Test
    fun testSimpleDocument() {
        val res =
                start {
                    documentClass("letsplay")
                    usePackage("babel", "key=value")
                    usePackage("test")
                    usePackage("best", "Neil=Gaiman", "Mister=Sandman")
                    document {
                        +"Bring me a dream"
                    }
                }.toString()
        assertEquals("""
                    |\documentclass{letsplay}
                    |\usepackage[key=value]{babel}
                    |\usepackage{test}
                    |\usepackage[Neil=Gaiman,Mister=Sandman]{best}
                    |\begin{document}
                    |    Bring me a dream
                    |\end{document}
                    |""".trimMargin(), res)
    }
}
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

    @Test(expected = TexException::class)
    fun testClasses() {
        start {
            documentClass("letsplay")
            documentClass("onegame")
        }.toString()
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

    @Test
    fun testAlignment() {
        val res =
                start {
                    documentClass("Mister Sandman")
                    document {
                        +"Bring me a dream"
                        flushLeft { +"bung" }
                        center { +"bung" }
                        flushRight { +"bung" }
                        +"bung"
                    }
                }.toString()
        assertEquals("""
                    |\documentclass{Mister Sandman}
                    |\begin{document}
                    |    Bring me a dream
                    |    \begin{flushleft}
                    |        bung
                    |    \end{flushleft}
                    |    \begin{center}
                    |        bung
                    |    \end{center}
                    |    \begin{flushright}
                    |        bung
                    |    \end{flushright}
                    |    bung
                    |\end{document}
                    |""".trimMargin(), res)
    }

    @Test
    fun testListing() {
        val res =
                start {
                    documentClass("Mister Sandman")
                    document {
                        +"Bring me a dream"
                        enumerate {
                            item { +"bung" }
                            item { +"bung" }
                            item { +"bung" }
                            item { +"bung" }
                        }
                        +"Make him the cutest that I've ever seen"
                        itemize {
                            item { +"bung" }
                            item { +"bung" }
                            item { +"bung" }
                            item { +"bung" }
                        }
                    }
                }.toString()
        assertEquals("""
                    |\documentclass{Mister Sandman}
                    |\begin{document}
                    |    Bring me a dream
                    |    \begin{enumerate}
                    |        \item
                    |            bung
                    |        \item
                    |            bung
                    |        \item
                    |            bung
                    |        \item
                    |            bung
                    |    \end{enumerate}
                    |    Make him the cutest that I've ever seen
                    |    \begin{itemize}
                    |        \item
                    |            bung
                    |        \item
                    |            bung
                    |        \item
                    |            bung
                    |        \item
                    |            bung
                    |    \end{itemize}
                    |\end{document}
                    |""".trimMargin(), res)
    }

    @Test
    fun testMath() {
        val res =
                start {
                    documentClass("Mister Sandman")
                    document {
                        +"Bring me a dream"
                        center {
                            +"bung x"
                            math { +"(1+1)*2" }
                        }
                    }
                }.toString()
        assertEquals("""
                    |\documentclass{Mister Sandman}
                    |\begin{document}
                    |    Bring me a dream
                    |    \begin{center}
                    |        bung x
                    |        \begin{math}
                    |            (1+1)*2
                    |        \end{math}
                    |    \end{center}
                    |\end{document}
                    |""".trimMargin(), res)
    }

    @Test
    fun testFrame() {
        val res =
                start {
                    documentClass("Mister Sandman")
                    document {
                        frame("Mister Sandman", "Song", "dream") {
                            +"Mr. Sandman, bring me a dream (bung, bung, bung, bung)"
                            +"Make him the cutest that I've ever seen (bung, bung, bung, bung)"
                            +"Give him two lips like roses and clover (bung, bung, bung, bung)"
                            +"Then tell him that his lonesome nights are over"
                            +"Sandman, I'm so alone (bung, bung, bung, bung)"
                            +"Don't have nobody to call my own (bung, bung, bung, bung)"
                            +"Please turn on your magic beam"
                            +"Mr. Sandman, bring me a dream"
                        }
                    }
                }.toString()
        assertEquals("""
                    |\documentclass{Mister Sandman}
                    |\begin{document}
                    |    \begin{frame}[Song,dream]{Mister Sandman}
                    |        Mr. Sandman, bring me a dream (bung, bung, bung, bung)
                    |        Make him the cutest that I've ever seen (bung, bung, bung, bung)
                    |        Give him two lips like roses and clover (bung, bung, bung, bung)
                    |        Then tell him that his lonesome nights are over
                    |        Sandman, I'm so alone (bung, bung, bung, bung)
                    |        Don't have nobody to call my own (bung, bung, bung, bung)
                    |        Please turn on your magic beam
                    |        Mr. Sandman, bring me a dream
                    |    \end{frame}
                    |\end{document}
                    |""".trimMargin(), res)
    }

    @Test
    fun testCommand() {
        val res =
                start {
                    documentClass("Mister Sandman")
                    document {
                        inlineCommand("helloMisterSandman")
                        blockCommand("bringMeADream", listOf("bung3", "bung4"), "bung1", "bung2") {
                            inlineCommand("makeHimTheCutest")
                            inlineCommand("giveHimTwoLips", listOf("roses", "clover"))
                            inlineCommand("tellHimLonesomeOver")
                            blockCommand("DearSandman") {
                                +"Sandman, I'm so alone (bung, bung, bung, bung)"
                                +"Don't have nobody to call my own (bung, bung, bung, bung)"
                                inlineCommand("turnOnYourMagicBeam")
                                +"Mr. Sandman, bring me a dream"
                            }
                        }
                    }
                }.toString()
        assertEquals("""
                    |\documentclass{Mister Sandman}
                    |\begin{document}
                    |    \helloMisterSandman
                    |    \begin{bringMeADream}[bung1,bung2]{bung3}{bung4}
                    |        \makeHimTheCutest
                    |        \giveHimTwoLips{roses}{clover}
                    |        \tellHimLonesomeOver
                    |        \begin{DearSandman}
                    |            Sandman, I'm so alone (bung, bung, bung, bung)
                    |            Don't have nobody to call my own (bung, bung, bung, bung)
                    |            \turnOnYourMagicBeam
                    |            Mr. Sandman, bring me a dream
                    |        \end{DearSandman}
                    |    \end{bringMeADream}
                    |\end{document}
                    |""".trimMargin(), res)
    }
}
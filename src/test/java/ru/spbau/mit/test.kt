package ru.spbau.mit

import kotlin.test.assertEquals
import org.junit.Test

class TestSource {
    @Test
    fun sampleTests() {
        assertEquals("IMPOSSIBLE", PatternFiller(3, "a?c").solve())
        assertEquals("abba", PatternFiller(2, "a??a").solve())
        assertEquals("abba", PatternFiller(2, "?b?a").solve())
        assertEquals("aaa", PatternFiller(1, "a?a").solve())
        assertEquals("aabaa", PatternFiller(2, "a???a").solve())
    }
}
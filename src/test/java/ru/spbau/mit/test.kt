package ru.spbau.mit

import kotlin.test.assertEquals
import org.junit.Test

class TestSource {
    @Test
    fun sampleTests() {
        assertEquals("IMPOSSIBLE", solve(3, "a?c"))
        assertEquals("abba", solve(2, "a??a"))
        assertEquals("abba", solve(2, "?b?a"))
        assertEquals("aaa", solve(1, "a?a"))
        assertEquals("aabaa", solve(2, "a???a"))
    }
}
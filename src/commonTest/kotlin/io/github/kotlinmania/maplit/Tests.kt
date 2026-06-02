// port-lint: source tests/tests.rs
package io.github.kotlinmania.maplit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Tests {
    @Test
    fun testParse() {
        val m = hashmap<Int, Int>()
        m[1] = 1
        hashmap(1 to 1)
        hashmap(1 to 1)
        hashmap(1 + 1 to 1, 2 + 1 to 2)
        hashmap(1 + 1 to 1, 2 + 1 to 2)
        hashmap(((1 + 2) to 1), ((1 + 3) to (0 + 2)))
        val mm = hashmap("a" to 1 + 2, "b" to 1 + 3)
        assertEquals(3, mm["a"])
        assertEquals(4, mm["b"])
        val mm2 = hashmap("a" to 1 + 2, "b" to 1 + 3)
        assertEquals(3, mm2["a"])
        assertEquals(4, mm2["b"])

        val s = hashset<Int>()
        s.add(1)
        hashset(1)
        hashset(1)
        hashset(1, 2)
        hashset(1, 2)
        hashset(1 + 1, 2 + 1)
        hashset(1 + 1, 2 + 1)
        hashset(1 + 1, 2 + 1)
    }

    @Test
    fun hashset() {
        val empty = hashset<Int>()
        assertTrue(empty.isEmpty())
        empty.add(2)
        val one = hashset(1)
        assertEquals(1, one.size)
        val two = hashset(2, 3)
        assertEquals(2, two.size)
        // Test that we can use many elements without hitting any recursion limit.
        val set = hashset(*List(2000) { it % 10 }.toTypedArray())
        assertEquals(10, set.size)
    }
}

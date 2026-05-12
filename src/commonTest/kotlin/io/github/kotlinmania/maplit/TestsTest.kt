// port-lint: source tests/tests.rs
package io.github.kotlinmania.maplit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestsTest {
    @Test
    fun testParse() {
        val m: HashMap<Int, Int> = hashmap()
        m[1] = 1
        hashmap(1 to 1)
        hashmap(1 to 1)
        hashmap(1 + 1 to 1, 2 + 1 to 2)
        hashmap(1 + 1 to 1, 2 + 1 to 2)
        hashmap((1 + 2) to 1, (1 + 3) to (0 + 2))
        val m2 = hashmap("a" to 1 + 2, "b" to 1 + 3)
        assertEquals(3, m2["a"])
        assertEquals(4, m2["b"])
        val m3 = hashmap("a" to 1 + 2, "b" to 1 + 3)
        assertEquals(3, m3["a"])
        assertEquals(4, m3["b"])

        val s: HashSet<Int> = hashset()
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
    fun testHashset() {
        val emptySet: HashSet<Int> = hashset()
        assertTrue(emptySet.isEmpty())
        emptySet.add(2)
        val singleton = hashset(1)
        assertEquals(1, singleton.size)
        val pair = hashset(2, 3)
        assertEquals(2, pair.size)
        // Test that we can use many elements without hitting the macro recursion
        // limit. Kotlin's vararg does not enforce a recursion-style limit, but
        // the upstream test stresses the same shape and the assertion is
        // preserved.
        val set = hashset(
            1,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
            2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
        )
        assertEquals(10, set.size)
    }
}

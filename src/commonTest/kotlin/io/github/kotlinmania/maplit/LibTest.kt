// port-lint: source src/lib.rs
package io.github.kotlinmania.maplit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LibTest {
    @Test
    fun testHashmap() {
        val names = hashmap(
            1 to "one",
            2 to "two",
        )
        assertEquals(2, names.size)
        assertEquals("one", names[1])
        assertEquals("two", names[2])
        assertNull(names[3])

        val empty: HashMap<Int, Int> = hashmap()
        assertEquals(0, empty.size)

        val nestedCompiles = hashmap(
            1 to hashmap(0 to 1 + 2),
            2 to hashmap(1 to 1),
        )
        assertEquals(2, nestedCompiles.size)

        val converted: HashMap<String, Int> = convertArgsHashmap(
            keys = { s: String -> s },
            values = ::id,
            "one" to 1,
            "two" to 2,
        )
        assertEquals(2, converted.size)

        val convertedExplicitId: HashMap<String, Int> = convertArgsHashmap(
            keys = { s: String -> s },
            values = ::id,
            "one" to 1,
            "two" to 2,
        )
        assertEquals(2, convertedExplicitId.size)

        val names2: HashSet<String> = convertArgsHashset(
            keys = ::id,
            "one",
            "two",
        )
        assertTrue(names2.contains("one"))
        assertTrue(names2.contains("two"))

        val lengths: HashSet<Int> = convertArgsHashset(
            keys = { s: String -> s.length },
            "one",
            "two",
        )
        assertEquals(1, lengths.size)

        val noTrailing: HashSet<Int> = convertArgsHashset(
            keys = { s: String -> s.length },
            "one",
            "two",
        )
        assertEquals(1, noTrailing.size)
    }

    @Test
    fun testBtreemap() {
        val names = btreemap(
            1 to "one",
            2 to "two",
        )
        assertEquals(2, names.size)
        assertEquals("one", names[1])
        assertEquals("two", names[2])
        assertNull(names[3])

        val empty: io.github.kotlinmania.btree.BTreeMap<Int, Int> = btreemap()
        assertEquals(0, empty.size)

        val nestedCompiles = btreemap(
            1 to btreemap(0 to 1 + 2),
            2 to btreemap(1 to 1),
        )
        assertEquals(2, nestedCompiles.size)
    }
}

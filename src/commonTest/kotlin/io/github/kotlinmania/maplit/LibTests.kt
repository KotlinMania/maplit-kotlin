// port-lint: source src/lib.rs
package io.github.kotlinmania.maplit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LibTests {
    @Test
    fun testHashmap() {
        val names = hashmap(
            1 to "one",
            2 to "two",
        )
        assertEquals(2, names.size)
        assertEquals("one", names[1])
        assertEquals("two", names[2])
        assertEquals(null, names[3])

        val empty: HashMap<Int, Int> = hashmap()
        assertEquals(0, empty.size)

        val nestedCompiles = hashmap(
            1 to hashmap(0 to 1 + 2),
            2 to hashmap(1 to 1),
        )
        assertEquals(3, nestedCompiles[1]!![0])
        assertEquals(1, nestedCompiles[2]!![1])

        val converted: HashMap<String, Int> = convertArgsHashmap(
            keys = ::id,
            values = ::id,
            "one" to 1,
            "two" to 2,
        )
        assertEquals(1, converted["one"])
        assertEquals(2, converted["two"])

        val convertedExplicitId: HashMap<String, Int> = convertArgsHashmap(
            keys = ::id,
            values = ::id,
            "one" to 1,
            "two" to 2,
        )
        assertEquals(1, convertedExplicitId["one"])

        val nameStrings: HashSet<String> = convertArgsHashset(
            keys = ::id,
            "one",
            "two",
        )
        assertTrue("one" in nameStrings)
        assertTrue("two" in nameStrings)

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
        assertEquals(null, names[3])

        val empty: io.github.kotlinmania.btree.BTreeMap<Int, Int> = btreemap()
        assertEquals(0, empty.size)

        val nestedCompiles = btreemap(
            1 to btreemap(0 to 1 + 2),
            2 to btreemap(1 to 1),
        )
        assertEquals(3, nestedCompiles[1]!![0])
        assertEquals(1, nestedCompiles[2]!![1])
    }
}

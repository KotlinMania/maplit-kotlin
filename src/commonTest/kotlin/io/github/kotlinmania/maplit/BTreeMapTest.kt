package io.github.kotlinmania.maplit

import io.github.kotlinmania.btree.BTreeMap
import io.github.kotlinmania.btree.BTreeSet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BTreeMapTest {
    @Test
    fun testBtreemap() {
        val names =
            btreemap(
                1 to "one",
                2 to "two",
            )
        assertEquals(2, names.size)
        assertEquals("one", names[1])
        assertEquals("two", names[2])
        assertNull(names[3])

        val empty: BTreeMap<Int, Int> = btreemap()
        assertEquals(0, empty.size)

        val nestedCompiles =
            btreemap(
                1 to btreemap(0 to 1 + 2),
                2 to btreemap(1 to 1),
            )
        assertEquals(2, nestedCompiles.size)
    }

    @Test
    fun testBtreeset() {
        val set = btreeset("a", "b")
        assertEquals(2, set.size)
        assertTrue("a" in set)
        assertTrue("b" in set)
        assertTrue("c" !in set)

        val convertedSet: BTreeSet<String> =
            convertArgsBtreeset(
                keys = { s: String -> s.uppercase() },
                "a",
                "b",
                "c",
                "d",
                "a",
                "e",
                "f",
            )
        assertEquals(6, convertedSet.size)
    }
}

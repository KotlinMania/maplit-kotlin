@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.maplit

import io.github.kotlinmania.btree.BTreeSet

/**
 * Create a [BTreeSet] from a list of elements.
 *
 * ## Example
 *
 * ```
 * import io.github.kotlinmania.maplit.btreeset
 *
 * val set = btreeset("a", "b")
 * check("a" in set)
 * check("b" in set)
 * check("c" !in set)
 * ```
 */
@kotlin.native.HiddenFromObjC
fun <T> btreeset(vararg elements: T): BTreeSet<T> {
    val set = BTreeSet<T>()
    for (element in elements) {
        set.add(element)
    }
    return set
}

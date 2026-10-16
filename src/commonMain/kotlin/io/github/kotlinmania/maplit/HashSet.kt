@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.maplit

/**
 * Create a [HashSet] from a list of elements.
 *
 * ## Example
 *
 * ```
 * import io.github.kotlinmania.maplit.hashset
 *
 * val set = hashset("a", "b")
 * check("a" in set)
 * check("b" in set)
 * check("c" !in set)
 * ```
 */
@kotlin.native.HiddenFromObjC
fun <T> hashset(vararg elements: T): HashSet<T> {
    val set = HashSet<T>()
    for (element in elements) {
        set.add(element)
    }
    return set
}

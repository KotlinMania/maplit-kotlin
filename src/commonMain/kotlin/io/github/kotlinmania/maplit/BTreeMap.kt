@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.maplit

import io.github.kotlinmania.btree.BTreeMap

/**
 * Create a [BTreeMap] from a list of key-value pairs.
 *
 * ## Example
 *
 * ```
 * import io.github.kotlinmania.maplit.btreemap
 *
 * val map = btreemap(
 *     "a" to 1,
 *     "b" to 2,
 * )
 * check(map["a"] == 1)
 * check(map["b"] == 2)
 * check(map["c"] == null)
 * ```
 */
@kotlin.native.HiddenFromObjC
fun <K, V> btreemap(vararg entries: Pair<K, V>): BTreeMap<K, V> {
    val map = BTreeMap<K, V>()
    for ((key, value) in entries) {
        map[key] = value
    }
    return map
}

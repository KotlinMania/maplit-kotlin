@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.maplit

/**
 * Create a [HashMap] from a list of key-value pairs.
 *
 * ## Example
 *
 * ```
 * import io.github.kotlinmania.maplit.hashmap
 *
 * val map = hashmap(
 *     "a" to 1,
 *     "b" to 2,
 * )
 * check(map["a"] == 1)
 * check(map["b"] == 2)
 * check(map["c"] == null)
 * ```
 */
@kotlin.native.HiddenFromObjC
fun <K, V> hashmap(vararg entries: Pair<K, V>): HashMap<K, V> {
    val map = HashMap<K, V>()
    for ((key, value) in entries) {
        map[key] = value
    }
    return map
}

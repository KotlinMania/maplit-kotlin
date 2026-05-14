// port-lint: source src/lib.rs
package io.github.kotlinmania.maplit

import io.github.kotlinmania.btree.BTreeMap
import io.github.kotlinmania.btree.BTreeSet

/**
 * Helpers for container literals with specific type.
 *
 * ```
 * import io.github.kotlinmania.maplit.hashmap
 *
 * val map = hashmap(
 *     "a" to 1,
 *     "b" to 2,
 * )
 * ```
 *
 * The **maplit** crate uses the `=>` separator between key and value in its
 * Rust macro syntax. The Kotlin port spells the same shape with the standard
 * library [Pair] (the `to` infix operator), so each entry is `key to value`.
 *
 * Generic container helpers already exist in Kotlin's standard library, so
 * those are not re-exported here.
 */

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
fun <K, V> hashmap(vararg entries: Pair<K, V>): HashMap<K, V> {
    val map = HashMap<K, V>()
    for ((key, value) in entries) {
        map[key] = value
    }
    return map
}

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
fun <T> hashset(vararg elements: T): HashSet<T> {
    val set = HashSet<T>()
    for (element in elements) {
        set.add(element)
    }
    return set
}

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
fun <K, V> btreemap(vararg entries: Pair<K, V>): BTreeMap<K, V> {
    val map = BTreeMap<K, V>()
    for ((key, value) in entries) {
        map[key] = value
    }
    return map
}

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
fun <T> btreeset(vararg elements: T): BTreeSet<T> {
    val set = BTreeSet<T>()
    for (element in elements) {
        set.add(element)
    }
    return set
}

/** Identity function. Used as the fallback for conversion. */
fun <T> id(t: T): T = t

/**
 * Helpers that convert the keys or key-value pairs passed to another maplit
 * helper. These are the runtime-API counterpart of the upstream
 * `convert_args!` macro: each container helper has a sibling that takes a
 * `keys` and `values` lambda and applies them to every entry before
 * inserting.
 *
 * The Kotlin port pulls the conversion functions to the front of each
 * helper's parameter list and leaves the entries as a trailing vararg, so a
 * call site reads:
 *
 * ```
 * val map: HashMap<String, Int> = convertArgsHashmap(
 *     keys = { it: String -> it },
 *     values = ::id,
 *     "one" to 1,
 *     "two" to 2,
 * )
 * ```
 *
 * Pass [id] for either lambda to leave the corresponding side untouched. The
 * upstream macro defaults to `Into::into` for both lambdas; Kotlin has no
 * matching auto-conversion trait, so the conversion functions are always
 * explicit at the call site.
 */
fun <K, V, NK, NV> convertArgsHashmap(
    keys: (K) -> NK,
    values: (V) -> NV,
    vararg entries: Pair<K, V>,
): HashMap<NK, NV> {
    val map = HashMap<NK, NV>()
    for ((key, value) in entries) {
        map[keys(key)] = values(value)
    }
    return map
}

/** Conversion-aware sibling of [hashset] (see [convertArgsHashmap]). */
fun <T, NT> convertArgsHashset(
    keys: (T) -> NT,
    vararg elements: T,
): HashSet<NT> {
    val set = HashSet<NT>()
    for (element in elements) {
        set.add(keys(element))
    }
    return set
}

/** Conversion-aware sibling of [btreemap] (see [convertArgsHashmap]). */
fun <K, V, NK, NV> convertArgsBtreemap(
    keys: (K) -> NK,
    values: (V) -> NV,
    vararg entries: Pair<K, V>,
): BTreeMap<NK, NV> {
    val map = BTreeMap<NK, NV>()
    for ((key, value) in entries) {
        map[keys(key)] = values(value)
    }
    return map
}

/** Conversion-aware sibling of [btreeset] (see [convertArgsHashmap]). */
fun <T, NT> convertArgsBtreeset(
    keys: (T) -> NT,
    vararg elements: T,
): BTreeSet<NT> {
    val set = BTreeSet<NT>()
    for (element in elements) {
        set.add(keys(element))
    }
    return set
}

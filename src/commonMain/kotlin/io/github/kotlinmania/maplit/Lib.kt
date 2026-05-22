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

/**
 * Identity function. Used as the fallback for conversion.
 *
 * This is the Kotlin counterpart of the upstream `__id` function, which serves as a
 * fallback when no custom conversion is needed. In the upstream Rust `convert_args!`
 * macro, conversions default to `Into::into`; in the Kotlin port, conversions are
 * always explicit, and this function provides a no-op conversion.
 */
fun <T> id(t: T): T = t

/**
 * Helper that converts the keys or key-value pairs passed to another maplit
 * helper. This is the runtime-API counterpart of the upstream `convert_args!` macro.
 *
 * The upstream `convert_args!` macro has the following syntax:
 *
 * ```
 * convert_args!(keys=function, values=function, macro_name!(key => value, ...))
 * ```
 *
 * where either or both of the explicit `keys=` and `values=` parameters can be omitted,
 * defaulting to `Into::into`.
 *
 * The Kotlin port provides separate conversion-aware functions for each container type:
 * [convertArgsHashmap], [convertArgsHashset], [convertArgsBtreemap], [convertArgsBtreeset].
 * Each pulls the conversion functions to the front of the parameter list and leaves the
 * entries as a trailing vararg.
 *
 * ## Examples
 *
 * ```
 * // a. Explicit conversion for both keys and values.
 * // The upstream would write this as:
 * //   let map1: HashMap<String, String> = convert_args!(hashmap!("a" => "b", "c" => "d"));
 * // with default Into::into conversions. In Kotlin we write:
 * val map1: HashMap<String, String> = convertArgsHashmap(
 *     keys = String::toString,
 *     values = String::toString,
 *     "a" to "b",
 *     "c" to "d",
 * )
 *
 * // b. Specify an explicit custom conversion for the keys. If we don't specify
 * // a conversion for the values, pass id to leave them unchanged.
 * // The upstream Rust would write:
 * //   let map2 = convert_args!(keys=String::from, hashmap!("a" => 1, "c" => 2));
 * // In Kotlin:
 * val map2 = convertArgsHashmap(
 *     keys = { s: String -> s },
 *     values = ::id,
 *     "a" to 1,
 *     "c" to 2,
 * )
 * // Note: map2 is a HashMap<String, Int>, but we didn't need to specify the type
 * val _: HashMap<String, Int> = map2
 *
 * // c. Conversion-aware helpers work with all the maplit container types.
 * // For example, converting strings to byte arrays in a BTreeSet.
 * // The upstream Rust would write:
 * //   let set: BTreeSet<Vec<u8>> = convert_args!(btreeset!("a", "b", "c", "d", "a", "e", "f"));
 * // In Kotlin:
 * val set: io.github.kotlinmania.btree.BTreeSet<ByteArray> = convertArgsBtreeset(
 *     keys = { s: String -> s.encodeToByteArray() },
 *     "a", "b", "c", "d", "a", "e", "f",
 * )
 * check(set.size == 6)
 * ```
 *
 * Pass [id] for either lambda to leave the corresponding side untouched. Kotlin has no
 * matching auto-conversion trait equivalent to Rust's `Into`, so the conversion functions
 * are always explicit at the call site.
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

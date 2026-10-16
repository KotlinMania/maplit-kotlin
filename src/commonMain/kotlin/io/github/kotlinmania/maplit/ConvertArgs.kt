@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.maplit

import io.github.kotlinmania.btree.BTreeMap
import io.github.kotlinmania.btree.BTreeSet

/**
 * Identity function. Used as the fallback for conversion.
 *
 * The upstream private identity helper served as the default conversion in the
 * conversion-args macro. In the Kotlin port, conversions are always explicit, and
 * this function provides a no-op conversion to pass when the caller does not want
 * to change one of the sides.
 */
@kotlin.native.HiddenFromObjC
fun <T> id(t: T): T = t

/**
 * Helpers that convert the keys or key-value pairs passed to another maplit
 * helper. These are the runtime-API counterpart of the upstream
 * `convertArgs` macro: each container helper has a sibling that takes a
 * `keys` and `values` lambda and applies them to every entry before
 * inserting.
 *
 * The upstream macro takes `keys=` and `values=` named parameters around a nested
 * container macro invocation, with either or both defaulting to the standard
 * library `Into` conversion. The Kotlin port pulls the conversion functions to the
 * front of each parameter list and leaves the entries as a trailing vararg:
 * [convertArgsHashmap], [convertArgsHashset], [convertArgsBtreemap], and
 * [convertArgsBtreeset].
 *
 * ## Examples
 *
 * ```
 * // a. Explicit conversion for both keys and values.
 * val map1: HashMap<String, String> = convertArgsHashmap(
 *     keys = String::toString,
 *     values = String::toString,
 *     "a" to "b",
 *     "c" to "d",
 * )
 *
 * // b. Explicit custom conversion for the keys, identity for the values.
 * val map2 = convertArgsHashmap(
 *     keys = { s: String -> s },
 *     values = ::id,
 *     "a" to 1,
 *     "c" to 2,
 * )
 * // Note: map2 is a HashMap<String, Int>, but we didn't need to specify the type.
 * val mapTyped: HashMap<String, Int> = map2
 *
 * // c. Conversion-aware helpers work with all the maplit container types.
 * // For example, converting strings to uppercase in a BTreeSet.
 * val set: io.github.kotlinmania.btree.BTreeSet<String> = convertArgsBtreeset(
 *     keys = { s: String -> s.uppercase() },
 *     "a", "b", "c", "d", "a", "e", "f",
 * )
 * check(set.size == 6)
 * ```
 *
 * Pass [id] for either lambda to leave the corresponding side untouched. Kotlin has
 * no auto-conversion analog of the standard-library `Into` trait, so the conversion
 * functions are always explicit at the call site.
 */
@kotlin.native.HiddenFromObjC
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
@kotlin.native.HiddenFromObjC
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
@kotlin.native.HiddenFromObjC
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
@kotlin.native.HiddenFromObjC
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

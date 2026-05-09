// port-lint: source src/lib.rs
package io.github.kotlinmania.maplit

import io.github.kotlinmania.btree.BTreeMap
import io.github.kotlinmania.btree.BTreeSet

/*
 * Container literal builders with specific type.
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
 * The upstream **maplit** crate uses `=>` syntax to separate the key and value
 * for the mapping macros. (It was not possible to use `:` as separator due to
 * syntactic restrictions in regular `macro_rules!` macros.) The Kotlin port
 * carries each `=>` over as Kotlin's built-in `to` infix on [Pair].
 *
 * Rust macros are flexible in which brackets you use for the invocation, so the
 * upstream calls may appear as `hashmap!{}` or `hashmap![]` or `hashmap!()`.
 * The Kotlin builders are ordinary functions; spelling the call site uses
 * parentheses unconditionally.
 *
 * Generic container builders already exist elsewhere, so those are not provided
 * here at the moment.
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
public fun <K, V> hashmap(vararg pairs: Pair<K, V>): HashMap<K, V> {
    val cap = pairs.size
    val map = HashMap<K, V>(cap)
    for ((key, value) in pairs) {
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
 * check(set.contains("a"))
 * check(set.contains("b"))
 * check(!set.contains("c"))
 * ```
 */
public fun <T> hashset(vararg elements: T): HashSet<T> {
    val cap = elements.size
    val set = HashSet<T>(cap)
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
public fun <K, V> btreemap(vararg pairs: Pair<K, V>): BTreeMap<K, V> {
    val map = BTreeMap<K, V>()
    for ((key, value) in pairs) {
        map.insert(key, value)
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
 * check(set.contains("a"))
 * check(set.contains("b"))
 * check(!set.contains("c"))
 * ```
 */
public fun <T> btreeset(vararg elements: T): BTreeSet<T> {
    val set = BTreeSet<T>()
    for (element in elements) {
        set.insert(element)
    }
    return set
}

/**
 * Identity function. Used as the fallback for conversion.
 *
 * Spelled `__id` in the upstream crate (and marked `doc(hidden)`); the Kotlin
 * port renames it to [identity] so that callers can spell it without the
 * leading double underscore. [convertArgs] still routes to this function as
 * the "no conversion" choice for keys or values.
 */
public fun <T> identity(t: T): T = t

/**
 * Companion to the maplit container builders that converts the keys and
 * values passed to them.
 *
 * Unlike the upstream `convert_args!` macro, the Kotlin form is an ordinary
 * function that returns a freshly built [Array] of [Pair]. Callers spread the
 * returned array into the desired builder.
 *
 * The upstream macro accepts either or both of `keys=` and `values=`, with
 * the default conversion being [`Into::into`](https://doc.rust-lang.org/std/convert/trait.Into.html).
 * Kotlin has no portable equivalent of the `Into` trait, so the Kotlin port
 * makes both transforms explicit; pass [identity] on either side to leave it
 * untouched. The single-element shape used by the set builders is spelled
 * [convertArgsElements] so that overload resolution does not collide with
 * the key-value form below.
 *
 * ## Examples
 *
 * ```
 * import io.github.kotlinmania.maplit.convertArgs
 * import io.github.kotlinmania.maplit.hashmap
 * import io.github.kotlinmania.maplit.identity
 *
 * // a. Use the default conversion ([identity] on both sides). The Rust
 * // version uses `Into::into`, but Kotlin is explicit about conversions:
 * // pass an explicit transform when one is needed.
 *
 * val map1: HashMap<String, String> = hashmap(
 *     *convertArgs(keys = ::identity, values = ::identity, "a" to "b", "c" to "d"),
 * )
 *
 * // b. Specify an explicit custom conversion for the keys. If `values` is
 * // not meant to change, pass [identity] there.
 *
 * val map2: HashMap<String, Int> = hashmap(
 *     *convertArgs(keys = { s: String -> s }, values = ::identity,
 *         "a" to 1, "c" to 2),
 * )
 *
 * // c. [convertArgs] works with all the maplit builders that take pairs;
 * // single-element builders use [convertArgsElements].
 * ```
 *
 * **Note:** The `macro_name` parameter that the upstream macro takes is not
 * needed in Kotlin: callers wrap the call in their builder of choice
 * directly.
 */
public inline fun <K, V, K2, V2> convertArgs(
    keys: (K) -> K2,
    values: (V) -> V2,
    vararg pairs: Pair<K, V>,
): Array<Pair<K2, V2>> = Array(pairs.size) { i ->
    val (k, v) = pairs[i]
    keys(k) to values(v)
}

/**
 * Single-element companion to [convertArgs] for the set-style builders
 * [hashset] and [btreeset]. Mirrors `convert_args!(keys=$kf, hashset!(...))`
 * — the upstream macro reuses the `keys=` parameter for the only conversion
 * function in the set form, so this Kotlin overload is named distinctly to
 * avoid colliding with the key-value form at overload resolution.
 *
 * ## Example
 *
 * ```
 * import io.github.kotlinmania.maplit.btreeset
 * import io.github.kotlinmania.maplit.convertArgsElements
 *
 * // Convert each `String` to `List<Byte>` (the upstream test uses `Vec<u8>`).
 * val set: BTreeSet<List<Byte>> = btreeset(
 *     *convertArgsElements({ s: String -> s.encodeToByteArray().toList() },
 *         "a", "b", "c", "d", "a", "e", "f"),
 * )
 * check(set.size == 6)
 * ```
 */
public inline fun <T, reified R> convertArgsElements(
    transform: (T) -> R,
    vararg elements: T,
): Array<R> = Array(elements.size) { i -> transform(elements[i]) }

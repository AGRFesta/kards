package agrfesta.k.cards.playingcards.utils

/**
 * Any [value] is transformed in a [Int] included into the interval [0, [List.size]-1], the transformation
 * considers the interval circular (after the last is again the first).
 * For example, if [List] has a size of 3:
 *  - a [value] of 3 -> 0
 *  - a [value] of 2 -> 2
 *  - a [value] of 0 -> 0
 *  - a [value] of -1 -> 2
 *
 * Throws [IllegalStateException] if [List] is empty.
 */
fun <T> List<T>.circularIndexMapping(value: Int): Int {
    check(isNotEmpty()) { "Trying to map a circular index on an empty List" }
    return  ((value % size) + size) % size
}

/**
 * The [List] is considered circular, will always return an element included into the interval [0, [List.size]-1] and
 * never throw a [IndexOutOfBoundsException].
 * Case of a [List] of size 3:
 * [value] -> ... -6 -5 -4  -3 -2 -1  0 1 2  3 4 5  6 7 8 ...
 *           ... [0  1  2 ][0  1  2 ][0 1 2][0 1 2][0 1 2] ...
 *
 * Throws [IllegalStateException] if [List] is empty.
 */
fun <T> List<T>.circularIndex(value: Int): T = this[circularIndexMapping(value)]

/**
 * Any [value] is transformed in a [Int] included into the interval [0, [Array.size]-1], the transformation
 * considers the interval circular (after the last is again the first).
 * For example, if [Array] has a size of 3:
 *  - a [value] of 3 -> 0
 *  - a [value] of 2 -> 2
 *  - a [value] of 0 -> 0
 *  - a [value] of -1 -> 2
 *
 * Throws [IllegalStateException] if [Array] is empty.
 */
fun <T> Array<T>.circularIndexMapping(value: Int): Int {
    check(isNotEmpty()) { "Trying to map a circular index on an empty Array" }
    return ((value % size) + size) % size
}

/**
 * The [Array] is considered circular, will always return an element included into the interval [0, [Array.size]-1] and
 * never throw a [IndexOutOfBoundsException].
 * Case of a [Array] of size 3:
 * [value] -> ... -6 -5 -4  -3 -2 -1  0 1 2  3 4 5  6 7 8 ...
 *           ... [0  1  2 ][0  1  2 ][0 1 2][0 1 2][0 1 2] ...
 *
 * Throws [IllegalStateException] if [Array] is empty.
 */
fun <T> Array<T>.circularIndex(value: Int): T = this[circularIndexMapping(value)]

/**
 * Will extract a sub-list from position [from] to [to] from the original [List] considering it circular.
 * By default will include elements in position [from] and [to], [includedFrom] and [includedTo] can be used to
 * include/exclude those positions in any combination.
 *
 * Throws [IllegalStateException] if [List] is empty.
 */
fun <T> List<T>.circularSubList(from: Int, to: Int, includedTo: Boolean = true, includedFrom: Boolean = true): List<T> {
    val start = circularIndexMapping(from)
    val end = circularIndexMapping(to)
    val result = when {
        start == end -> listOf(start)
        start < end -> (start .. end)
        else -> (start until size) + (0 .. end)
    }
        .map { this[it] }
        .toMutableList()
    if (result.size == 1) {
        return if (includedFrom || includedTo) result else emptyList()
    }
    if (!includedTo) result.removeAt(result.size-1)
    if (!includedFrom) result.removeAt(0)
    return result.toList()
}

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
    if (this.isEmpty()) throw IllegalStateException("Trying to map a circular index on an empty List")
    return  ((value % this.size) + this.size) % this.size
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
fun <T> List<T>.circularIndex(value: Int): T = this[this.circularIndexMapping(value)]

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
    if (this.isEmpty()) throw IllegalStateException("Trying to map a circular index on an empty Array")
    return ((value % this.size) + this.size) % this.size
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
fun <T> Array<T>.circularIndex(value: Int): T = this[this.circularIndexMapping(value)]

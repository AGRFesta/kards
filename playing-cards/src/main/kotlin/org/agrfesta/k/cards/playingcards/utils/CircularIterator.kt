package org.agrfesta.k.cards.playingcards.utils

/**
 * Represents a never ending iterator, each invocation of [next] returns the next element in a circular way.
 */
interface CircularIterator<T> {
    fun next(): T
}

/**
 * A simple implementation of [CircularIterator] created from a [list] and an optional starting position
 * [actualPosition].
 */
class CircularIteratorImpl<T>(private val list: List<T>, initialPosition: UInt = 0u): CircularIterator<T> {
    private var actualPosition: UInt

    init {
        require(list.isNotEmpty()) { "Can't create a circular iterator from an empty list" }
        actualPosition = list.circularIndexMapping(initialPosition)
    }

    override fun next(): T {
        val element = list[actualPosition.toInt()]
        actualPosition = list.circularIndexMapping(actualPosition + 1u)
        return element
    }

}

/**
 * Utility method to generate a [CircularIterator] from a [List], will start from the position 0.
 */
fun <T> List<T>.circularIterator(): CircularIterator<T> = CircularIteratorImpl(this)

/**
 * Utility method to generate a [CircularIterator] from a [List], will start from the specified position [initialPos].
 */
fun <T> List<T>.circularIteratorFrom(initialPos: UInt): CircularIterator<T> = CircularIteratorImpl(this, initialPos)

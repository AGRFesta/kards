package org.agrfesta.k.cards.playingcards.utils

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CircularIteratorTest {

    @Test
    @DisplayName("circularIterator(): empty list -> throws an exception")
    fun circularIterator_emptyList_throwsAnException() {
        val failure = assertThat {
            emptyList<Int>().circularIterator()
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Can't create a circular iterator from an empty list")
    }

    @Test
    @DisplayName("next(): single element iterator -> returns always the same single element")
    fun next_singleElementIterator_returnsAlwaysTheSameSingleElement() {
        val iterator = listOf(5).circularIterator()

        assertThat(iterator.next()).isEqualTo(5)
        assertThat(iterator.next()).isEqualTo(5)
        assertThat(iterator.next()).isEqualTo(5)
    }

    @Test
    @DisplayName("next(): three elements iterator -> returns always the same sequence")
    fun next_threeElementsIterator_returnsAlwaysTheSameSequence() {
        val iterator = listOf(5, 234, 34).circularIterator()

        assertThat(iterator.next()).isEqualTo(5)
        assertThat(iterator.next()).isEqualTo(234)
        assertThat(iterator.next()).isEqualTo(34)
        assertThat(iterator.next()).isEqualTo(5)
        assertThat(iterator.next()).isEqualTo(234)
        assertThat(iterator.next()).isEqualTo(34)
    }

    @Test
    @DisplayName("circularIteratorFrom(): empty list -> throws an exception")
    fun circularIteratorFrom_emptyList_throwsAnException() {
        val failure = assertThat {
            emptyList<Int>().circularIteratorFrom(0u)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Can't create a circular iterator from an empty list")
    }

    @Test
    @DisplayName("next(): single element iterator starting out of bounds -> returns the single element")
    fun next_singleElementIteratorStartingOutOfBounds_returnsTheSingleElement() {
        val iterator = listOf(5).circularIteratorFrom(3u)

        assertThat(iterator.next()).isEqualTo(5)
    }

    @Test
    @DisplayName("next(): three elements iterator starting from 4 -> returns element in position 1")
    fun next_threeElementsIteratorStartingFrom4_returnsElementInPosition1() {
        val iterator = listOf(5, 346, 45).circularIteratorFrom(4u)

        assertThat(iterator.next()).isEqualTo(346)
    }

    @Test
    @DisplayName("next(): three elements iterator starting from 2 -> returns element in position 2")
    fun next_threeElementsIteratorStartingFrom2_returnsElementInPosition2() {
        val iterator = listOf(5, 346, 45).circularIteratorFrom(2u)

        assertThat(iterator.next()).isEqualTo(45)
    }

}

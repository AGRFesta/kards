package org.agrfesta.k.cards.playingcards.utils

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.agrfesta.k.cards.playingcards.deck.rankOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class CircularUIndexTest {
    private val list = listOf('A', 'B', 'C')
    private val rankX = rankOf('X')
    private val rankY = rankOf('Y')
    private val array = arrayOf(rankX, rankY)

    @Test
    @DisplayName("Get circular index mapping of an empty List -> throws IllegalStateException")
    fun gettingCircularIndexMappingOfAnEmptyList() {
        val failure = assertThat {
            emptyList<String>().circularIndexMapping(0u)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to map a circular index on an empty List")
    }

    @TestFactory
    @DisplayName("Circular index mapping on List tests")
    fun circularIndexMappingOnListTests() = listOf(
        0u to 0u,
        1u to 1u,
        2u to 2u,

        3u to 0u,
        4u to 1u,
        7u to 1u
    ).map {
        dynamicTest("List size ${list.size}: ${it.first} -> ${it.second}")
            { assertThat(list.circularIndexMapping(it.first)).isEqualTo(it.second) }
    }

    @Test
    @DisplayName("Get circular index from an empty List -> throws IllegalStateException")
    fun getCircularIndexFromAnEmptyList() {
        val failure = assertThat {
            emptyList<String>().circularIndex(0U)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to map a circular index on an empty List")
    }

    @TestFactory
    @DisplayName("Circular index on List tests")
    fun circularIndexOnListTests() = listOf(
        0u to 'A',
        1u to 'B',
        2u to 'C',

        3u to 'A',
        4u to 'B',
        7u to 'B'
    ).map {
        dynamicTest("List $list: ${it.first} -> ${it.second}")
            { assertThat(list.circularIndex(it.first)).isEqualTo(it.second) }
    }

    @Test
    @DisplayName("Get circular index mapping of an empty Array -> throws IllegalStateException")
    fun gettingCircularIndexMappingOfAnEmptyArray() {
        val failure = assertThat {
            emptyArray<String>().circularIndexMapping(0u)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to map a circular index on an empty Array")
    }

    @TestFactory
    @DisplayName("Circular index mapping on Array tests")
    fun circularIndexMappingOnArrayTests() = listOf(
        0u to 0u,
        1u to 1u,

        2u to 0u,
        3u to 1u,
        7u to 1u
    ).map {
        dynamicTest("Array size ${array.size}: ${it.first} -> ${it.second}")
        { assertThat(array.circularIndexMapping(it.first)).isEqualTo(it.second) }
    }

    @Test
    @DisplayName("Get circular index from an empty Array -> throws IllegalStateException")
    fun gettingCircularIndexFromAnEmptyArray() {
        val failure = assertThat {
            emptyArray<String>().circularIndex(0u)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to map a circular index on an empty Array")
    }

    @TestFactory
    @DisplayName("Circular index on Array tests")
    fun circularIndexOnArrayTests() = listOf(
        0u to rankX,
        1u to rankY,

        2u to rankX,
        3u to rankY,
        7u to rankY
    ).map {
        dynamicTest("Array ${array.toList()}: ${it.first} -> ${it.second}")
        { assertThat(array.circularIndex(it.first)).isEqualTo(it.second) }
    }

    private infix fun List<String>.subListingFrom(pair: Pair<UInt,UInt>) = Pair(this, pair)
    private infix fun Pair<List<String>, Pair<UInt,UInt>>.includedIsExactly(expected: List<String>) =
        dynamicTest("$first extracting circular sub-list from ${second.first} to" +
                " ${second.second} included -> $expected") {
            val result = first.circularSubList(second.first, second.second)
            assertThat(result).containsExactly(*expected.toTypedArray())
        }
    @TestFactory
    @DisplayName("List.circularSubList() [a, b]")
    fun list_circularSubList_includedIndex_tests() = listOf(
        listOf("A", "B", "C") subListingFrom (0u to 1u) includedIsExactly listOf("A", "B"),
        listOf("A", "B", "C") subListingFrom (0u to 0u) includedIsExactly listOf("A"),
        listOf("A", "B", "C") subListingFrom (1u to 0u) includedIsExactly listOf("B", "C", "A"),
        listOf("A", "B", "C") subListingFrom (2u to 0u) includedIsExactly listOf("C", "A")
    )

    private infix fun Pair<List<String>, Pair<UInt,UInt>>.excludedToIsExactly(expected: List<String>) =
        dynamicTest("$first extracting circular sub-list from ${second.first} to" +
                " ${second.second} [a, b) -> $expected") {
            val result = first.circularSubList(second.first, second.second, includedTo = false)
            assertThat(result).containsExactly(*expected.toTypedArray())
        }
    @TestFactory
    @DisplayName("List.circularSubList() [a, b)")
    fun list_circularSubList_excludedToIndex_tests() = listOf(
        listOf("A", "B", "C") subListingFrom (0u to 1u) excludedToIsExactly listOf("A"),
        listOf("A", "B", "C") subListingFrom (0u to 0u) excludedToIsExactly listOf("A"),
        listOf("A", "B", "C") subListingFrom (1u to 0u) excludedToIsExactly listOf("B", "C"),
        listOf("A", "B", "C") subListingFrom (2u to 0u) excludedToIsExactly listOf("C")
    )

    private infix fun Pair<List<String>, Pair<UInt,UInt>>.excludedFromIsExactly(expected: List<String>) =
        dynamicTest("$first extracting circular sub-list from ${second.first} to" +
                " ${second.second} (a, b] -> $expected") {
            val result = first.circularSubList(second.first, second.second, includedFrom = false)
            assertThat(result).containsExactly(*expected.toTypedArray())
        }
    @TestFactory
    @DisplayName("List.circularSubList() (a, b]")
    fun list_circularSubList_excludedFromIndex_tests() = listOf(
        listOf("A", "B", "C") subListingFrom (0u to 1u) excludedFromIsExactly listOf("B"),
        listOf("A", "B", "C") subListingFrom (0u to 0u) excludedFromIsExactly listOf("A"),
        listOf("A", "B", "C") subListingFrom (1u to 0u) excludedFromIsExactly listOf("C", "A"),
        listOf("A", "B", "C") subListingFrom (2u to 0u) excludedFromIsExactly listOf("A")
    )

    private infix fun Pair<List<String>, Pair<UInt,UInt>>.excludedFromToIsExactly(expected: List<String>) =
        dynamicTest("$first extracting circular sub-list from ${second.first} to" +
                " ${second.second} (a, b) -> $expected") {
            val result = first.circularSubList(second.first, second.second,
                includedFrom = false, includedTo = false)
            assertThat(result).containsExactly(*expected.toTypedArray())
        }
    @TestFactory
    @DisplayName("List.circularSubList() (a, b)")
    fun list_circularSubList_excludedFromToIndex_tests() = listOf(
        listOf("A", "B", "C") subListingFrom (0u to 1u) excludedFromToIsExactly listOf(),
        listOf("A", "B", "C") subListingFrom (0u to 0u) excludedFromToIsExactly listOf(),
        listOf("A", "B", "C") subListingFrom (1u to 0u) excludedFromToIsExactly listOf("C"),
        listOf("A", "B", "C") subListingFrom (2u to 0u) excludedFromToIsExactly listOf()
    )

}

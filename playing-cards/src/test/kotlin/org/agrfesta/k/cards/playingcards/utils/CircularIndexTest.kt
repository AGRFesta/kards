package org.agrfesta.k.cards.playingcards.utils

import org.agrfesta.k.cards.playingcards.deck.rankOf
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("Circular Index Tests")
class CircularIndexTest {
    private val list = listOf('A', 'B', 'C')
    private val rankX = rankOf('X')
    private val rankY = rankOf('Y')
    private val array = arrayOf(rankX, rankY)

    @Test
    @DisplayName("Get circular index mapping of an empty List -> throws IllegalStateException")
    fun gettingCircularIndexMappingOfAnEmptyList() {
        val failure = assertThat {
            emptyList<String>().circularIndexMapping(0)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to map a circular index on an empty List")
    }

    @TestFactory
    @DisplayName("Circular index mapping on List tests")
    fun circularIndexMappingOnListTests() = listOf(
            -6 to 0,
            -2 to 1,
            -1 to 2,

            0 to 0,
            1 to 1,
            2 to 2,

            3 to 0,
            4 to 1,
            7 to 1
    ).map {
        dynamicTest("List size ${list.size}: ${it.first} -> ${it.second}")
            { assertThat(list.circularIndexMapping(it.first)).isEqualTo(it.second) }
    }

    @Test
    @DisplayName("Get circular index from an empty List -> throws IllegalStateException")
    fun getCircularIndexFromAnEmptyList() {
        val failure = assertThat {
            emptyList<String>().circularIndex(0)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to map a circular index on an empty List")
    }

    @TestFactory
    @DisplayName("Circular index on List tests")
    fun circularIndexOnListTests() = listOf(
            -6 to 'A',
            -2 to 'B',
            -1 to 'C',

            0 to 'A',
            1 to 'B',
            2 to 'C',

            3 to 'A',
            4 to 'B',
            7 to 'B'
    ).map {
        dynamicTest("List $list: ${it.first} -> ${it.second}")
            { assertThat(list.circularIndex(it.first)).isEqualTo(it.second) }
    }

    @Test
    @DisplayName("Get circular index mapping of an empty Array -> throws IllegalStateException")
    fun gettingCircularIndexMappingOfAnEmptyArray() {
        val failure = assertThat {
            emptyArray<String>().circularIndexMapping(0)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to map a circular index on an empty Array")
    }

    @TestFactory
    @DisplayName("Circular index mapping on Array tests")
    fun circularIndexMappingOnArrayTests() = listOf(
            -6 to 0,
            -1 to 1,

            0 to 0,
            1 to 1,

            2 to 0,
            3 to 1,
            7 to 1
    ).map {
        dynamicTest("Array size ${array.size}: ${it.first} -> ${it.second}")
        { assertThat(array.circularIndexMapping(it.first)).isEqualTo(it.second) }
    }

    @Test
    @DisplayName("Get circular index from an empty Array -> throws IllegalStateException")
    fun gettingCircularIndexFromAnEmptyArray() {
        val failure = assertThat {
            emptyArray<String>().circularIndex(0)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to map a circular index on an empty Array")
    }

    @TestFactory
    @DisplayName("Circular index on Array tests")
    fun circularIndexOnArrayTests() = listOf(
            -6 to rankX,
            -1 to rankY,

            0 to rankX,
            1 to rankY,

            2 to rankX,
            3 to rankY,
            7 to rankY
    ).map {
        dynamicTest("Array ${array.toList()}: ${it.first} -> ${it.second}")
        { assertThat(array.circularIndex(it.first)).isEqualTo(it.second) }
    }

    private infix fun List<String>.subListingFrom(pair: Pair<Int,Int>) = Pair(this, pair)
    private infix fun Pair<List<String>, Pair<Int,Int>>.includedIsExactly(expected: List<String>) =
            dynamicTest("$first extracting circular sub-list from ${second.first} to" +
                    " ${second.second} included -> $expected") {
                val result = first.circularSubList(second.first, second.second)
                assertThat(result).containsExactly(*expected.toTypedArray())
            }
    @TestFactory
    @DisplayName("List.circularSubList() [a, b]")
    fun list_circularSubList_includedIndex_tests() = listOf(
            listOf("A", "B", "C") subListingFrom (0 to 1) includedIsExactly listOf("A", "B"),
            listOf("A", "B", "C") subListingFrom (0 to 0) includedIsExactly listOf("A"),
            listOf("A", "B", "C") subListingFrom (1 to 0) includedIsExactly listOf("B", "C", "A"),
            listOf("A", "B", "C") subListingFrom (2 to 0) includedIsExactly listOf("C", "A")
    )

    private infix fun Pair<List<String>, Pair<Int,Int>>.excludedToIsExactly(expected: List<String>) =
            dynamicTest("$first extracting circular sub-list from ${second.first} to" +
                    " ${second.second} [a, b) -> $expected") {
                val result = first.circularSubList(second.first, second.second, includedTo = false)
                assertThat(result).containsExactly(*expected.toTypedArray())
            }
    @TestFactory
    @DisplayName("List.circularSubList() [a, b)")
    fun list_circularSubList_excludedToIndex_tests() = listOf(
            listOf("A", "B", "C") subListingFrom (0 to 1) excludedToIsExactly listOf("A"),
            listOf("A", "B", "C") subListingFrom (0 to 0) excludedToIsExactly listOf("A"),
            listOf("A", "B", "C") subListingFrom (1 to 0) excludedToIsExactly listOf("B", "C"),
            listOf("A", "B", "C") subListingFrom (2 to 0) excludedToIsExactly listOf("C")
    )

    private infix fun Pair<List<String>, Pair<Int,Int>>.excludedFromIsExactly(expected: List<String>) =
            dynamicTest("$first extracting circular sub-list from ${second.first} to" +
                    " ${second.second} (a, b] -> $expected") {
                val result = first.circularSubList(second.first, second.second, includedFrom = false)
                assertThat(result).containsExactly(*expected.toTypedArray())
            }
    @TestFactory
    @DisplayName("List.circularSubList() (a, b]")
    fun list_circularSubList_excludedFromIndex_tests() = listOf(
            listOf("A", "B", "C") subListingFrom (0 to 1) excludedFromIsExactly listOf("B"),
            listOf("A", "B", "C") subListingFrom (0 to 0) excludedFromIsExactly listOf("A"),
            listOf("A", "B", "C") subListingFrom (1 to 0) excludedFromIsExactly listOf("C", "A"),
            listOf("A", "B", "C") subListingFrom (2 to 0) excludedFromIsExactly listOf("A")
    )

    private infix fun Pair<List<String>, Pair<Int,Int>>.excludedFromToIsExactly(expected: List<String>) =
            dynamicTest("$first extracting circular sub-list from ${second.first} to" +
                    " ${second.second} (a, b) -> $expected") {
                val result = first.circularSubList(second.first, second.second,
                        includedFrom = false, includedTo = false)
                assertThat(result).containsExactly(*expected.toTypedArray())
            }
    @TestFactory
    @DisplayName("List.circularSubList() (a, b)")
    fun list_circularSubList_excludedFromToIndex_tests() = listOf(
            listOf("A", "B", "C") subListingFrom (0 to 1) excludedFromToIsExactly listOf(),
            listOf("A", "B", "C") subListingFrom (0 to 0) excludedFromToIsExactly listOf(),
            listOf("A", "B", "C") subListingFrom (1 to 0) excludedFromToIsExactly listOf("C"),
            listOf("A", "B", "C") subListingFrom (2 to 0) excludedFromToIsExactly listOf()
    )

}

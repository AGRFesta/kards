package agrfesta.k.cards.playingcards.utils

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("Circular Index Tests")
class CircularIndexTest {
    private val list = listOf('A','B','C')
    private val array = arrayOf('X','Y')

    @Test
    @DisplayName("Get circular index mapping of an empty List -> throws IllegalStateException")
    fun gettingCircularIndexMappingOfAnEmptyList() {
        val emptyList = listOf<String>()
        val failure = assertThat {
            emptyList.circularIndexMapping(0)
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
    ).map { pair ->
        DynamicTest.dynamicTest(
                "List size ${list.size}: ${pair.first} -> ${pair.second}")
        { assertThat(list.circularIndexMapping(pair.first)).isEqualTo(pair.second) }
    }

    @Test
    @DisplayName("Get circular index from an empty List -> throws IllegalStateException")
    fun getCircularIndexFromAnEmptyList() {
        val emptyList = listOf<String>()
        val failure = assertThat {
            emptyList.circularIndex(0)
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
    ).map { pair ->
        DynamicTest.dynamicTest(
                "List $list: ${pair.first} -> ${pair.second}")
        { assertThat(list.circularIndex(pair.first)).isEqualTo(pair.second) }
    }

    @Test
    @DisplayName("Get circular index mapping of an empty Array -> throws IllegalStateException")
    fun gettingCircularIndexMappingOfAnEmptyArray() {
        val emptyArray = arrayOf<String>()
        val failure = assertThat {
            emptyArray.circularIndexMapping(0)
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
    ).map { pair ->
        DynamicTest.dynamicTest(
                "Array size ${array.size}: ${pair.first} -> ${pair.second}")
        { assertThat(array.circularIndexMapping(pair.first)).isEqualTo(pair.second) }
    }

    @Test
    @DisplayName("Get circular index from an empty Array -> throws IllegalStateException")
    fun gettingCircularIndexFromAnEmptyArray() {
        val emptyArray = arrayOf<String>()
        val failure = assertThat {
            emptyArray.circularIndex(0)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to map a circular index on an empty Array")
    }

    @TestFactory
    @DisplayName("Circular index on Array tests")
    fun circularIndexOnArrayTests() = listOf(
            -6 to 'X',
            -1 to 'Y',

            0 to 'X',
            1 to 'Y',

            2 to 'X',
            3 to 'Y',
            7 to 'Y'
    ).map { pair ->
        DynamicTest.dynamicTest(
                "Array ${array.toList()}: ${pair.first} -> ${pair.second}")
        { assertThat(array.circularIndex(pair.first)).isEqualTo(pair.second) }
    }

}

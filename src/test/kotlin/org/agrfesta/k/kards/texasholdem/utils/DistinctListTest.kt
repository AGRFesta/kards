package org.agrfesta.k.kards.texasholdem.utils

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import org.agrfesta.k.kards.texasholdem.utils.DistinctList.Companion.distinctListOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DistinctListTest {

    @Test
    @DisplayName("distinctListOf(): no vararg elements -> returns empty distinct list")
    fun distinctListOf_noVarargElements_returnsEmptyDistinctList() {
        val distinctList = distinctListOf<Int>()

        assertThat(distinctList).isEmpty()
    }

    @Test
    @DisplayName("distinctListOf(): distinct vararg elements -> returns distinct list with same elements")
    fun distinctListOf_distinctVarargElements_returnsDistinctListWithSameElements() {
        val distinctList = distinctListOf(1, 2, 3)

        assertThat(distinctList).containsExactly(1, 2, 3)
    }

    @Test
    @DisplayName("distinctListOf(): vararg elements with duplicates -> returns distinct list without duplicates")
    fun distinctListOf_varargElementsWithDuplicates_returnsDistinctListWithoutDuplicates() {
        val distinctList = distinctListOf(3, 2, 3)

        assertThat(distinctList).containsExactly(3, 2)
    }

    @Test
    @DisplayName("distinctListOf(): empty list -> returns empty distinct list")
    fun distinctListOf_emptyList_returnsEmptyDistinctList() {
        val distinctList = distinctListOf<Int>(emptyList())

        assertThat(distinctList).isEmpty()
    }

    @Test
    @DisplayName("distinctListOf(): distinct list elements -> returns distinct list with same elements")
    fun distinctListOf_distinctListElements_returnsDistinctListWithSameElements() {
        val distinctList = distinctListOf(listOf(1, 2, 3))

        assertThat(distinctList).containsExactly(1, 2, 3)
    }

    @Test
    @DisplayName("distinctListOf(): list with duplicates -> returns distinct list without duplicates")
    fun distinctListOf_listWithDuplicates_returnsDistinctListWithoutDuplicates() {
        val distinctList = distinctListOf(3, 2, 3)

        assertThat(distinctList).containsExactly(3, 2)
    }

    @Test
    @DisplayName("distinctListOf(): empty set -> returns empty distinct list")
    fun distinctListOf_emptySet_returnsEmptyDistinctList() {
        val distinctList = distinctListOf<Int>(emptySet())

        assertThat(distinctList).isEmpty()
    }

    @Test
    @DisplayName("distinctListOf(): non empty set -> returns distinct list with same elements")
    fun distinctListOf_nonEmptySet_returnsDistinctListWithSameElements() {
        val distinctList = distinctListOf(setOf(1, 2, 3))

        assertThat(distinctList).containsExactly(1, 2, 3)
    }

    @Test
    @DisplayName("Set.toDistinctList(): empty set -> returns empty distinct list")
    fun setToDistinctList_emptySet_returnsEmptyDistinctList() {
        val set = emptySet<Int>()

        val result = set.toDistinctList()

        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("Set.toDistinctList(): non empty set -> returns distinct list with same elements")
    fun setToDistinctList_nonEmptySet_returnsDistinctListWithSameElements() {
        val set = setOf(1, 2, 3)

        val result = set.toDistinctList()

        assertThat(result).containsExactly(1, 2, 3)
    }

    @Test
    @DisplayName("map(): empty list -> returns empty distinct list")
    fun map_emptyList_returnsEmptyDistinctList() {
        val distinctList = distinctListOf<Int>()

        val result: DistinctList<UInt> = distinctList.map { it.toUInt() }

        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("map(): distinct element mapped to distinct element -> returns distinct list with same elements")
    fun map_distinctElementMappedToDistinctElement_returnsDistinctListWithSameElements() {
        val distinctList = distinctListOf(1, 2, 3)

        val result: DistinctList<UInt> = distinctList.map { it.toUInt() }

        assertThat(result).containsExactly(1u, 2u, 3u)
    }

    @Test
    @DisplayName("map(): distinct element mapped to same element -> returns distinct list with a single mapped element")
    fun map_distinctElementMappedToSameElement_returnsDistinctListWithASingleMappedElement() {
        val distinctList = distinctListOf(1, 2, 3)

        val result: DistinctList<UInt> = distinctList.map { 5u }

        assertThat(result).containsExactly(5u)
    }

    @Test
    @DisplayName("filter(): empty list -> returns empty distinct list")
    fun filter_emptyList_returnsEmptyDistinctList() {
        val distinctList = distinctListOf<Int>()

        val result: DistinctList<Int> = distinctList.filter { true }

        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("filter(): non empty distinct list -> returns distinct list with filtered elements")
    fun filter_nonEmptyDistinctList_returnsDistinctListWithFilteredElements() {
        val distinctList = distinctListOf(1, 2, 3)

        val result: DistinctList<Int> = distinctList.filter { it % 2 != 0 }

        assertThat(result).containsExactly(1, 3)
    }

}

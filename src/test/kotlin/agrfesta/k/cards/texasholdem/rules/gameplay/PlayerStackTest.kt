package agrfesta.k.cards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("PlayerStack tests")
class PlayerStackTest {

    @Test
    @DisplayName("Collection<PlayerStack>.toRanking(): empty collection -> returns an empty list")
    fun emptyCollectionReturnsEmptyList() {
        val list = emptyList<PlayerStack>()
        assertThat(list.toRanking()).isEmpty()
    }

    @Test
    @DisplayName("Collection<PlayerStack>.toRanking(): single element list -> returns a single element list")
    fun singleElementListReturnsASingleElementList() {
        val list = listOf( alex owns 2000)
        assertThat(list.toRanking()).containsExactly( alex owns 2000 )
    }

    @Test
    @DisplayName("Collection<PlayerStack>.toRanking(): a list not sorted by stack -> returns a list sorted by stack")
    fun aListNotSortedByStackReturnsAListSortedByStack() {
        val list = listOf( alex owns 100, poly owns 500, jane owns 200)
        assertThat(list.toRanking()).containsExactly( poly owns 500, jane owns 200, alex owns 100 )
    }

}

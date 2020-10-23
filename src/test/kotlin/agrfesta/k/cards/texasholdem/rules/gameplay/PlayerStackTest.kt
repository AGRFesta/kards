package agrfesta.k.cards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
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
        val alex = aPlayer()
        val list = listOf(PlayerStack(alex, 2000))
        assertThat(list.toRanking()).extracting { it.player to it.stack }
                .containsExactly( alex to 2000 )
    }

    @Test
    @DisplayName("Collection<PlayerStack>.toRanking(): a list not sorted by stack -> returns a list sorted by stack")
    fun aListNotSortedByStackReturnsAListSortedByStack() {
        val poly = aPlayer()
        val alex = aPlayer()
        val jane = aPlayer()
        val list = listOf(PlayerStack(alex, 100), PlayerStack(poly, 500), PlayerStack(jane, 200))
        assertThat(list.toRanking()).extracting { it.player to it.stack }
                .containsExactly( poly to 500, jane to 200, alex to 100 )
    }

}

package org.agrfesta.k.cards.playingcards.cards

import org.agrfesta.k.cards.playingcards.deck.aRank
import org.agrfesta.k.cards.playingcards.deck.aSeed
import org.agrfesta.k.cards.playingcards.deck.rankOf
import org.agrfesta.k.cards.playingcards.deck.seedOf
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Card Tests")
class CardTest {

    @Test
    @DisplayName("Comparing two cards with same rank and same seed -> equal")
    fun comparingCardsWithSameRankAndSameSeedResultEqual() {
        val seed = aSeed()
        val rank = aRank()
        val a = cardOf(rank, seed)
        val b = cardOf(rank, seed)
        assertThat(a).isEqualTo(b)
        assertThat(a.hashCode()).isEqualTo(b.hashCode())
    }
    @Test
    @DisplayName("Comparing two cards with different seed -> not equal")
    fun comparingCardsWithDifferentSeedResultNotEqual() {
        val rank = aRank()
        val a = cardOf(rank, aSeed())
        val b = cardOf(rank, aSeed())
        assertThat(a).isNotEqualTo(b)
    }
    @Test
    @DisplayName("Comparing two cards with different rank -> not equal")
    fun comparingCardsWithDifferentRankResultNotEqual() {
        val seed = aSeed()
        val a = cardOf(aRank(), seed)
        val b = cardOf(aRank(), seed)
        assertThat(a).isNotEqualTo(b)
    }
    @Test
    @DisplayName("Comparing two cards with different rank and different seed -> not equal")
    fun comparingCardsWithDifferentRankAndDifferentSeedResultNotEqual() {
        val a = cardOf(aRank(), aSeed())
        val b = cardOf(aRank(), aSeed())
        assertThat(a).isNotEqualTo(b)
    }
    @Test
    @DisplayName("Comparing a card to a String -> not equal")
    fun comparingACardToAStringResultNotEqual() {
        val a = cardOf(aRank(), aSeed())
        assertThat(a).isNotEqualTo("string")
    }
    @Test
    @DisplayName("Comparing a card to null -> not equal")
    fun comparingACardToNullResultNotEqual() {
        val a = cardOf(aRank(), aSeed())
        assertThat(a).isNotEqualTo(null)
    }

    @Test
    @DisplayName("A Card with rank 'r' and seed 'S' -> to string is 'rS'")
    fun toStringTest() {
        val card = cardOf(rankOf('r'), seedOf('S',0))
        assertThat(card.toString()).isEqualTo("rS")
    }

}

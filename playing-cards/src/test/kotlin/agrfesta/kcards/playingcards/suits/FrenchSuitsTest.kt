package agrfesta.kcards.playingcards.suits

import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.deck.Deck
import agrfesta.kcards.playingcards.deck.RandomDrawDeck
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("French Suits Tests")
class FrenchSuitsTest {

    @Test
    @DisplayName("Build hand from an empty string card -> throws IllegalArgumentException")
    fun buildHandFromEmptyStringCardThrowsException() {
        val failure = assertThat {
            createFrenchHand("")
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to create French Card, received empty String")
    }
    @Test
    @DisplayName("Build hand from a single char string card -> throws IllegalArgumentException")
    fun buildHandFromASingleCharStringCardThrowsException() {
        val failure = assertThat {
            createFrenchHand("A")
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to create French Card, expected two char String, received: A")
    }
    @Test
    @DisplayName("Build hand from a three char string card -> throws IllegalArgumentException")
    fun buildHandFromAThreeCharStringCardThrowsException() {
        val failure = assertThat {
            createFrenchHand("A2v")
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to create French Card, expected two char String, received: A2v")
    }
    @Test
    @DisplayName("Build hand from an empty array -> an empty hand")
    fun buildHandFromAnEmptyArrayReturnsAnEmptyHand() {
        val a = Array(0) {""}
        val hand = createFrenchHand(*a)
        assertThat(hand).isEmpty()
    }
    @Test
    @DisplayName("Build hand from a single valid string -> a one card hand")
    fun buildHandFromASingleValidStringReturnsAnOneCardHand() {
        val hand = createFrenchHand("Ah")
        assertThat(hand).hasSize(1)
        val card = hand[0]
        assertThat(card.rank()).isEqualTo(FrenchRank.ACE)
        assertThat(card.seed()).isEqualTo(FrenchSeed.HEARTS)
    }
    @Test
    @DisplayName("Build hand from two valid string -> a two card hand")
    fun buildHandFromTwoValidStringReturnsATwoCardHand() {
        val hand = createFrenchHand("Ah","5s")
        assertThat(hand).extracting(Card::rank, Card::seed)
                .containsOnly(
                        Pair(FrenchRank.FIVE,FrenchSeed.SPADES),
                        Pair(FrenchRank.ACE,FrenchSeed.HEARTS)
                )
    }

    private fun assertIsFullDeck(deck: Deck) {
        assertThat(deck.size()).isEqualTo(52)
        val cards = deck.draw(52)
        assertThat(cards).extracting(Card::rank, Card::seed)
                .containsOnly(
                        Pair(FrenchRank.ACE, FrenchSeed.HEARTS),
                        Pair(FrenchRank.KING, FrenchSeed.HEARTS),
                        Pair(FrenchRank.QUEEN, FrenchSeed.HEARTS),
                        Pair(FrenchRank.JACK, FrenchSeed.HEARTS),
                        Pair(FrenchRank.TEN, FrenchSeed.HEARTS),
                        Pair(FrenchRank.NINE, FrenchSeed.HEARTS),
                        Pair(FrenchRank.EIGHT, FrenchSeed.HEARTS),
                        Pair(FrenchRank.SEVEN, FrenchSeed.HEARTS),
                        Pair(FrenchRank.SIX, FrenchSeed.HEARTS),
                        Pair(FrenchRank.FIVE, FrenchSeed.HEARTS),
                        Pair(FrenchRank.FOUR, FrenchSeed.HEARTS),
                        Pair(FrenchRank.THREE, FrenchSeed.HEARTS),
                        Pair(FrenchRank.TWO, FrenchSeed.HEARTS),

                        Pair(FrenchRank.ACE, FrenchSeed.SPADES),
                        Pair(FrenchRank.KING, FrenchSeed.SPADES),
                        Pair(FrenchRank.QUEEN, FrenchSeed.SPADES),
                        Pair(FrenchRank.JACK, FrenchSeed.SPADES),
                        Pair(FrenchRank.TEN, FrenchSeed.SPADES),
                        Pair(FrenchRank.NINE, FrenchSeed.SPADES),
                        Pair(FrenchRank.EIGHT, FrenchSeed.SPADES),
                        Pair(FrenchRank.SEVEN, FrenchSeed.SPADES),
                        Pair(FrenchRank.SIX, FrenchSeed.SPADES),
                        Pair(FrenchRank.FIVE, FrenchSeed.SPADES),
                        Pair(FrenchRank.FOUR, FrenchSeed.SPADES),
                        Pair(FrenchRank.THREE, FrenchSeed.SPADES),
                        Pair(FrenchRank.TWO, FrenchSeed.SPADES),

                        Pair(FrenchRank.ACE, FrenchSeed.CLUBS),
                        Pair(FrenchRank.KING, FrenchSeed.CLUBS),
                        Pair(FrenchRank.QUEEN, FrenchSeed.CLUBS),
                        Pair(FrenchRank.JACK, FrenchSeed.CLUBS),
                        Pair(FrenchRank.TEN, FrenchSeed.CLUBS),
                        Pair(FrenchRank.NINE, FrenchSeed.CLUBS),
                        Pair(FrenchRank.EIGHT, FrenchSeed.CLUBS),
                        Pair(FrenchRank.SEVEN, FrenchSeed.CLUBS),
                        Pair(FrenchRank.SIX, FrenchSeed.CLUBS),
                        Pair(FrenchRank.FIVE, FrenchSeed.CLUBS),
                        Pair(FrenchRank.FOUR, FrenchSeed.CLUBS),
                        Pair(FrenchRank.THREE, FrenchSeed.CLUBS),
                        Pair(FrenchRank.TWO, FrenchSeed.CLUBS),

                        Pair(FrenchRank.ACE, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.KING, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.QUEEN, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.JACK, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.TEN, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.NINE, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.EIGHT, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.SEVEN, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.SIX, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.FIVE, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.FOUR, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.THREE, FrenchSeed.DIAMONDS),
                        Pair(FrenchRank.TWO, FrenchSeed.DIAMONDS)
                )
    }
    @Test
    @DisplayName("Create deck with default settings -> a full deck")
    fun createDeckWithDefaultSettings() {
        val deck = createFrenchDeck()
        assertIsFullDeck(deck)
    }
    @Test
    @DisplayName("Create deck with specific settings -> a full deck")
    fun createDeckWithSpecificSettings() {
        val deck = createFrenchDeck() { RandomDrawDeck() }
        assertIsFullDeck(deck)
    }
}
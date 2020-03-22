package agrfesta.kcards.playingcards.suits

import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.deck.Deck
import agrfesta.kcards.playingcards.deck.RandomDrawDeck
import agrfesta.kcards.playingcards.deck.rankOf
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import agrfesta.kcards.playingcards.suits.FrenchSeed.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

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
        assertThat(card.rank()).isEqualTo(ACE)
        assertThat(card.seed()).isEqualTo(HEARTS)
    }
    @Test
    @DisplayName("Build hand from two valid string -> a two card hand")
    fun buildHandFromTwoValidStringReturnsATwoCardHand() {
        val hand = createFrenchHand("Ah","5s")
        assertThat(hand).extracting(Card::rank, Card::seed)
                .containsOnly(
                        Pair(FIVE,SPADES),
                        Pair(ACE,HEARTS)
                )
    }

    private fun assertIsFullDeck(deck: Deck) {
        assertThat(deck.size()).isEqualTo(52)
        val cards = deck.draw(52)
        assertThat(cards).extracting(Card::rank, Card::seed)
                .containsOnly(
                        Pair(ACE, HEARTS),
                        Pair(KING, HEARTS),
                        Pair(QUEEN, HEARTS),
                        Pair(JACK, HEARTS),
                        Pair(TEN, HEARTS),
                        Pair(NINE, HEARTS),
                        Pair(EIGHT, HEARTS),
                        Pair(SEVEN, HEARTS),
                        Pair(SIX, HEARTS),
                        Pair(FIVE, HEARTS),
                        Pair(FOUR, HEARTS),
                        Pair(THREE, HEARTS),
                        Pair(TWO, HEARTS),

                        Pair(ACE, SPADES),
                        Pair(KING, SPADES),
                        Pair(QUEEN, SPADES),
                        Pair(JACK, SPADES),
                        Pair(TEN, SPADES),
                        Pair(NINE, SPADES),
                        Pair(EIGHT, SPADES),
                        Pair(SEVEN, SPADES),
                        Pair(SIX, SPADES),
                        Pair(FIVE, SPADES),
                        Pair(FOUR, SPADES),
                        Pair(THREE, SPADES),
                        Pair(TWO, SPADES),

                        Pair(ACE, CLUBS),
                        Pair(KING, CLUBS),
                        Pair(QUEEN, CLUBS),
                        Pair(JACK, CLUBS),
                        Pair(TEN, CLUBS),
                        Pair(NINE, CLUBS),
                        Pair(EIGHT, CLUBS),
                        Pair(SEVEN, CLUBS),
                        Pair(SIX, CLUBS),
                        Pair(FIVE, CLUBS),
                        Pair(FOUR, CLUBS),
                        Pair(THREE, CLUBS),
                        Pair(TWO, CLUBS),

                        Pair(ACE, DIAMONDS),
                        Pair(KING, DIAMONDS),
                        Pair(QUEEN, DIAMONDS),
                        Pair(JACK, DIAMONDS),
                        Pair(TEN, DIAMONDS),
                        Pair(NINE, DIAMONDS),
                        Pair(EIGHT, DIAMONDS),
                        Pair(SEVEN, DIAMONDS),
                        Pair(SIX, DIAMONDS),
                        Pair(FIVE, DIAMONDS),
                        Pair(FOUR, DIAMONDS),
                        Pair(THREE, DIAMONDS),
                        Pair(TWO, DIAMONDS)
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

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            Pair(ACE, KING),
            Pair(KING, QUEEN),
            Pair(QUEEN, JACK),
            Pair(JACK, TEN),
            Pair(TEN, NINE),
            Pair(NINE, EIGHT),
            Pair(EIGHT, SEVEN),
            Pair(SEVEN, SIX),
            Pair(SIX, FIVE),
            Pair(FIVE, FOUR),
            Pair(FOUR, THREE),
            Pair(THREE, TWO)
    ).map { pair ->
        DynamicTest.dynamicTest(
                "${pair.first} is greater than ${pair.second}")
                { assertThat(pair.first).isGreaterThan(pair.second) }
    }

    @Test
    @DisplayName("Compare FrenchRankAdapter with a different Rank implementation -> throws IllegalArgumentException")
    fun compareWithADifferentRankImplThrowsException() {
        val difImplRank = rankOf('x')
        val failure = assertThat {
            ACE.compareTo(difImplRank)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of FrenchRankAdapter")
    }
}
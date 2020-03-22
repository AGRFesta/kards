package agrfesta.kcards.playingcards.suits

import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.deck.Deck
import agrfesta.kcards.playingcards.deck.RandomDrawDeck
import agrfesta.kcards.playingcards.deck.rankOf
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import agrfesta.kcards.playingcards.suits.ItalianSeed.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

@DisplayName("Italian Suits Tests")
class ItalianSuitsTest {
    @Test
    @DisplayName("Build hand from an empty string card -> throws IllegalArgumentException")
    fun buildHandFromEmptyStringCardThrowsException() {
        val failure = assertThat {
            createItalianHand("")
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to create Italian Card, received empty String")
    }
    @Test
    @DisplayName("Build hand from a single char string card -> throws IllegalArgumentException")
    fun buildHandFromASingleCharStringCardThrowsException() {
        val failure = assertThat {
            createItalianHand("A")
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to create Italian Card, expected two char String, received: A")
    }
    @Test
    @DisplayName("Build hand from a three char string card -> throws IllegalArgumentException")
    fun buildHandFromAThreeCharStringCardThrowsException() {
        val failure = assertThat {
            createItalianHand("A2v")
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to create Italian Card, expected two char String, received: A2v")
    }
    @Test
    @DisplayName("Build hand from an empty array -> an empty hand")
    fun buildHandFromAnEmptyArrayReturnsAnEmptyHand() {
        val a = Array(0) {""}
        val hand = createItalianHand(*a)
        assertThat(hand).isEmpty()
    }
    @Test
    @DisplayName("Build hand from a single valid string -> a one card hand")
    fun buildHandFromASingleValidStringReturnsAnOneCardHand() {
        val hand = createItalianHand("Ab")
        assertThat(hand).hasSize(1)
        val card = hand[0]
        assertThat(card.rank()).isEqualTo(ASSO)
        assertThat(card.seed()).isEqualTo(BASTONI)
    }
    @Test
    @DisplayName("Build hand from two valid string -> a two card hand")
    fun buildHandFromTwoValidStringReturnsATwoCardHand() {
        val hand = createItalianHand("Kc","3s")
        assertThat(hand).extracting(Card::rank, Card::seed)
                .containsOnly(
                        Pair(RE,COPPE),
                        Pair(TRE,SPADE)
                )
    }

    private fun assertIsFullDeck(deck: Deck) {
        assertThat(deck.size()).isEqualTo(40)
        val cards = deck.draw(40)
        assertThat(cards).extracting(Card::rank, Card::seed)
                .containsOnly(
                        Pair(ASSO, DENARI),
                        Pair(RE, DENARI),
                        Pair(CAVALLO, DENARI),
                        Pair(FANTE, DENARI),
                        Pair(SETTE, DENARI),
                        Pair(SEI, DENARI),
                        Pair(CINQUE, DENARI),
                        Pair(QUATTRO, DENARI),
                        Pair(TRE, DENARI),
                        Pair(DUE, DENARI),

                        Pair(ASSO, COPPE),
                        Pair(RE, COPPE),
                        Pair(CAVALLO, COPPE),
                        Pair(FANTE, COPPE),
                        Pair(SETTE, COPPE),
                        Pair(SEI, COPPE),
                        Pair(CINQUE, COPPE),
                        Pair(QUATTRO, COPPE),
                        Pair(TRE, COPPE),
                        Pair(DUE, COPPE),

                        Pair(ASSO, BASTONI),
                        Pair(RE, BASTONI),
                        Pair(CAVALLO, BASTONI),
                        Pair(FANTE, BASTONI),
                        Pair(SETTE, BASTONI),
                        Pair(SEI, BASTONI),
                        Pair(CINQUE, BASTONI),
                        Pair(QUATTRO, BASTONI),
                        Pair(TRE, BASTONI),
                        Pair(DUE, BASTONI),

                        Pair(ASSO, SPADE),
                        Pair(RE, SPADE),
                        Pair(CAVALLO, SPADE),
                        Pair(FANTE, SPADE),
                        Pair(SETTE, SPADE),
                        Pair(SEI, SPADE),
                        Pair(CINQUE, SPADE),
                        Pair(QUATTRO, SPADE),
                        Pair(TRE, SPADE),
                        Pair(DUE, SPADE)
                )
    }
    @Test
    @DisplayName("Create deck with default settings -> a full deck")
    fun createDeckWithDefaultSettings() {
        val deck = createItalianDeck()
        assertIsFullDeck(deck)
    }
    @Test
    @DisplayName("Create deck with specific settings -> a full deck")
    fun createDeckWithSpecificSettings() {
        val deck = createItalianDeck() { RandomDrawDeck() }
        assertIsFullDeck(deck)
    }

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            Pair(ASSO, RE),
            Pair(RE, CAVALLO),
            Pair(CAVALLO, FANTE),
            Pair(FANTE, SETTE),
            Pair(SETTE, SEI),
            Pair(SEI, CINQUE),
            Pair(CINQUE, QUATTRO),
            Pair(QUATTRO, TRE),
            Pair(TRE, DUE)
    ).map { pair ->
        DynamicTest.dynamicTest(
                "${pair.first} is greater than ${pair.second}")
        { assertThat(pair.first).isGreaterThan(pair.second) }
    }

    @Test
    @DisplayName("Compare ItalianRankAdapter with a different Rank implementation -> throws IllegalArgumentException")
    fun compareWithADifferentRankImplThrowsException() {
        val difImplRank = rankOf('x')
        val failure = assertThat {
            ASSO.compareTo(difImplRank)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of ItalianRankAdapter")
    }
}
package agrfesta.kcards.playingcards.suits

import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.deck.Deck
import agrfesta.kcards.playingcards.deck.RandomDrawDeck
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

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
        assertThat(card.rank()).isEqualTo(ItalianRank.ASSO)
        assertThat(card.seed()).isEqualTo(ItalianSeed.BASTONI)
    }
    @Test
    @DisplayName("Build hand from two valid string -> a two card hand")
    fun buildHandFromTwoValidStringReturnsATwoCardHand() {
        val hand = createItalianHand("Kc","3s")
        assertThat(hand).extracting(Card::rank, Card::seed)
                .containsOnly(
                        Pair(ItalianRank.RE,ItalianSeed.COPPE),
                        Pair(ItalianRank.TRE,ItalianSeed.SPADE)
                )
    }

    private fun assertIsFullDeck(deck: Deck) {
        assertThat(deck.size()).isEqualTo(40)
        val cards = deck.draw(40)
        assertThat(cards).extracting(Card::rank, Card::seed)
                .containsOnly(
                        Pair(ItalianRank.ASSO, ItalianSeed.DENARI),
                        Pair(ItalianRank.RE, ItalianSeed.DENARI),
                        Pair(ItalianRank.CAVALLO, ItalianSeed.DENARI),
                        Pair(ItalianRank.FANTE, ItalianSeed.DENARI),
                        Pair(ItalianRank.SETTE, ItalianSeed.DENARI),
                        Pair(ItalianRank.SEI, ItalianSeed.DENARI),
                        Pair(ItalianRank.CINQUE, ItalianSeed.DENARI),
                        Pair(ItalianRank.QUATTRO, ItalianSeed.DENARI),
                        Pair(ItalianRank.TRE, ItalianSeed.DENARI),
                        Pair(ItalianRank.DUE, ItalianSeed.DENARI),

                        Pair(ItalianRank.ASSO, ItalianSeed.COPPE),
                        Pair(ItalianRank.RE, ItalianSeed.COPPE),
                        Pair(ItalianRank.CAVALLO, ItalianSeed.COPPE),
                        Pair(ItalianRank.FANTE, ItalianSeed.COPPE),
                        Pair(ItalianRank.SETTE, ItalianSeed.COPPE),
                        Pair(ItalianRank.SEI, ItalianSeed.COPPE),
                        Pair(ItalianRank.CINQUE, ItalianSeed.COPPE),
                        Pair(ItalianRank.QUATTRO, ItalianSeed.COPPE),
                        Pair(ItalianRank.TRE, ItalianSeed.COPPE),
                        Pair(ItalianRank.DUE, ItalianSeed.COPPE),

                        Pair(ItalianRank.ASSO, ItalianSeed.BASTONI),
                        Pair(ItalianRank.RE, ItalianSeed.BASTONI),
                        Pair(ItalianRank.CAVALLO, ItalianSeed.BASTONI),
                        Pair(ItalianRank.FANTE, ItalianSeed.BASTONI),
                        Pair(ItalianRank.SETTE, ItalianSeed.BASTONI),
                        Pair(ItalianRank.SEI, ItalianSeed.BASTONI),
                        Pair(ItalianRank.CINQUE, ItalianSeed.BASTONI),
                        Pair(ItalianRank.QUATTRO, ItalianSeed.BASTONI),
                        Pair(ItalianRank.TRE, ItalianSeed.BASTONI),
                        Pair(ItalianRank.DUE, ItalianSeed.BASTONI),

                        Pair(ItalianRank.ASSO, ItalianSeed.SPADE),
                        Pair(ItalianRank.RE, ItalianSeed.SPADE),
                        Pair(ItalianRank.CAVALLO, ItalianSeed.SPADE),
                        Pair(ItalianRank.FANTE, ItalianSeed.SPADE),
                        Pair(ItalianRank.SETTE, ItalianSeed.SPADE),
                        Pair(ItalianRank.SEI, ItalianSeed.SPADE),
                        Pair(ItalianRank.CINQUE, ItalianSeed.SPADE),
                        Pair(ItalianRank.QUATTRO, ItalianSeed.SPADE),
                        Pair(ItalianRank.TRE, ItalianSeed.SPADE),
                        Pair(ItalianRank.DUE, ItalianSeed.SPADE)
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
}
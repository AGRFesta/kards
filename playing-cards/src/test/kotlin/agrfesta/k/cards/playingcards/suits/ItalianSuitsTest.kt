package agrfesta.k.cards.playingcards.suits

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.deck.buildDeck
import agrfesta.k.cards.playingcards.deck.rankOf
import agrfesta.k.cards.playingcards.suits.ItalianSeed.*
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("Italian Suits Tests")
class ItalianSuitsTest {

    @Test
    @DisplayName("Get rank from symbol 'x' -> throws IllegalArgumentException")
    fun gettingRankFromWrongSymbolThrowsException() {
        val failure = assertThat {
            getItalianRankFromSymbol('x')
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Symbol 'x' is not an Italian Rank")
    }
    @Test
    @DisplayName("Get seed from symbol 'x' -> throws IllegalArgumentException")
    fun gettingSeedFromWrongSymbolThrowsException() {
        val failure = assertThat {
            getItalianSeedFromSymbol('x')
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Symbol 'x' is not an Italian Seed")
    }

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
        val hand = createItalianHand("Kc", "3s")
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
        val deck = buildDeck(italianCards())
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

    @TestFactory
    @DisplayName("Plus operator overloading tests")
    fun plusOperatorOverloadingTest() = listOf(
            Triple(ASSO, 1, DUE),
            Triple(RE, 2, DUE),
            Triple(DUE, 1, TRE),
            Triple(ASSO, 10, ASSO),
            Triple(CAVALLO, 20, CAVALLO),
            Triple(FANTE, 11, CAVALLO),
            Triple(ASSO, -1, RE),
            Triple(SETTE, -10, SETTE),
            Triple(SEI, -20, SEI),
            Triple(QUATTRO, -21, TRE)
    ).map { t ->
        DynamicTest.dynamicTest(
                "${t.first} + ${t.second} = ${t.third}")
        { assertThat(t.first + t.second).isEqualTo(t.third) }
    }
    @TestFactory
    @DisplayName("Minus operator overloading tests")
    fun minusOperatorOverloadingTest() = listOf(
            Triple(RE, 1, CAVALLO),
            Triple(RE, 2, FANTE),
            Triple(DUE, 1, ASSO),
            Triple(ASSO, 10, ASSO),
            Triple(CAVALLO, 20, CAVALLO),
            Triple(FANTE, 11, SETTE),
            Triple(ASSO, -1, DUE),
            Triple(SETTE, -10, SETTE),
            Triple(SEI, -20, SEI),
            Triple(QUATTRO, -11, CINQUE)
    ).map { t ->
        DynamicTest.dynamicTest(
                "${t.first} - ${t.second} = ${t.third}")
        { assertThat(t.first - t.second).isEqualTo(t.third) }
    }
}
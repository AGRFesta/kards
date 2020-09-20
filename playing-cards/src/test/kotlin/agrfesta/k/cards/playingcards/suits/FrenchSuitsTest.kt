package agrfesta.k.cards.playingcards.suits

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.deck.rankOf
import agrfesta.k.cards.playingcards.suits.FrenchSeed.*
import agrfesta.k.cards.playingcards.suits.Suit.FRENCH
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("French Suits Tests")
class FrenchSuitsTest {

    @Test
    @DisplayName("Get rank from symbol 'x' -> throws IllegalArgumentException")
    fun gettingRankFromWrongSymbolThrowsException() {
        val failure = assertThat {
            getFrenchRankFromSymbol('x')
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Symbol 'x' is not a French Rank")
    }
    @TestFactory
    @DisplayName("Get french rank from symbol tests")
    fun getFrenchRankFromSymbolTests() = listOf(
            'A' to ACE,
            'K' to KING,
            'Q' to QUEEN,
            'J' to JACK,
            'T' to TEN,
            '9' to NINE,
            '8' to EIGHT,
            '7' to SEVEN,
            '6' to SIX,
            '5' to FIVE,
            '4' to FOUR,
            '3' to THREE,
            '2' to TWO
    ).map { pair ->
        DynamicTest.dynamicTest(
                "${pair.first} -> ${pair.second}")
        { assertThat(getFrenchRankFromSymbol(pair.first)).isEqualTo(pair.second) }
    }

    @Test
    @DisplayName("Get seed from symbol 'x' -> throws IllegalArgumentException")
    fun gettingSeedFromWrongSymbolThrowsException() {
        val failure = assertThat {
            getFrenchSeedFromSymbol('x')
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Symbol 'x' is not a French Seed")
    }
    @TestFactory
    @DisplayName("Get french seed from symbol tests")
    fun getFrenchSeedFromSymbolTests() = listOf(
            'h' to HEARTS,
            'c' to CLUBS,
            's' to SPADES,
            'd' to DIAMONDS
    ).map { pair ->
        DynamicTest.dynamicTest(
                "${pair.first} -> ${pair.second}")
        { assertThat(getFrenchSeedFromSymbol(pair.first)).isEqualTo(pair.second) }
    }

    @TestFactory
    @DisplayName("Get french rank from ordinal tests")
    fun getFrenchRankFromOrdinalTests() = listOf(
            -15 to THREE,
            -1 to TWO,

            0 to ACE,
            1 to KING,
            2 to QUEEN,
            3 to JACK,
            4 to TEN,
            5 to NINE,
            6 to EIGHT,
            7 to SEVEN,
            8 to SIX,
            9 to FIVE,
            10 to FOUR,
            11 to THREE,
            12 to TWO,

            13 to ACE,
            27 to KING
    ).map { pair ->
        DynamicTest.dynamicTest(
                "${pair.first} -> ${pair.second}")
        { assertThat(getFrenchRankFromOrdinal(pair.first)).isEqualTo(pair.second) }
    }

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
        val hand = createFrenchHand("Ah", "5s")
        assertThat(hand).extracting(Card::rank, Card::seed)
                .containsOnly(
                        Pair(FIVE,SPADES),
                        Pair(ACE,HEARTS)
                )
    }

    @Test
    @DisplayName("Create Set from two identical strings -> a one card Set")
    fun createSetFromTwoIdenticalStringReturnsAOneCardSet() {
        val set = frenchCardsSet("Ah", "Ah")
        assertThat(set).extracting(Card::rank, Card::seed)
                .containsOnly(
                        Pair(ACE,HEARTS)
                )
    }

    @Test
    @DisplayName("Create deck with default settings -> a full deck")
    fun createDeckWithDefaultSettings() {
        assertIsFullFrenchDeck(FRENCH.createDeck())
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

    @TestFactory
    @DisplayName("Plus operator overloading tests")
    fun plusOperatorOverloadingTest() = listOf(
            Triple(ACE, 1, TWO),
            Triple(KING, 2, TWO),
            Triple(TWO, 1, THREE),
            Triple(ACE, 13, ACE),
            Triple(QUEEN, 26, QUEEN),
            Triple(JACK, 14, QUEEN),
            Triple(ACE, -1, KING),
            Triple(SEVEN, -13, SEVEN),
            Triple(SIX, -26, SIX),
            Triple(FOUR, -27, THREE)
    ).map { t ->
        DynamicTest.dynamicTest(
                "${t.first} + ${t.second} = ${t.third}")
        { assertThat(t.first + t.second).isEqualTo(t.third) }
    }
    @TestFactory
    @DisplayName("Minus operator overloading tests")
    fun minusOperatorOverloadingTest() = listOf(
            Triple(ACE, 1, KING),
            Triple(KING, 2, JACK),
            Triple(TWO, 1, ACE),
            Triple(ACE, 13, ACE),
            Triple(QUEEN, 26, QUEEN),
            Triple(JACK, 14, TEN),
            Triple(ACE, -1, TWO),
            Triple(SEVEN, -13, SEVEN),
            Triple(SIX, -26, SIX),
            Triple(FOUR, -27, FIVE)
    ).map { t ->
        DynamicTest.dynamicTest(
                "${t.first} - ${t.second} = ${t.third}")
        { assertThat(t.first - t.second).isEqualTo(t.third) }
    }

    @Test
    @DisplayName("Test french seed values")
    fun assertFrenchSeedValues() {
        assertThat(listOf(
                HEARTS, DIAMONDS, CLUBS, SPADES
        )).extracting(FrenchSeed::ord,FrenchSeed::char,FrenchSeed::symbol)
                .containsExactly(
                        Triple(0, 'h', '♡'),
                        Triple(1, 'd', '♢'),
                        Triple(2, 'c', '♣'),
                        Triple(3, 's', '♠')
                )
    }
    @Test
    @DisplayName("Test french rank values")
    fun assertFrenchRankValues() {
        assertThat(listOf(
                FrenchRank.ACE,
                FrenchRank.KING,
                FrenchRank.QUEEN,
                FrenchRank.JACK,
                FrenchRank.TEN,
                FrenchRank.NINE,
                FrenchRank.EIGHT,
                FrenchRank.SEVEN,
                FrenchRank.SIX,
                FrenchRank.FIVE,
                FrenchRank.FOUR,
                FrenchRank.THREE,
                FrenchRank.TWO
        )).extracting(FrenchRank::ordinal,FrenchRank::adapter,FrenchRank::symbol)
                .containsExactly(
                        Triple(0, ACE, 'A'),
                        Triple(1, KING, 'K'),
                        Triple(2, QUEEN, 'Q'),
                        Triple(3, JACK, 'J'),
                        Triple(4, TEN, 'T'),
                        Triple(5, NINE, '9'),
                        Triple(6, EIGHT, '8'),
                        Triple(7, SEVEN, '7'),
                        Triple(8, SIX, '6'),
                        Triple(9, FIVE, '5'),
                        Triple(10, FOUR, '4'),
                        Triple(11, THREE, '3'),
                        Triple(12, TWO, '2')
                )
    }
}

fun assertIsFullFrenchCardList(cards: Collection<Card>) {
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
fun assertIsFullFrenchDeck(deck: Deck) {
    assertThat(deck.size()).isEqualTo(52)
    val cards = deck.draw(52)
    assertIsFullFrenchCardList(cards)
}

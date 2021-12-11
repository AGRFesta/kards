package org.agrfesta.k.kards.texasholdem.draws

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.suits.ACE
import agrfesta.k.cards.playingcards.suits.EIGHT
import agrfesta.k.cards.playingcards.suits.FIVE
import agrfesta.k.cards.playingcards.suits.FOUR
import agrfesta.k.cards.playingcards.suits.FrenchSeed.HEARTS
import agrfesta.k.cards.playingcards.suits.JACK
import agrfesta.k.cards.playingcards.suits.KING
import agrfesta.k.cards.playingcards.suits.NINE
import agrfesta.k.cards.playingcards.suits.QUEEN
import agrfesta.k.cards.playingcards.suits.SEVEN
import agrfesta.k.cards.playingcards.suits.SIX
import agrfesta.k.cards.playingcards.suits.TEN
import agrfesta.k.cards.playingcards.suits.THREE
import agrfesta.k.cards.playingcards.suits.TWO
import org.agrfesta.k.kards.texasholdem.LazyCollectionAssertion
import org.agrfesta.k.kards.texasholdem.createDynamicTest
import org.agrfesta.k.kards.texasholdem.resultContainsOnly
import org.agrfesta.k.kards.texasholdem.resultIsEmpty
import org.agrfesta.k.kards.texasholdem.willAssertThatCards
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory

interface DrawsEvaluatorTest {
    fun getDrawsEvaluator(): DrawsEvaluator

    @TestFactory
    @DisplayName("Evaluations of cards containing an Outside Straight Draw")
    fun outsideStraightDrawEvaluations() = listOf<LazyCollectionAssertion<Set<Card>, Draw>>(

            willAssertThatCards("Th", "4h", "Jh", "3d", "Kc", "Qc")
                    .resultContainsOnly(OutsideStraightDraw(KING)),
            willAssertThatCards("Th", "9h", "Jh", "9d", "9c", "Qc")
                    .resultContainsOnly(OutsideStraightDraw(QUEEN)),
            willAssertThatCards("Th", "9h", "Jh", "8d", "9c", "8c")
                    .resultContainsOnly(OutsideStraightDraw(JACK)),
            willAssertThatCards("Th", "9h", "7h", "8d", "9c", "5c")
                    .resultContainsOnly(OutsideStraightDraw(TEN)),
            willAssertThatCards("4h", "9h", "7h", "8d", "6c", "3c")
                    .resultContainsOnly(OutsideStraightDraw(NINE)),
            willAssertThatCards("5h", "Th", "7h", "8d", "6c", "Qc")
                    .resultContainsOnly(OutsideStraightDraw(EIGHT)),
            willAssertThatCards("5h", "Ah", "7h", "4d", "6c", "Ac")
                    .resultContainsOnly(OutsideStraightDraw(SEVEN)),
            willAssertThatCards("5h", "Ah", "3h", "4d", "6c", "Kc")
                    .resultContainsOnly(OutsideStraightDraw(SIX)),
            willAssertThatCards("5h", "2h", "3h", "4d", "8c", "9c")
                    .resultContainsOnly(OutsideStraightDraw(FIVE))

    ).map {
        createDynamicTest(it) { s -> getDrawsEvaluator().evaluate(s) }
    }

    @TestFactory
    @DisplayName("Evaluations of cards containing an high Inside Straight Draw")
    fun highInsideStraightDrawEvaluations() = listOf<LazyCollectionAssertion<Set<Card>, Draw>>(

            willAssertThatCards("Jh", "Th", "3h", "Qd", "2c", "Ac")
                    .resultContainsOnly(InsideStraightDraw(ACE, KING)),
            willAssertThatCards("Jh", "Th", "3h", "Kd", "2c", "Ac")
                    .resultContainsOnly(InsideStraightDraw(ACE, QUEEN)),
            willAssertThatCards("Kh", "Th", "3h", "Qd", "2c", "Ac")
                    .resultContainsOnly(InsideStraightDraw(ACE, JACK)),
            willAssertThatCards("Jh", "Kh", "3h", "Qd", "2c", "Ac")
                    .resultContainsOnly(InsideStraightDraw(ACE, TEN)),
            willAssertThatCards("Jh", "Kh", "3h", "Td", "2c", "9c")
                    .resultContainsOnly(InsideStraightDraw(KING, QUEEN)),
            willAssertThatCards("Qh", "Kh", "3h", "Td", "Qc", "9c")
                    .resultContainsOnly(InsideStraightDraw(KING, JACK)),
            willAssertThatCards("Qh", "Kh", "3h", "Jd", "Qc", "9c")
                    .resultContainsOnly(InsideStraightDraw(KING, TEN)),
            willAssertThatCards("8h", "Qh", "3h", "Td", "2c", "9c")
                    .resultContainsOnly(InsideStraightDraw(QUEEN, JACK)),
            willAssertThatCards("8h", "Qh", "3h", "Jd", "8c", "9c")
                    .resultContainsOnly(InsideStraightDraw(QUEEN, TEN)),
            willAssertThatCards("8h", "Qh", "3h", "Jd", "Tc", "Qc")
                    .resultContainsOnly(InsideStraightDraw(QUEEN, NINE)),
            willAssertThatCards("8h", "Jh", "3h", "7d", "Ac", "9c")
                    .resultContainsOnly(InsideStraightDraw(JACK, TEN)),
            willAssertThatCards("8h", "Jh", "Ah", "7d", "Ac", "Tc")
                    .resultContainsOnly(InsideStraightDraw(JACK, NINE)),
            willAssertThatCards("Th", "Jh", "2h", "7d", "Ac", "9c")
                    .resultContainsOnly(InsideStraightDraw(JACK, EIGHT))

    ).map {
        createDynamicTest(it) { s -> getDrawsEvaluator().evaluate(s) }
    }

    @TestFactory
    @DisplayName("Evaluations of cards containing a low Inside Straight Draw")
    fun lowInsideStraightDrawEvaluations() = listOf<LazyCollectionAssertion<Set<Card>, Draw>>(

            willAssertThatCards("8h", "Th", "Kh", "7d", "Ac", "6c")
                    .resultContainsOnly(InsideStraightDraw(TEN, NINE)),
            willAssertThatCards("Th", "6h", "3h", "7d", "Ac", "9c")
                    .resultContainsOnly(InsideStraightDraw(TEN, EIGHT)),
            willAssertThatCards("Th", "6h", "3h", "4d", "8c", "9c")
                    .resultContainsOnly(InsideStraightDraw(TEN, SEVEN)),
            willAssertThatCards("5h", "9h", "Ah", "7d", "Ac", "6c")
                    .resultContainsOnly(InsideStraightDraw(NINE, EIGHT)),
            willAssertThatCards("5h", "9h", "Ah", "8d", "8c", "6c")
                    .resultContainsOnly(InsideStraightDraw(NINE, SEVEN)),
            willAssertThatCards("5h", "9h", "Ah", "8d", "5c", "7c")
                    .resultContainsOnly(InsideStraightDraw(NINE, SIX)),
            willAssertThatCards("5h", "8h", "Qh", "4d", "Ac", "6c")
                    .resultContainsOnly(InsideStraightDraw(EIGHT, SEVEN)),
            willAssertThatCards("5h", "8h", "Qh", "4d", "Qc", "7c")
                    .resultContainsOnly(InsideStraightDraw(EIGHT, SIX)),
            willAssertThatCards("7h", "8h", "Qh", "4d", "8c", "6c")
                    .resultContainsOnly(InsideStraightDraw(EIGHT, FIVE)),
            willAssertThatCards("5h", "7h", "Qh", "4d", "4c", "3c")
                    .resultContainsOnly(InsideStraightDraw(SEVEN, SIX)),
            willAssertThatCards("6h", "7h", "Qh", "7d", "4c", "3c")
                    .resultContainsOnly(InsideStraightDraw(SEVEN, FIVE)),
            willAssertThatCards("6h", "7h", "Qh", "6d", "5c", "3c")
                    .resultContainsOnly(InsideStraightDraw(SEVEN, FOUR)),
            willAssertThatCards("2h", "6h", "Qh", "4d", "4c", "3c")
                    .resultContainsOnly(InsideStraightDraw(SIX, FIVE)),
            willAssertThatCards("2h", "6h", "Qh", "5d", "Jc", "3c")
                    .resultContainsOnly(InsideStraightDraw(SIX, FOUR)),
            willAssertThatCards("2h", "6h", "4h", "5d", "4c", "4c")
                    .resultContainsOnly(InsideStraightDraw(SIX, THREE)),
            willAssertThatCards("2h", "3h", "Ah", "4d", "4c", "3c")
                    .resultContainsOnly(InsideStraightDraw(FIVE, FIVE)),
            willAssertThatCards("2h", "3h", "Ah", "Ad", "Kc", "5c")
                    .resultContainsOnly(InsideStraightDraw(FIVE, FOUR)),
            willAssertThatCards("2h", "4h", "Ah", "Kd", "Kc", "5c")
                    .resultContainsOnly(InsideStraightDraw(FIVE, THREE)),
            willAssertThatCards("5h", "4h", "Ah", "3d", "Kc", "Qc")
                    .resultContainsOnly(InsideStraightDraw(FIVE, TWO))

    ).map {
        createDynamicTest(it) { s -> getDrawsEvaluator().evaluate(s) }
    }

    @TestFactory
    @DisplayName("Evaluations of cards containing a Double Inside Straight Draw")
    fun doubleInsideStraightDrawEvaluations() = listOf<LazyCollectionAssertion<Set<Card>, Draw>>(

            willAssertThatCards("Jh", "7h", "Th", "Kd", "Ac", "9c")
                    .resultContainsOnly(DoubleInsideStraightDraw(JACK, ACE)),
            willAssertThatCards("6h", "8h", "Th", "Kd", "Qc", "9c")
                    .resultContainsOnly(DoubleInsideStraightDraw(TEN, KING)),
            willAssertThatCards("7h", "8h", "Jh", "5d", "Qc", "9c")
                    .resultContainsOnly(DoubleInsideStraightDraw(NINE, QUEEN)),
            willAssertThatCards("7h", "8h", "Jh", "Td", "4c", "6c")
                    .resultContainsOnly(DoubleInsideStraightDraw(EIGHT, JACK)),
            willAssertThatCards("7h", "5h", "9h", "Td", "3c", "6c")
                    .resultContainsOnly(DoubleInsideStraightDraw(SEVEN, TEN)),
            willAssertThatCards("4h", "5h", "9h", "8d", "2c", "6c")
                    .resultContainsOnly(DoubleInsideStraightDraw(SIX, NINE)),
            willAssertThatCards("4h", "5h", "Ah", "8d", "3c", "7c")
                    .resultContainsOnly(DoubleInsideStraightDraw(FIVE, EIGHT)),
            willAssertThatCards("Qh", "Ah", "Th", "Ad", "8c", "Jc")
                    .resultContainsOnly(DoubleInsideStraightDraw(QUEEN, ACE)),
            willAssertThatCards("Jh", "3h", "Th", "Kd", "9c", "7c")
                    .resultContainsOnly(DoubleInsideStraightDraw(JACK, KING)),
            willAssertThatCards("5h", "6h", "Th", "8d", "9c", "Qc")
                    .resultContainsOnly(DoubleInsideStraightDraw(TEN, QUEEN)),
            willAssertThatCards("Jh", "5h", "8h", "5d", "9c", "7c")
                    .resultContainsOnly(DoubleInsideStraightDraw(NINE, JACK)),
            willAssertThatCards("Th", "7h", "8h", "4d", "6c", "7c")
                    .resultContainsOnly(DoubleInsideStraightDraw(EIGHT, TEN)),
            willAssertThatCards("6h", "7h", "5h", "3d", "6c", "9c")
                    .resultContainsOnly(DoubleInsideStraightDraw(SEVEN, NINE)),
            willAssertThatCards("6h", "5h", "8h", "2d", "6c", "4c")
                    .resultContainsOnly(DoubleInsideStraightDraw(SIX, EIGHT)),
            willAssertThatCards("4h", "5h", "7h", "3d", "Kc", "Ac")
                    .resultContainsOnly(DoubleInsideStraightDraw(FIVE, SEVEN))

    ).map {
        createDynamicTest(it) { s -> getDrawsEvaluator().evaluate(s) }
    }

    @TestFactory
    @DisplayName("evaluations")
    fun comparisons() = listOf<LazyCollectionAssertion<Set<Card>, Draw>>(
            /// Six Cards TESTS
            willAssertThatCards("Ah", "6h", "Th", "Ad", "Ac", "5h")
                    .resultContainsOnly(FlushDraw(ACE, TEN, SIX, FIVE, HEARTS)),
            willAssertThatCards("Ah", "Kh", "Th", "2h", "7h", "5h").resultIsEmpty(),
            willAssertThatCards("Ah", "Kh", "Th", "Jh", "Qh", "5d").resultIsEmpty(),
            willAssertThatCards("Ah", "Kh", "Th", "Ks", "Ad", "5h")
                    .resultContainsOnly(FlushDraw(ACE, KING, TEN, FIVE, HEARTS)),
            willAssertThatCards("Ah", "Kh", "Tc", "2c", "7s", "5s").resultIsEmpty(),
            willAssertThatCards("Jh", "3h", "Th", "Kd", "9c", "7h")
                    .resultContainsOnly(
                            DoubleInsideStraightDraw(JACK, KING),
                            FlushDraw(JACK, TEN, SEVEN, THREE, HEARTS)
                    ),
            willAssertThatCards("Jh", "Qh", "3h", "Kd", "Ac", "7h")
                    .resultContainsOnly(
                            InsideStraightDraw(ACE, TEN),
                            FlushDraw(QUEEN, JACK, SEVEN, THREE, HEARTS)
                    ),
            willAssertThatCards("Th", "Qh", "3h", "8d", "9c", "7h")
                    .resultContainsOnly(
                            OutsideStraightDraw(TEN),
                            FlushDraw(QUEEN, TEN, SEVEN, THREE, HEARTS)
                    ),

            willAssertThatCards("Jh", "3h", "Th", "Kh", "9h", "7c").resultIsEmpty(),
            willAssertThatCards("Jh", "Th", "3h", "Qh", "2h", "Ac").resultIsEmpty()

            //TODO tests with five cards
            //TODO test for flush straight draws

    ).map {
        createDynamicTest(it) { s -> getDrawsEvaluator().evaluate(s) }
    }

    //TODO test for minimum and maximum number of cards
}

package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.suits.ACE
import agrfesta.k.cards.playingcards.suits.EIGHT
import agrfesta.k.cards.playingcards.suits.FIVE
import agrfesta.k.cards.playingcards.suits.FOUR
import agrfesta.k.cards.playingcards.suits.FrenchSeed.CLUBS
import agrfesta.k.cards.playingcards.suits.FrenchSeed.HEARTS
import agrfesta.k.cards.playingcards.suits.JACK
import agrfesta.k.cards.playingcards.suits.KING
import agrfesta.k.cards.playingcards.suits.QUEEN
import agrfesta.k.cards.playingcards.suits.SEVEN
import agrfesta.k.cards.playingcards.suits.SIX
import agrfesta.k.cards.playingcards.suits.TEN
import agrfesta.k.cards.playingcards.suits.THREE
import agrfesta.k.cards.playingcards.suits.TWO
import agrfesta.k.cards.playingcards.suits.frenchCardsSet
import agrfesta.k.cards.texasholdem.LazyFunctionAssertion
import agrfesta.k.cards.texasholdem.createDynamicTest
import agrfesta.k.cards.texasholdem.result
import agrfesta.k.cards.texasholdem.rules.hands.FlushHand
import agrfesta.k.cards.texasholdem.rules.hands.FourOfAKindHand
import agrfesta.k.cards.texasholdem.rules.hands.FullHouseHand
import agrfesta.k.cards.texasholdem.rules.hands.HighCardHand
import agrfesta.k.cards.texasholdem.rules.hands.PairHand
import agrfesta.k.cards.texasholdem.rules.hands.StraightFlushHand
import agrfesta.k.cards.texasholdem.rules.hands.StraightHand
import agrfesta.k.cards.texasholdem.rules.hands.ThreeOfAKindHand
import agrfesta.k.cards.texasholdem.rules.hands.TwoPairHand
import agrfesta.k.cards.texasholdem.willAssertThatCards
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

interface CardsEvaluatorTest {
    fun getCardsEvaluator(): CardsEvaluator

    @TestFactory
    @DisplayName("Seven cards evaluations")
    fun sevenCardsEvaluations() = listOf<LazyFunctionAssertion<Set<Card>,CardsEvaluation>>(

            willAssertThatCards("Ah","Jh","Th","Ad","Ac","Kh","Qh")
                    .result(StraightFlushHand(ACE, HEARTS)),
            willAssertThatCards("Ah","5h","Th","3h","Ac","4h","2h")
                    .result(StraightFlushHand(FIVE, HEARTS)),

            willAssertThatCards("Jh","Jc","3s","Jd","3c","3h","Js")
                    .result(FourOfAKindHand(JACK, THREE)),
            willAssertThatCards("Ah","Jc","3s","3d","3c","3h","Js")
                    .result(FourOfAKindHand(THREE, ACE)),

            willAssertThatCards("3d","3c","3h","Ah","Ac","As","Js")
                    .result(FullHouseHand(ACE, THREE)),
            willAssertThatCards("Ah","Ac","As","3d","3c","Kh","Ks")
                    .result(FullHouseHand(ACE, KING)),
            willAssertThatCards("Ah","Qs","Ac","As","Jd","Jc","Kh")
                    .result(FullHouseHand(ACE, JACK)),

            willAssertThatCards("Ah","7h","Kh","3d","3c","3h","5h")
                    .result(FlushHand(ACE,KING,SEVEN,FIVE,THREE, HEARTS)),
            willAssertThatCards("Ah","7h","Kh","3d","5c","3h","5h")
                    .result(FlushHand(ACE,KING,SEVEN,FIVE,THREE, HEARTS)),
            willAssertThatCards("6c","Ac","Kc","Qc","7c","5c","Tc")
                    .result(FlushHand(ACE,KING,QUEEN,TEN,SEVEN, CLUBS)),

            willAssertThatCards("7s","7h","7d","6d","4c","3h","5h")
                    .result(StraightHand(SEVEN)),
            willAssertThatCards("As","Jh","2d","Jd","4c","3h","5h")
                    .result(StraightHand(FIVE)),
            willAssertThatCards("7s","4h","7d","6d","4c","3h","5h")
                    .result(StraightHand(SEVEN)),
            willAssertThatCards("As","2h","Qd","Jd","Kc","3h","Th")
                    .result(StraightHand(ACE)),

            willAssertThatCards("7s","7h","7d","6d","Ac","3h","5h")
                    .result(ThreeOfAKindHand(SEVEN,  ACE,SIX)),

            willAssertThatCards("7s","Ah","7d","6d","Ac","3h","5h")
                    .result(TwoPairHand(ACE,SEVEN, SIX)),
            willAssertThatCards("7s","Kh","7d","3d","Kc","3h","5h")
                    .result(TwoPairHand(KING,SEVEN, FIVE)),

            willAssertThatCards("7s","Kh","7d","6d","Ac","3h","5h")
                    .result(PairHand(SEVEN, ACE,KING,SIX)),

            willAssertThatCards("Ah","2d","3c","4h","Ks","8c","7s")
                    .result(HighCardHand(ACE,KING,EIGHT,SEVEN,FOUR)),
            willAssertThatCards("Ah","Jc","Ts","2d","3c","8c","7s")
                    .result(HighCardHand(ACE,JACK,TEN,EIGHT,SEVEN))

    ).map {
        createDynamicTest(it){ s -> getCardsEvaluator().evaluate(s) }
    }

    @TestFactory
    @DisplayName("Five cards evaluations")
    fun fiveCardsEvaluations() = listOf<LazyFunctionAssertion<Set<Card>,CardsEvaluation>>(

            willAssertThatCards("Ah","Jh","Th","Kh","Qh")
                    .result(StraightFlushHand(ACE, HEARTS)),
            willAssertThatCards("Ah","5h","3h","4h","2h")
                    .result(StraightFlushHand(FIVE, HEARTS)),

            willAssertThatCards("Ah","3s","3d","3c","3h")
                    .result(FourOfAKindHand(THREE, ACE)),
            willAssertThatCards("Jh","3s","Jd","Jc","Js")
                    .result(FourOfAKindHand(JACK, THREE)),

            willAssertThatCards("3d","3h","Ah","Ac","As")
                    .result(FullHouseHand(ACE, THREE)),
            willAssertThatCards("Ah","Ac","Jd","Jc","Jh")
                    .result(FullHouseHand(JACK, ACE)),

            willAssertThatCards("Ah","3h","Kh","2h","4h")
                    .result(FlushHand(ACE,KING,FOUR,THREE,TWO, HEARTS)),

            willAssertThatCards("7h","6d","4c","3h","5h")
                    .result(StraightHand(SEVEN)),
            willAssertThatCards("As","2d","4c","3h","5h")
                    .result(StraightHand(FIVE)),
            willAssertThatCards("As","Qd","Jd","Kc","Th")
                    .result(StraightHand(ACE)),

            willAssertThatCards("7s","7h","7d","6d","Ac")
                    .result(ThreeOfAKindHand(SEVEN, ACE,SIX)),

            willAssertThatCards("7s","Ah","7d","6d","Ac")
                    .result(TwoPairHand(ACE,SEVEN, SIX)),

            willAssertThatCards("7s","Kh","7d","6d","Ac")
                    .result(PairHand(SEVEN, ACE,KING,SIX)),

            willAssertThatCards("Ah","3c","Ks","8c","7s")
                    .result(HighCardHand(ACE,KING,EIGHT,SEVEN,THREE))

    ).map {
        createDynamicTest(it){ s -> getCardsEvaluator().evaluate(s) }
    }

    @TestFactory
    @DisplayName("Six cards evaluations")
    fun sixCardsEvaluations() = listOf<LazyFunctionAssertion<Set<Card>,CardsEvaluation>>(

            willAssertThatCards("Ah","Jh","Th","Ad","Kh","Qh")
                    .result(StraightFlushHand(ACE, HEARTS)),
            willAssertThatCards("Ah","5h","Th","3h","4h","2h")
                    .result(StraightFlushHand(FIVE, HEARTS)),

            willAssertThatCards("Ah","3s","3d","3c","3h","Js")
                    .result(FourOfAKindHand(THREE, ACE)),
            willAssertThatCards("Jh","3s","Jd","Jc","3h","Js")
                    .result(FourOfAKindHand(JACK, THREE)),

            willAssertThatCards("3d","3c","3h","Ah","Ac","As")
                    .result(FullHouseHand(ACE, THREE)),
            willAssertThatCards("Ah","Ac","As","3c","Kh","Ks")
                    .result(FullHouseHand(ACE, KING)),
            willAssertThatCards("Ah","Ac","As","Jd","Jc","Kh")
                    .result(FullHouseHand(ACE, JACK)),

            willAssertThatCards("Ah","7h","Kh","3c","3h","5h")
                    .result(FlushHand(ACE,KING,SEVEN,FIVE,THREE, HEARTS)),

            willAssertThatCards("7s","7h","6d","4c","3h","5h")
                    .result(StraightHand(SEVEN)),
            willAssertThatCards("As","Jh","2d","4c","3h","5h")
                    .result(StraightHand(FIVE)),
            willAssertThatCards("As","2h","Qd","Jd","Kc","Th")
                    .result(StraightHand(ACE)),

            willAssertThatCards("7s","7h","7d","6d","Ac","5h")
                    .result(ThreeOfAKindHand(SEVEN, ACE,SIX)),

            willAssertThatCards("7s","Ah","7d","6d","Ac","5h")
                    .result(TwoPairHand(ACE,SEVEN, SIX)),
            willAssertThatCards("7s","Kh","7d","3d","Kc","3h")
                    .result(TwoPairHand(KING,SEVEN, THREE)),

            willAssertThatCards("7s","Kh","7d","6d","Ac","5h")
                    .result(PairHand(SEVEN, ACE,KING,SIX)),

            willAssertThatCards("Ah","2d","3c","Ks","8c","7s")
                    .result(HighCardHand(ACE,KING,EIGHT,SEVEN,THREE)),
            willAssertThatCards("Ah","Jc","Ts","2d","8c","7s")
                    .result(HighCardHand(ACE,JACK,TEN,EIGHT,SEVEN))

    ).map {
        createDynamicTest(it){ s -> getCardsEvaluator().evaluate(s) }
    }

    @Test
    @DisplayName("Evaluate a set of eight cards -> raises an Exception")
    fun evaluateASetOfEightCardsRaisesAnException() {
        val failure = assertThat {
            getCardsEvaluator().evaluate(frenchCardsSet("Ah","Ad","As","3d","3c","3h","Js","Tc"))
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to evaluate a set of more than seven cards: 8")
    }
    @Test
    @DisplayName("Evaluate a set of four cards -> raises an Exception")
    fun evaluateASetOfFourCardsRaisesAnException() {
        val failure = assertThat {
            getCardsEvaluator().evaluate(frenchCardsSet("Ah","Ad","As","3d"))
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to evaluate a set of less than five cards: 4")
    }
    @Test
    @DisplayName("Evaluate an empty set of cards -> raises an Exception")
    fun evaluateAnEmptySetOfCardsRaisesAnException() {
        val failure = assertThat {
            getCardsEvaluator().evaluate(frenchCardsSet())
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to evaluate a set of less than five cards: 0")
    }

}

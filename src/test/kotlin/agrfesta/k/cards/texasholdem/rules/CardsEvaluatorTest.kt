package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.texasholdem.LazyFunctionAssertion
import agrfesta.k.cards.texasholdem.createDynamicTest
import agrfesta.k.cards.texasholdem.result
import agrfesta.k.cards.texasholdem.rules.hands.*
import agrfesta.k.cards.texasholdem.willAssertThat
import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.suits.*
import agrfesta.kcards.playingcards.suits.FrenchSeed.*
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
    @DisplayName("evaluations")
    fun comparisons() = listOf<LazyFunctionAssertion<Set<Card>,CardsEvaluation>>(
            /// Seven Cards TESTS
            willAssertThat(frenchCardsSet("Ah","Jh","Th","Ad","Ac","Kh","Qh"))
                    .result(StraightFlushHand(ACE, HEARTS)),
            willAssertThat(frenchCardsSet("Ah","5h","Th","3h","Ac","4h","2h"))
                    .result(StraightFlushHand(FIVE, HEARTS)),

            willAssertThat(frenchCardsSet("Jh","Jc","3s","Jd","3c","3h","Js"))
                    .result(FourOfAKindHand(JACK, THREE)),
            willAssertThat(frenchCardsSet("Ah","Jc","3s","3d","3c","3h","Js"))
                    .result(FourOfAKindHand(THREE, ACE)),

            willAssertThat(frenchCardsSet("3d","3c","3h","Ah","Ac","As","Js"))
                    .result(FullHouseHand(ACE, THREE)),
            willAssertThat(frenchCardsSet("Ah","Ac","As","3d","3c","Kh","Ks"))
                    .result(FullHouseHand(ACE, KING)),
            willAssertThat(frenchCardsSet("Ah","Qs","Ac","As","Jd","Jc","Kh"))
                    .result(FullHouseHand(ACE, JACK)),

            willAssertThat(frenchCardsSet("Ah","7h","Kh","3d","3c","3h","5h"))
                    .result(FlushHand(ACE,KING,SEVEN,FIVE,THREE, HEARTS)),
            willAssertThat(frenchCardsSet("Ah","7h","Kh","3d","5c","3h","5h"))
                    .result(FlushHand(ACE,KING,SEVEN,FIVE,THREE, HEARTS)),

            willAssertThat(frenchCardsSet("7s","7h","7d","6d","4c","3h","5h"))
                    .result(StraightHand(SEVEN)),
            willAssertThat(frenchCardsSet("As","Jh","2d","Jd","4c","3h","5h"))
                    .result(StraightHand(FIVE)),
            willAssertThat(frenchCardsSet("7s","4h","7d","6d","4c","3h","5h"))
                    .result(StraightHand(SEVEN)),
            willAssertThat(frenchCardsSet("As","2h","Qd","Jd","Kc","3h","Th"))
                    .result(StraightHand(ACE)),

            willAssertThat(frenchCardsSet("7s","7h","7d","6d","Ac","3h","5h"))
                    .result(ThreeOfAKindHand(SEVEN,  ACE,SIX)),

            willAssertThat(frenchCardsSet("7s","Ah","7d","6d","Ac","3h","5h"))
                    .result(TwoPairHand(ACE,SEVEN, SIX)),
            willAssertThat(frenchCardsSet("7s","Kh","7d","3d","Kc","3h","5h"))
                    .result(TwoPairHand(KING,SEVEN, FIVE)),

            willAssertThat(frenchCardsSet("7s","Kh","7d","6d","Ac","3h","5h"))
                    .result(PairHand(SEVEN, ACE,KING,SIX)),

            willAssertThat(frenchCardsSet("Ah","2d","3c","4h","Ks","8c","7s"))
                    .result(HighCardHand(ACE,KING,EIGHT,SEVEN,FOUR)),
            willAssertThat(frenchCardsSet("Ah","Jc","Ts","2d","3c","8c","7s"))
                    .result(HighCardHand(ACE,JACK,TEN,EIGHT,SEVEN)),
            //////////////////////////////////////

            /// Five Cards TESTS
            willAssertThat(frenchCardsSet("Ah","Jh","Th","Kh","Qh"))
                    .result(StraightFlushHand(ACE, HEARTS)),
            willAssertThat(frenchCardsSet("Ah","5h","3h","4h","2h"))
                    .result(StraightFlushHand(FIVE, HEARTS)),

            willAssertThat(frenchCardsSet("Ah","3s","3d","3c","3h"))
                    .result(FourOfAKindHand(THREE, ACE)),
            willAssertThat(frenchCardsSet("Jh","3s","Jd","Jc","Js"))
                    .result(FourOfAKindHand(JACK, THREE)),

            willAssertThat(frenchCardsSet("3d","3h","Ah","Ac","As"))
                    .result(FullHouseHand(ACE, THREE)),
            willAssertThat(frenchCardsSet("Ah","Ac","Jd","Jc","Jh"))
                    .result(FullHouseHand(JACK, ACE)),

            willAssertThat(frenchCardsSet("Ah","3h","Kh","2h","4h"))
                    .result(FlushHand(ACE,KING,FOUR,THREE,TWO, HEARTS)),

            willAssertThat(frenchCardsSet("7h","6d","4c","3h","5h"))
                    .result(StraightHand(SEVEN)),
            willAssertThat(frenchCardsSet("As","2d","4c","3h","5h"))
                    .result(StraightHand(FIVE)),
            willAssertThat(frenchCardsSet("As","Qd","Jd","Kc","Th"))
                    .result(StraightHand(ACE)),

            willAssertThat(frenchCardsSet("7s","7h","7d","6d","Ac"))
                    .result(ThreeOfAKindHand(SEVEN, ACE,SIX)),

            willAssertThat(frenchCardsSet("7s","Ah","7d","6d","Ac"))
                    .result(TwoPairHand(ACE,SEVEN, SIX)),

            willAssertThat(frenchCardsSet("7s","Kh","7d","6d","Ac"))
                    .result(PairHand(SEVEN, ACE,KING,SIX)),

            willAssertThat(frenchCardsSet("Ah","3c","Ks","8c","7s"))
                    .result(HighCardHand(ACE,KING,EIGHT,SEVEN,THREE)),
            //////////////////////////////////////

            /// Six Cards TESTS
            willAssertThat(frenchCardsSet("Ah","Jh","Th","Ad","Kh","Qh"))
                    .result(StraightFlushHand(ACE, HEARTS)),
            willAssertThat(frenchCardsSet("Ah","5h","Th","3h","4h","2h"))
                    .result(StraightFlushHand(FIVE, HEARTS)),

            willAssertThat(frenchCardsSet("Ah","3s","3d","3c","3h","Js"))
                    .result(FourOfAKindHand(THREE, ACE)),
            willAssertThat(frenchCardsSet("Jh","3s","Jd","Jc","3h","Js"))
                    .result(FourOfAKindHand(JACK, THREE)),

            willAssertThat(frenchCardsSet("3d","3c","3h","Ah","Ac","As"))
                    .result(FullHouseHand(ACE, THREE)),
            willAssertThat(frenchCardsSet("Ah","Ac","As","3c","Kh","Ks"))
                    .result(FullHouseHand(ACE, KING)),
            willAssertThat(frenchCardsSet("Ah","Ac","As","Jd","Jc","Kh"))
                    .result(FullHouseHand(ACE, JACK)),

            willAssertThat(frenchCardsSet("Ah","7h","Kh","3c","3h","5h"))
                    .result(FlushHand(ACE,KING,SEVEN,FIVE,THREE, HEARTS)),

            willAssertThat(frenchCardsSet("7s","7h","6d","4c","3h","5h"))
                    .result(StraightHand(SEVEN)),
            willAssertThat(frenchCardsSet("As","Jh","2d","4c","3h","5h"))
                    .result(StraightHand(FIVE)),
            willAssertThat(frenchCardsSet("As","2h","Qd","Jd","Kc","Th"))
                    .result(StraightHand(ACE)),

            willAssertThat(frenchCardsSet("7s","7h","7d","6d","Ac","5h"))
                    .result(ThreeOfAKindHand(SEVEN, ACE,SIX)),

            willAssertThat(frenchCardsSet("7s","Ah","7d","6d","Ac","5h"))
                    .result(TwoPairHand(ACE,SEVEN, SIX)),
            willAssertThat(frenchCardsSet("7s","Kh","7d","3d","Kc","3h"))
                    .result(TwoPairHand(KING,SEVEN, THREE)),

            willAssertThat(frenchCardsSet("7s","Kh","7d","6d","Ac","5h"))
                    .result(PairHand(SEVEN, ACE,KING,SIX)),

            willAssertThat(frenchCardsSet("Ah","2d","3c","Ks","8c","7s"))
                    .result(HighCardHand(ACE,KING,EIGHT,SEVEN,THREE)),
            willAssertThat(frenchCardsSet("Ah","Jc","Ts","2d","8c","7s"))
                    .result(HighCardHand(ACE,JACK,TEN,EIGHT,SEVEN))
            //////////////////////////////////////
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
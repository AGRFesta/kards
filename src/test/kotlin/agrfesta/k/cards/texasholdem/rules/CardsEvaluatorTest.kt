package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.texasholdem.rules.hands.*
import agrfesta.kcards.playingcards.suits.*
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

interface CardsEvaluatorTest {
    fun getCardsEvaluator(): CardsEvaluator

    @TestFactory
    @DisplayName("evaluations")
    fun comparisons() = listOf(
            /// Seven Cards TESTS
            Pair(
                    frenchCardsSet("Ah","Jh","Th","Ad","Ac","Kh","Qh"),
                    StraightFlushHand(ACE, FrenchSeed.HEARTS)),
            Pair(
                    frenchCardsSet("Ah","5h","Th","3h","Ac","4h","2h"),
                    StraightFlushHand(FIVE, FrenchSeed.HEARTS)),

            Pair(
                    frenchCardsSet("Ah","Jc","3s","3d","3c","3h","Js"),
                    FourOfAKindHand(THREE, ACE)),
            Pair(
                    frenchCardsSet("Jh","Jc","3s","Jd","3c","3h","Js"),
                    FourOfAKindHand(JACK, THREE)),

            Pair(
                    frenchCardsSet("3d","3c","3h","Ah","Ac","As","Js"),
                    FullHouseHand(ACE, THREE)),
            Pair(
                    frenchCardsSet("Ah","Ac","As","3d","3c","Kh","Ks"),
                    FullHouseHand(ACE, KING)),
            Pair(
                    frenchCardsSet("Ah","Qs","Ac","As","Jd","Jc","Kh"),
                    FullHouseHand(ACE, JACK)),

            Pair(
                    frenchCardsSet("Ah","7h","Kh","3d","3c","3h","5h"),
                    FlushHand(ACE, KING, SEVEN, FIVE, THREE, FrenchSeed.HEARTS)),
            Pair(
                    frenchCardsSet("Ah","7h","Kh","3d","5c","3h","5h"),
                    FlushHand(ACE, KING, SEVEN, FIVE, THREE, FrenchSeed.HEARTS)),

            Pair(
                    frenchCardsSet("7s","7h","7d","6d","4c","3h","5h"),
                    StraightHand(SEVEN)),
            Pair(
                    frenchCardsSet("As","Jh","2d","Jd","4c","3h","5h"),
                    StraightHand(FIVE)),
            Pair(
                    frenchCardsSet("7s","4h","7d","6d","4c","3h","5h"),
                    StraightHand(SEVEN)),
            Pair(
                    frenchCardsSet("As","2h","Qd","Jd","Kc","3h","Th"),
                    StraightHand(ACE)),

            Pair(
                    frenchCardsSet("7s","7h","7d","6d","Ac","3h","5h"),
                    ThreeOfAKindHand(SEVEN,   ACE, SIX)),

            Pair(
                    frenchCardsSet("7s","Ah","7d","6d","Ac","3h","5h"),
                    TwoPairHand(ACE, SEVEN,  SIX)),
            Pair(
                    frenchCardsSet("7s","Kh","7d","3d","Kc","3h","5h"),
                    TwoPairHand(KING, SEVEN,  FIVE)),

            Pair(
                    frenchCardsSet("7s","Kh","7d","6d","Ac","3h","5h"),
                    PairHand(SEVEN,   ACE, KING, SIX)),

            Pair(
                    frenchCardsSet("Ah","2d","3c","4h","Ks","8c","7s"),
                    HighCardHand(ACE, KING, EIGHT, SEVEN, FOUR)),
            Pair(
                    frenchCardsSet("Ah","Jc","Ts","2d","3c","8c","7s"),
                    HighCardHand(ACE, JACK, TEN, EIGHT, SEVEN)),
            //////////////////////////////////////

            /// Five Cards TESTS
            Pair(
                    frenchCardsSet("Ah","Jh","Th","Kh","Qh"),
                    StraightFlushHand(ACE, FrenchSeed.HEARTS)),
            Pair(
                    frenchCardsSet("Ah","5h","3h","4h","2h"),
                    StraightFlushHand(FIVE, FrenchSeed.HEARTS)),

            Pair(
                    frenchCardsSet("Ah","3s","3d","3c","3h"),
                    FourOfAKindHand(THREE, ACE)),
            Pair(
                    frenchCardsSet("Jh","3s","Jd","Jc","Js"),
                    FourOfAKindHand(JACK, THREE)),

            Pair(
                    frenchCardsSet("3d","3h","Ah","Ac","As"),
                    FullHouseHand(ACE, THREE)),
            Pair(
                    frenchCardsSet("Ah","Ac","Jd","Jc","Jh"),
                    FullHouseHand(JACK, ACE)),

            Pair(
                    frenchCardsSet("Ah","3h","Kh","2h","4h"),
                    FlushHand(ACE, KING, FOUR, THREE, TWO, FrenchSeed.HEARTS)),

            Pair(
                    frenchCardsSet("7h","6d","4c","3h","5h"),
                    StraightHand(SEVEN)),
            Pair(
                    frenchCardsSet("As","2d","4c","3h","5h"),
                    StraightHand(FIVE)),
            Pair(
                    frenchCardsSet("As","Qd","Jd","Kc","Th"),
                    StraightHand(ACE)),

            Pair(
                    frenchCardsSet("7s","7h","7d","6d","Ac"),
                    ThreeOfAKindHand(SEVEN,   ACE, SIX)),

            Pair(
                    frenchCardsSet("7s","Ah","7d","6d","Ac"),
                    TwoPairHand(ACE, SEVEN,  SIX)),

            Pair(
                    frenchCardsSet("7s","Kh","7d","6d","Ac"),
                    PairHand(SEVEN,   ACE, KING, SIX)),

            Pair(
                    frenchCardsSet("Ah","3c","Ks","8c","7s"),
                    HighCardHand(ACE, KING, EIGHT, SEVEN, THREE)),
            //////////////////////////////////////

            /// Six Cards TESTS
            Pair(
                    frenchCardsSet("Ah","Jh","Th","Ad","Kh","Qh"),
                    StraightFlushHand(ACE, FrenchSeed.HEARTS)),
            Pair(
                    frenchCardsSet("Ah","5h","Th","3h","4h","2h"),
                    StraightFlushHand(FIVE, FrenchSeed.HEARTS)),

            Pair(
                    frenchCardsSet("Ah","3s","3d","3c","3h","Js"),
                    FourOfAKindHand(THREE, ACE)),
            Pair(
                    frenchCardsSet("Jh","3s","Jd","Jc","3h","Js"),
                    FourOfAKindHand(JACK, THREE)),

            Pair(
                    frenchCardsSet("3d","3c","3h","Ah","Ac","As"),
                    FullHouseHand(ACE, THREE)),
            Pair(
                    frenchCardsSet("Ah","Ac","As","3c","Kh","Ks"),
                    FullHouseHand(ACE, KING)),
            Pair(
                    frenchCardsSet("Ah","Ac","As","Jd","Jc","Kh"),
                    FullHouseHand(ACE, JACK)),

            Pair(
                    frenchCardsSet("Ah","7h","Kh","3c","3h","5h"),
                    FlushHand(ACE, KING, SEVEN, FIVE, THREE, FrenchSeed.HEARTS)),

            Pair(
                    frenchCardsSet("7s","7h","6d","4c","3h","5h"),
                    StraightHand(SEVEN)),
            Pair(
                    frenchCardsSet("As","Jh","2d","4c","3h","5h"),
                    StraightHand(FIVE)),
            Pair(
                    frenchCardsSet("As","2h","Qd","Jd","Kc","Th"),
                    StraightHand(ACE)),

            Pair(
                    frenchCardsSet("7s","7h","7d","6d","Ac","5h"),
                    ThreeOfAKindHand(SEVEN,   ACE, SIX)),

            Pair(
                    frenchCardsSet("7s","Ah","7d","6d","Ac","5h"),
                    TwoPairHand(ACE, SEVEN,  SIX)),
            Pair(
                    frenchCardsSet("7s","Kh","7d","3d","Kc","3h"),
                    TwoPairHand(KING, SEVEN,  THREE)),

            Pair(
                    frenchCardsSet("7s","Kh","7d","6d","Ac","5h"),
                    PairHand(SEVEN,   ACE, KING, SIX)),

            Pair(
                    frenchCardsSet("Ah","2d","3c","Ks","8c","7s"),
                    HighCardHand(ACE, KING, EIGHT, SEVEN, THREE)),
            Pair(
                    frenchCardsSet("Ah","Jc","Ts","2d","8c","7s"),
                    HighCardHand(ACE, JACK, TEN, EIGHT, SEVEN))
            //////////////////////////////////////
    ).map {
        DynamicTest.dynamicTest(
                "${it.first} -> ${it.second}"
        ) {
            assertThat(getCardsEvaluator().evaluate(it.first)).isEqualTo(it.second)
        }
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
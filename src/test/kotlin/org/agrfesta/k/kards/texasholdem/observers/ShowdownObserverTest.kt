package org.agrfesta.k.kards.texasholdem.observers

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.extracting
import assertk.assertions.isEmpty
import assertk.assertions.isTrue
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.agrfesta.k.cards.playingcards.suits.ACE
import org.agrfesta.k.cards.playingcards.suits.EIGHT
import org.agrfesta.k.cards.playingcards.suits.JACK
import org.agrfesta.k.cards.playingcards.suits.NINE
import org.agrfesta.k.cards.playingcards.suits.TEN
import org.agrfesta.k.cards.playingcards.suits.frenchCardsSet
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluatorBaseImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.CALL
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.RAISE
import org.agrfesta.k.kards.texasholdem.rules.gameplay.ShowdownImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.anInGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.board
import org.agrfesta.k.kards.texasholdem.rules.gameplay.buildMutablePot
import org.agrfesta.k.kards.texasholdem.rules.hands.PairHand
import org.agrfesta.k.kards.texasholdem.rules.hands.ThreeOfAKindHand
import org.agrfesta.k.kards.texasholdem.rules.hands.TwoPairHand
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.collections.set

@DisplayName("Showdown Observer tests")
class ShowdownObserverTest {

    @Test
    @DisplayName("A Showdown Observer will be notify only once with result of the Showdown")
    fun showdownObserverStory000() {
        val result = slot<Collection<ShowdownPlayerResult>>()
        val observerMock = mockk<ShowdownObserver>()
        every { observerMock.notifyResult(capture(result)) } just Runs
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = RAISE, cards = frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = CALL, cards = frenchCardsSet("Jd","7c"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = CALL, cards = frenchCardsSet("9d","9c"))
        val pot = buildMutablePot()
        pot[alex] = 300u
        pot[poly] = 300u
        pot[jane] = 100u
        val board = board("Ac","Js","9s", "8c", "3d")

        ShowdownImpl(CardsEvaluatorBaseImpl(),observerMock).execute(pot,board)

        verify(exactly = 1) { observerMock.notifyResult(any()) }
        assertThat(result.isCaptured).isTrue()
        assertThat(result.captured).extracting({it.player},{it.evaluation},{it.prize})
                .containsOnly(Triple(alex, PairHand(ACE, JACK,TEN,NINE), 400u),
                              Triple(poly, PairHand(JACK, ACE,NINE,EIGHT), null),
                              Triple(jane, ThreeOfAKindHand(NINE, ACE,JACK), 300u)
                        )

    }

    @Test
    @DisplayName("A Showdown on an empty Pot will notify the Observer with an empty list")
    fun showdownObserverStory001() {
        val result = slot<Collection<ShowdownPlayerResult>>()
        val observerMock = mockk<ShowdownObserver>()
        every { observerMock.notifyResult(capture(result)) } just Runs
        val pot = buildMutablePot()
        val board = board("Ac","Js","9s", "8c", "3d")

        ShowdownImpl(CardsEvaluatorBaseImpl(),observerMock).execute(pot,board)

        verify(exactly = 1) { observerMock.notifyResult(any()) }
        assertThat(result.isCaptured).isTrue()
        assertThat(result.captured).isEmpty()
    }

    @Test
    @DisplayName("notify(): Showdown with single winner -> observer is notified with winner and prize")
    fun notifyTest002() {
        val result = slot<Collection<ShowdownPlayerResult>>()
        val observerMock = mockk<ShowdownObserver>()
        every { observerMock.notifyResult(capture(result)) } just Runs
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, status = CALL, cards = frenchCardsSet("Ad","9h"))
        val poly = anInGamePlayer(name = "Poly", stack = 2000u, status = CALL, cards = frenchCardsSet("As","9d"))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, status = CALL, cards = frenchCardsSet("Ah","Jc"))
        val dave = anInGamePlayer(name = "Dave", stack = 1000u, status = FOLD, cards = frenchCardsSet("Qh","Qc"))
        val pot = buildMutablePot()
        pot[alex] = 225u
        pot[poly] = 225u
        pot[jane] = 225u
        pot[dave] = 25u
        val board = board("Ac","Js","9s", "8c", "3h")

        ShowdownImpl(CardsEvaluatorBaseImpl(),observerMock).execute(pot,board)

        verify(exactly = 1) { observerMock.notifyResult(any()) }
        assertThat(result.isCaptured).isTrue()
        assertThat(result.captured).extracting({it.player},{it.evaluation},{it.prize})
            .containsOnly(
                Triple(alex, TwoPairHand(ACE,NINE, JACK), null),
                Triple(poly, TwoPairHand(ACE,NINE, JACK), null),
                Triple(jane, TwoPairHand(ACE,JACK, NINE), 700u)
            )

    }

    @Test
    @DisplayName("""implemented observer without method override -> 
        can be notified but will use default implementation (do nothing)""")
    fun implementationTest000() {
        val observer = object: ShowdownObserver {}

        observer.notifyResult(showdownPlayerResultCollection)
    }

}

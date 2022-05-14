package org.agrfesta.k.kards.texasholdem.observers

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.extracting
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
import org.agrfesta.k.kards.texasholdem.rules.gameplay.buildMutablePot
import org.agrfesta.k.kards.texasholdem.rules.hands.PairHand
import org.agrfesta.k.kards.texasholdem.rules.hands.ThreeOfAKindHand
import org.agrfesta.k.kards.texasholdem.rules.hands.TwoPairHand
import org.agrfesta.k.kards.texasholdem.testing.mothers.anInGamePlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.board
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.aTenInGamePlayerTable
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.bigB
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.button
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.smallB
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.utg
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
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = RAISE, cards = frenchCardsSet("Ad","Ts")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = CALL, cards = frenchCardsSet("Jd","7c")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = CALL, cards = frenchCardsSet("9d","9c")) }
        }
        val pot = buildMutablePot()
        pot[button] = 300u
        pot[smallB] = 300u
        pot[bigB] = 100u
        val board = board("Ac","Js","9s", "8c", "3d")

        ShowdownImpl(CardsEvaluatorBaseImpl(),observerMock).execute(pot,board,table)

        verify(exactly = 1) { observerMock.notifyResult(any()) }
        assertThat(result.isCaptured).isTrue()
        assertThat(result.captured).extracting({it.player},{it.evaluation},{it.prize})
                .containsOnly(Triple(button, PairHand(ACE, JACK,TEN,NINE), 400u),
                              Triple(smallB, PairHand(JACK, ACE,NINE,EIGHT), null),
                              Triple(bigB, ThreeOfAKindHand(NINE, ACE,JACK), 300u)
                        )

    }

    @Test
    @DisplayName("notify(): Showdown with single winner -> observer is notified with winner and prize")
    fun notifyTest002() {
        val result = slot<Collection<ShowdownPlayerResult>>()
        val observerMock = mockk<ShowdownObserver>()
        every { observerMock.notifyResult(capture(result)) } just Runs
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 2000u, status = CALL, cards = frenchCardsSet("Ad","9h")) }
            smallB with { anInGamePlayer(it, stack = 2000u, status = CALL, cards = frenchCardsSet("As","9d")) }
            bigB with { anInGamePlayer(it, stack = 2000u, status = CALL, cards = frenchCardsSet("Ah","Jc")) }
            utg with { anInGamePlayer(it, stack = 1000u, status = FOLD, cards = frenchCardsSet("Qh","Qc")) }
        }
        val pot = buildMutablePot()
        pot[button] = 225u
        pot[smallB] = 225u
        pot[bigB] = 225u
        pot[utg] = 25u
        val board = board("Ac","Js","9s", "8c", "3h")

        ShowdownImpl(CardsEvaluatorBaseImpl(),observerMock).execute(pot,board,table)

        verify(exactly = 1) { observerMock.notifyResult(any()) }
        assertThat(result.isCaptured).isTrue()
        assertThat(result.captured).extracting({it.player},{it.evaluation},{it.prize})
            .containsOnly(
                Triple(button, TwoPairHand(ACE,NINE, JACK), null),
                Triple(smallB, TwoPairHand(ACE,NINE, JACK), null),
                Triple(bigB, TwoPairHand(ACE,JACK, NINE), 700u)
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

package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.playingcards.suits.ACE
import agrfesta.k.cards.playingcards.suits.EIGHT
import agrfesta.k.cards.playingcards.suits.JACK
import agrfesta.k.cards.playingcards.suits.NINE
import agrfesta.k.cards.playingcards.suits.TEN
import agrfesta.k.cards.playingcards.suits.frenchCardsSet
import agrfesta.k.cards.texasholdem.rules.CardsEvaluatorBaseImpl
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus
import agrfesta.k.cards.texasholdem.rules.gameplay.ShowdownImpl
import agrfesta.k.cards.texasholdem.rules.gameplay.anInGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.board
import agrfesta.k.cards.texasholdem.rules.gameplay.buildMutablePot
import agrfesta.k.cards.texasholdem.rules.hands.PairHand
import agrfesta.k.cards.texasholdem.rules.hands.ThreeOfAKindHand
import agrfesta.k.cards.texasholdem.rules.hands.TwoPairHand
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
        val alex = anInGamePlayer("Alex", 1000, PlayerStatus.RAISE, frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer("Poly", 1000, PlayerStatus.CALL,  frenchCardsSet("Jd","7c"))
        val jane = anInGamePlayer("Jane", 1000, PlayerStatus.CALL,  frenchCardsSet("9d","9c"))
        val pot = buildMutablePot()
        pot[alex] = 300
        pot[poly] = 300
        pot[jane] = 100
        val board = board("Ac","Js","9s", "8c", "3d")

        ShowdownImpl(CardsEvaluatorBaseImpl(),observerMock).execute(pot,board)

        verify(exactly = 1) { observerMock.notifyResult(any()) }
        assertThat(result.isCaptured).isTrue()
        assertThat(result.captured).extracting({it.player.player},{it.evaluation},{it.prize})
                .containsOnly(Triple(alex.player, PairHand(ACE, JACK,TEN,NINE), 400),
                              Triple(poly.player, PairHand(JACK, ACE,NINE,EIGHT), null),
                              Triple(jane.player, ThreeOfAKindHand(NINE, ACE,JACK), 300)
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
        val alex = anInGamePlayer("Alex", 2000, PlayerStatus.CALL, frenchCardsSet("Ad","9h"))
        val poly = anInGamePlayer("Poly", 2000, PlayerStatus.CALL, frenchCardsSet("As","9d"))
        val jane = anInGamePlayer("Jane", 2000, PlayerStatus.CALL, frenchCardsSet("Ah","Jc"))
        val dave = anInGamePlayer("Dave", 1000, PlayerStatus.FOLD, frenchCardsSet("Qh","Qc"))
        val pot = buildMutablePot()
        pot[alex] = 225
        pot[poly] = 225
        pot[jane] = 225
        pot[dave] = 25
        val board = board("Ac","Js","9s", "8c", "3h")

        ShowdownImpl(CardsEvaluatorBaseImpl(),observerMock).execute(pot,board)

        verify(exactly = 1) { observerMock.notifyResult(any()) }
        assertThat(result.isCaptured).isTrue()
        assertThat(result.captured).extracting({it.player.player},{it.evaluation},{it.prize})
            .containsOnly(
                Triple(alex.player, TwoPairHand(ACE,NINE, JACK), null),
                Triple(poly.player, TwoPairHand(ACE,NINE, JACK), null),
                Triple(jane.player, TwoPairHand(ACE,JACK, NINE), 700)
            )

    }

    @Test
    @DisplayName("""multipleShowdownObserverOf(): multi observer composed by null and non-null observer -> 
        only one observer will be notified""")
    fun multipleShowdownObserverOfTest000() {
        val observer: ShowdownObserver = mockk(relaxed = true)
        val multipleShowdownObserver = multipleShowdownObserverOf(null, observer)

        multipleShowdownObserver.notifyResult(showdownPlayerResultCollection)

        assertNotifiedShowdownObserver(observer)
    }
    @Test
    @DisplayName("""multipleShowdownObserverOf(): multi observer composed by two observer -> 
        both observer will be notified""")
    fun multipleShowdownObserverOfTest001() {
        val observerA: ShowdownObserver = mockk(relaxed = true)
        val observerB: ShowdownObserver = mockk(relaxed = true)
        val observerC: ShowdownObserver = mockk(relaxed = true)
        val multipleShowdownObserver = multipleShowdownObserverOf(observerA, observerB)

        multipleShowdownObserver.notifyResult(showdownPlayerResultCollection)

        assertNotifiedShowdownObserver(observerA)
        assertNotifiedShowdownObserver(observerB)
        assertNotNotifiedShowdownObserver(observerC)
    }

    @Test
    @DisplayName("""implemented observer without method override -> 
        can be notified but will use default implementation (do nothing)""")
    fun implementationTest000() {
        val observer = object: ShowdownObserver {}

        observer.notifyResult(showdownPlayerResultCollection)
    }

}

package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.DeckListImpl
import agrfesta.k.cards.texasholdem.rules.gameplay.Board
import agrfesta.k.cards.texasholdem.rules.gameplay.Dealer
import agrfesta.k.cards.texasholdem.rules.gameplay.DealerFactory
import agrfesta.k.cards.texasholdem.rules.gameplay.EmptyBoard
import agrfesta.k.cards.texasholdem.rules.gameplay.FlopBoard
import agrfesta.k.cards.texasholdem.rules.gameplay.GameBuilder.Companion.buildingAGame
import agrfesta.k.cards.texasholdem.rules.gameplay.GameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus
import agrfesta.k.cards.texasholdem.rules.gameplay.Pot
import agrfesta.k.cards.texasholdem.rules.gameplay.RiverBoard
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import agrfesta.k.cards.texasholdem.rules.gameplay.TurnBoard
import agrfesta.k.cards.texasholdem.rules.gameplay.aGamePayments
import agrfesta.k.cards.texasholdem.rules.gameplay.anInGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.buildPot
import agrfesta.k.cards.texasholdem.rules.gameplay.cardList
import agrfesta.k.cards.texasholdem.rules.gameplay.cards
import agrfesta.k.cards.texasholdem.rules.gameplay.receiveFrom
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Game Observer tests")
class GameObserverTest {
    private val payments = aGamePayments()
    private var alex = anInGamePlayer()
    private var poly = anInGamePlayer()

    @BeforeEach
    fun init() {
        alex = anInGamePlayer("Alex",1000)
        poly = anInGamePlayer("Poly",1000)
    }

    private fun observerMock(winner: CapturingSlot<Player>, prize: CapturingSlot<Int>, boards: MutableList<Board>)
            : GameObserver {
        val observerMock = mockk<GameObserver>()
        every { observerMock.notifyStartingPhase(capture(boards)) } just Runs
        every { observerMock.notifyWinner(capture(winner),capture(prize)) } just Runs
        return observerMock
    }
    private fun dealerMock(collectPotBody: () -> Pot): Dealer {
        val dealer = mockk<Dealer>()
        every { dealer.collectPot() } returns collectPotBody.invoke()
        return dealer
    }
    private val defaultDealer: () -> Pot = {
        assert(false) { "The game is not following the correct phases sequence" }
        buildPot()
    }
    private val allChecksDealer: () -> Pot = {
        alex.status = PlayerStatus.CALL
        poly.status = PlayerStatus.CALL
        buildPot()
    }

    @Test
    @DisplayName("A Game ends at pre-flop -> the observer is notified only at pre-flop and for the winner")
    fun gameObserverStory000() {
        val table = Table(listOf(alex,poly), 0)
        val deck = DeckListImpl(cardList("Ah","Ac","3h","5s"))
        val preFlopDealer: () -> Pot = {
            val pot = buildPot()
            pot.receiveFrom(alex,200)
            pot.receiveFrom(poly,100)
            alex.status = PlayerStatus.RAISE
            poly.status = PlayerStatus.FOLD
            pot
        }
        val flopDealer: () -> Pot = {
            assert(false) { "The game should finish at pre-flop but is collecting pot at flop" }
            buildPot()
        }
        val dealerFactory = mockk<DealerFactory>()
        every { dealerFactory.preFlopDealer(any(),any()) } answers { dealerMock(preFlopDealer) }
        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers { dealerMock(flopDealer) }

        val boards = mutableListOf<Board>()
        val winner = slot<Player>()
        val prize = slot<Int>()
        val observerMock = observerMock(winner, prize, boards)

        buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .observedBy(observerMock)
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .build()
                .play()

        verify(exactly = 1) { observerMock.notifyStartingPhase(any()) }
        verify(exactly = 1) { observerMock.notifyWinner(any(),any()) }
        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown

        assertThat(boards).hasSize(1)
        assertThat(boards[0]).isInstanceOf(EmptyBoard::class)
        assertThat(boards[0].cards()).isEmpty()

        assertThat(winner.captured).isEqualTo(alex.player)
        assertThat(prize.captured).isEqualTo(300)
    }

    @Test
    @DisplayName("A Game ends at flop -> the observer is notified only at pre-flop, flop and for the winner")
    fun gameObserverStory001() {
        val table = Table(listOf(alex,poly), 0)
        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h"))
        val preFlopDealer: () -> Pot = {
            val pot = buildPot()
            pot.receiveFrom(alex,200)
            pot.receiveFrom(poly,200)
            alex.status = PlayerStatus.CALL
            poly.status = PlayerStatus.CALL
            pot
        }
        val flopDealer: () -> Pot = {
            val pot = buildPot()
            pot.receiveFrom(alex,400)
            pot.receiveFrom(poly,200)
            alex.status = PlayerStatus.RAISE
            poly.status = PlayerStatus.FOLD
            pot
        }
        val turnDealer: () -> Pot = {
            assert(false) { "The game should finish at flop but is collecting pot at turn" }
            buildPot()
        }
        val dealerFactory = mockk<DealerFactory>()
        every { dealerFactory.preFlopDealer(any(),any()) } returns dealerMock(preFlopDealer)
        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers {
            when (secondArg<GameContext>().board) {
                is FlopBoard -> dealerMock(flopDealer)
                is TurnBoard -> dealerMock(turnDealer)
                else -> dealerMock(defaultDealer)
            }
        }

        val boards = mutableListOf<Board>()
        val winner = slot<Player>()
        val prize = slot<Int>()
        val observerMock = observerMock(winner, prize, boards)

        buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .observedBy(observerMock)
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .build()
                .play()

        verify(exactly = 2) { observerMock.notifyStartingPhase(any()) }
        verify(exactly = 1) { observerMock.notifyWinner(any(),any()) }
        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown

        assertThat(boards).hasSize(2)
        assertThat(boards[0]).isInstanceOf(EmptyBoard::class)
        assertThat(boards[0].cards()).isEmpty()
        assertThat(boards[1]).isInstanceOf(FlopBoard::class)
        assertThat(boards[1].cards()).containsOnly(*cards("Jh","Js","7h"))

        assertThat(winner.captured).isEqualTo(alex.player)
        assertThat(prize.captured).isEqualTo(1000)
    }

    @Test
    @DisplayName("A Game ends at turn -> the observer is notified only at pre-flop, flop, turn and for the winner")
    fun gameObserverStory002() {
        val table = Table(listOf(alex,poly), 0)
        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h", "5d"))
        val preFlopDealer: () -> Pot = {
            val pot = buildPot()
            pot.receiveFrom(alex,200)
            pot.receiveFrom(poly,200)
            alex.status = PlayerStatus.CALL
            poly.status = PlayerStatus.CALL
            pot
        }
        val turnDealer: () -> Pot = {
            val pot = buildPot()
            pot.receiveFrom(alex,500)
            pot.receiveFrom(poly,200)
            alex.status = PlayerStatus.RAISE
            poly.status = PlayerStatus.FOLD
            pot
        }
        val riverDealer: () -> Pot = {
            assert(false) { "The game should finish at turn but is collecting pot at river" }
            buildPot()
        }
        val dealerFactory = mockk<DealerFactory>()
        every { dealerFactory.preFlopDealer(any(),any()) } returns dealerMock(preFlopDealer)
        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers {
            when (secondArg<GameContext>().board) {
                is FlopBoard -> dealerMock(allChecksDealer)
                is TurnBoard -> dealerMock(turnDealer)
                is RiverBoard -> dealerMock(riverDealer)
                else -> dealerMock(defaultDealer)
            }
        }

        val boards = mutableListOf<Board>()
        val winner = slot<Player>()
        val prize = slot<Int>()
        val observerMock = observerMock(winner, prize, boards)

        buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .observedBy(observerMock)
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .build()
                .play()

        verify(exactly = 3) { observerMock.notifyStartingPhase(any()) }
        verify(exactly = 1) { observerMock.notifyWinner(any(),any()) }
        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown

        assertThat(boards).hasSize(3)
        assertThat(boards[0]).isInstanceOf(EmptyBoard::class)
        assertThat(boards[0].cards()).isEmpty()
        assertThat(boards[1]).isInstanceOf(FlopBoard::class)
        assertThat(boards[1].cards()).containsOnly(*cards("Jh","Js","7h"))
        assertThat(boards[2]).isInstanceOf(TurnBoard::class)
        assertThat(boards[2].cards()).containsOnly(*cards("Jh","Js","7h", "5d"))

        assertThat(winner.captured).isEqualTo(alex.player)
        assertThat(prize.captured).isEqualTo(1100)
    }

    @Test
    @DisplayName("A Game ends at river -> the observer is notified in all phases and for the winner")
    fun gameObserverStory003() {
        val table = Table(listOf(alex,poly), 0)
        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h", "5d", "Td"))
        val preFlopDealer: () -> Pot = {
            val pot = buildPot()
            pot.receiveFrom(alex,200)
            pot.receiveFrom(poly,200)
            alex.status = PlayerStatus.CALL
            poly.status = PlayerStatus.CALL
            pot
        }
        val riverDealer: () -> Pot = {
            val pot = buildPot()
            pot.receiveFrom(alex,300)
            pot.receiveFrom(poly,200)
            alex.status = PlayerStatus.RAISE
            poly.status = PlayerStatus.FOLD
            pot
        }
        val dealerFactory = mockk<DealerFactory>()
        every { dealerFactory.preFlopDealer(any(),any()) } returns dealerMock(preFlopDealer)
        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers {
            when (secondArg<GameContext>().board) {
                is FlopBoard -> dealerMock(allChecksDealer)
                is TurnBoard -> dealerMock(allChecksDealer)
                is RiverBoard -> dealerMock(riverDealer)
                else -> dealerMock(defaultDealer)
            }
        }

        val boards = mutableListOf<Board>()
        val winner = slot<Player>()
        val prize = slot<Int>()
        val observerMock = observerMock(winner, prize, boards)

        buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .observedBy(observerMock)
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .build()
                .play()

        verify(exactly = 4) { observerMock.notifyStartingPhase(any()) }
        verify(exactly = 1) { observerMock.notifyWinner(any(),any()) }
        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown

        assertThat(boards).hasSize(4)
        assertThat(boards[0]).isInstanceOf(EmptyBoard::class)
        assertThat(boards[0].cards()).isEmpty()
        assertThat(boards[1]).isInstanceOf(FlopBoard::class)
        assertThat(boards[1].cards()).containsOnly(*cards("Jh","Js","7h"))
        assertThat(boards[2]).isInstanceOf(TurnBoard::class)
        assertThat(boards[2].cards()).containsOnly(*cards("Jh","Js","7h", "5d"))
        assertThat(boards[3]).isInstanceOf(RiverBoard::class)
        assertThat(boards[3].cards()).containsOnly(*cards("Jh","Js","7h", "5d", "Td"))

        assertThat(winner.captured).isEqualTo(alex.player)
        assertThat(prize.captured).isEqualTo(900)
    }

    @Test
    @DisplayName("A Game ends at showdown -> the observer is notified in all phases and with result of showdown")
    fun gameObserverStory004() {
        val table = Table(listOf(alex,poly), 0)
        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h", "5d", "Td"))
        val preFlopDealer: () -> Pot = {
            alex.status = PlayerStatus.CALL
            poly.status = PlayerStatus.CALL
            buildPot()
        }

        val boards = mutableListOf<Board>()
        val observerMock = mockk<GameObserver>()
        every { observerMock.notifyStartingPhase(capture(boards)) } just Runs
        every { observerMock.notifyResult(any()) } just Runs

        val dealerFactory = mockk<DealerFactory>()
        every { dealerFactory.preFlopDealer(any(),any()) } returns dealerMock(preFlopDealer)
        every { dealerFactory.postFlopDealer(any(),any(),any()) } returns dealerMock(allChecksDealer)

        buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .observedBy(observerMock)
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .build()
                .play()

        verify(exactly = 4) { observerMock.notifyStartingPhase(any()) }
        verify(exactly = 0) { observerMock.notifyWinner(any(),any()) } // winner at showdown
        verify(exactly = 1) { observerMock.notifyResult(any()) }

        assertThat(boards).hasSize(4)
        assertThat(boards[0]).isInstanceOf(EmptyBoard::class)
        assertThat(boards[0].cards()).isEmpty()
        assertThat(boards[1]).isInstanceOf(FlopBoard::class)
        assertThat(boards[1].cards()).containsOnly(*cards("Jh","Js","7h"))
        assertThat(boards[2]).isInstanceOf(TurnBoard::class)
        assertThat(boards[2].cards()).containsOnly(*cards("Jh","Js","7h", "5d"))
        assertThat(boards[3]).isInstanceOf(RiverBoard::class)
        assertThat(boards[3].cards()).containsOnly(*cards("Jh","Js","7h", "5d", "Td"))
    }
}

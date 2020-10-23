package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.DeckListImpl
import agrfesta.k.cards.texasholdem.rules.gameplay.Board
import agrfesta.k.cards.texasholdem.rules.gameplay.Dealer
import agrfesta.k.cards.texasholdem.rules.gameplay.DealerFactory
import agrfesta.k.cards.texasholdem.rules.gameplay.EmptyBoard
import agrfesta.k.cards.texasholdem.rules.gameplay.FlopBoard
import agrfesta.k.cards.texasholdem.rules.gameplay.GameBuilder.Companion.buildingAGame
import agrfesta.k.cards.texasholdem.rules.gameplay.GameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus
import agrfesta.k.cards.texasholdem.rules.gameplay.Pot
import agrfesta.k.cards.texasholdem.rules.gameplay.RiverBoard
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import agrfesta.k.cards.texasholdem.rules.gameplay.TurnBoard
import agrfesta.k.cards.texasholdem.rules.gameplay.aGamePayments
import agrfesta.k.cards.texasholdem.rules.gameplay.aPlayerWithName
import agrfesta.k.cards.texasholdem.rules.gameplay.buildPot
import agrfesta.k.cards.texasholdem.rules.gameplay.cardList
import agrfesta.k.cards.texasholdem.rules.gameplay.cards
import agrfesta.k.cards.texasholdem.rules.gameplay.utils.BuilderEnrich
import agrfesta.k.cards.texasholdem.rules.gameplay.utils.dealerMockFromBuilder
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
    private lateinit var alex: PlayerStack
    private lateinit var poly: PlayerStack

    @BeforeEach
    fun init() {
        alex = PlayerStack(aPlayerWithName("Alex"),1000)
        poly = PlayerStack(aPlayerWithName("Poly"),1000)
    }

    private fun observerMock(result: CapturingSlot<GameResult>, boards: MutableList<Board>)
            : GameObserver {
        val observerMock = mockk<GameObserver>()
        every { observerMock.notifyStartingPhase(capture(boards)) } just Runs
        every { observerMock.notifyWinner(capture(result)) } just Runs
        return observerMock
    }
    private fun dealerMock(gameContext: GameContext, collectPotBody: (Table<InGamePlayer>) -> Pot): Dealer {
        val dealer = mockk<Dealer>()
        every { dealer.collectPot() } returns collectPotBody.invoke(gameContext.table)
        return dealer
    }
    private val defaultDealer: (Table<InGamePlayer>) -> Pot = {
        assert(false) { "The game is not following the correct phases sequence" }
        buildPot()
    }
    private val allChecksDealer: (Table<InGamePlayer>) -> Pot = {
        it.findPlayerBySeatName(alex.getSeatName())?.status = PlayerStatus.CALL
        it.findPlayerBySeatName(poly.getSeatName())?.status = PlayerStatus.CALL
        buildPot()
    }

    @Test
    @DisplayName("A Game ends at pre-flop -> the observer is notified only at pre-flop and for the winner")
    fun gameObserverStory000() {
        val table = Table(listOf(alex,poly), 0)
        val deck = DeckListImpl(cardList("Ah","Ac","3h","5s"))
        val preFlopDealer: BuilderEnrich = {
            it.receiveCallFrom(poly.player, 100)
                    .receiveRaiseFrom(alex.player, 200)
                    .receiveFoldFrom(poly.player)
        }
        val flopDealer: (Table<InGamePlayer>) -> Pot = {
            assert(false) { "The game should finish at pre-flop but is collecting pot at flop" }
            buildPot()
        }
        val dealerFactory = mockk<DealerFactory>()
        every { dealerFactory.preFlopDealer(any(),any()) } answers
                { dealerMockFromBuilder(firstArg(), preFlopDealer) }
        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers { dealerMock(firstArg(), flopDealer) }

        val boards = mutableListOf<Board>()
        val result = slot<GameResult>()
        val observerMock = observerMock(result, boards)

        buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .observedBy(observerMock)
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .build()
                .play()

        verify(exactly = 1) { observerMock.notifyStartingPhase(any()) }
        verify(exactly = 1) { observerMock.notifyWinner(any()) }
        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown

        assertThat(boards).hasSize(1)
        assertThat(boards[0]).isInstanceOf(EmptyBoard::class)
        assertThat(boards[0].cards()).isEmpty()

        assertThat(result.captured.winner).isEqualTo(alex.player)
        assertThat(result.captured.prize).isEqualTo(300)
    }

    @Test
    @DisplayName("A Game ends at flop -> the observer is notified only at pre-flop, flop and for the winner")
    fun gameObserverStory001() {
        val table = Table(listOf(alex,poly), 0)
        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h"))
        val preFlopDealer: BuilderEnrich = {
            it.receiveCallFrom(alex.player, 200)
                    .receiveRaiseFrom(poly.player, 200)
        }
        val flopDealer: BuilderEnrich = {
            it.receiveCallFrom(poly.player, 200)
                    .receiveRaiseFrom(alex.player, 400)
                    .receiveFoldFrom(poly.player)
        }
        val turnDealer: (Table<InGamePlayer>) -> Pot = {
            assert(false) { "The game should finish at flop but is collecting pot at turn" }
            buildPot()
        }
        val dealerFactory = mockk<DealerFactory>()
        every { dealerFactory.preFlopDealer(any(),any()) } answers
                { dealerMockFromBuilder(firstArg(), preFlopDealer) }
        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers {
            when (secondArg<GameContext>().board) {
                is FlopBoard -> dealerMockFromBuilder(secondArg(), flopDealer)
                is TurnBoard -> dealerMock(firstArg(), turnDealer)
                else -> dealerMock(firstArg(), defaultDealer)
            }
        }

        val boards = mutableListOf<Board>()
        val result = slot<GameResult>()
        val observerMock = observerMock(result, boards)

        buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .observedBy(observerMock)
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .build()
                .play()

        verify(exactly = 2) { observerMock.notifyStartingPhase(any()) }
        verify(exactly = 1) { observerMock.notifyWinner(any()) }
        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown

        assertThat(boards).hasSize(2)
        assertThat(boards[0]).isInstanceOf(EmptyBoard::class)
        assertThat(boards[0].cards()).isEmpty()
        assertThat(boards[1]).isInstanceOf(FlopBoard::class)
        assertThat(boards[1].cards()).containsOnly(*cards("Jh","Js","7h"))

        assertThat(result.captured.winner).isEqualTo(alex.player)
        assertThat(result.captured.prize).isEqualTo(1000)
    }

    @Test
    @DisplayName("A Game ends at turn -> the observer is notified only at pre-flop, flop, turn and for the winner")
    fun gameObserverStory002() {
        val table = Table(listOf(alex,poly), 0)
        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h", "5d"))
        val preFlopDealer: BuilderEnrich = {
            it.receiveCallFrom(alex.player, 200)
                    .receiveCallFrom(poly.player, 200)
        }
        val turnDealer: BuilderEnrich = {
            it.receiveCallFrom(poly.player, 200)
                    .receiveRaiseFrom(alex.player, 500)
                    .receiveFoldFrom(poly.player)
        }
        val riverDealer: (Table<InGamePlayer>) -> Pot = {
            assert(false) { "The game should finish at turn but is collecting pot at river" }
            buildPot()
        }
        val dealerFactory = mockk<DealerFactory>()
        every { dealerFactory.preFlopDealer(any(),any()) } answers
                { dealerMockFromBuilder(firstArg(), preFlopDealer) }
        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers {
            when (secondArg<GameContext>().board) {
                is FlopBoard -> dealerMock(secondArg(), allChecksDealer)
                is TurnBoard -> dealerMockFromBuilder(secondArg(), turnDealer)
                is RiverBoard -> dealerMock(secondArg(), riverDealer)
                else -> dealerMock(secondArg(), defaultDealer)
            }
        }

        val boards = mutableListOf<Board>()
        val result = slot<GameResult>()
        val observerMock = observerMock(result, boards)

        buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .observedBy(observerMock)
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .build()
                .play()

        verify(exactly = 3) { observerMock.notifyStartingPhase(any()) }
        verify(exactly = 1) { observerMock.notifyWinner(any()) }
        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown

        assertThat(boards).hasSize(3)
        assertThat(boards[0]).isInstanceOf(EmptyBoard::class)
        assertThat(boards[0].cards()).isEmpty()
        assertThat(boards[1]).isInstanceOf(FlopBoard::class)
        assertThat(boards[1].cards()).containsOnly(*cards("Jh","Js","7h"))
        assertThat(boards[2]).isInstanceOf(TurnBoard::class)
        assertThat(boards[2].cards()).containsOnly(*cards("Jh","Js","7h", "5d"))

        assertThat(result.captured.winner).isEqualTo(alex.player)
        assertThat(result.captured.prize).isEqualTo(1100)
    }

    @Test
    @DisplayName("A Game ends at river -> the observer is notified in all phases and for the winner")
    fun gameObserverStory003() {
        val table = Table(listOf(alex,poly), 0)
        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h", "5d", "Td"))
        val preFlopDealer: BuilderEnrich = {
            it.receiveCallFrom(alex.player, 200)
                    .receiveCallFrom(poly.player, 200)
        }
        val riverDealer: BuilderEnrich = {
            it.receiveCallFrom(poly.player, 200)
                    .receiveRaiseFrom(alex.player, 300)
                    .receiveFoldFrom(poly.player)
        }
        val dealerFactory = mockk<DealerFactory>()
        every { dealerFactory.preFlopDealer(any(),any()) } answers
                { dealerMockFromBuilder(firstArg(), preFlopDealer) }
        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers {
            when (secondArg<GameContext>().board) {
                is FlopBoard -> dealerMock(secondArg(), allChecksDealer)
                is TurnBoard -> dealerMock(secondArg(), allChecksDealer)
                is RiverBoard -> dealerMockFromBuilder(secondArg(), riverDealer)
                else -> dealerMock(secondArg(), defaultDealer)
            }
        }

        val boards = mutableListOf<Board>()
        val result = slot<GameResult>()
        val observerMock = observerMock(result, boards)

        buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .observedBy(observerMock)
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .build()
                .play()

        verify(exactly = 4) { observerMock.notifyStartingPhase(any()) }
        verify(exactly = 1) { observerMock.notifyWinner(any()) }
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

        assertThat(result.captured.winner).isEqualTo(alex.player)
        assertThat(result.captured.prize).isEqualTo(900)
    }

    @Test
    @DisplayName("A Game ends at showdown -> the observer is notified in all phases and with result of showdown")
    fun gameObserverStory004() {
        val table = Table(listOf(alex,poly), 0)
        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h", "5d", "Td"))
        val preFlopDealer: BuilderEnrich = {
            it.receiveCallFrom(alex.player, 200)
                    .receiveCallFrom(poly.player, 200)
        }

        val boards = mutableListOf<Board>()
        val observerMock = mockk<GameObserver>()
        every { observerMock.notifyStartingPhase(capture(boards)) } just Runs
        every { observerMock.notifyResult(any()) } just Runs

        val dealerFactory = mockk<DealerFactory>()
        every { dealerFactory.preFlopDealer(any(),any()) } answers
                { dealerMockFromBuilder(firstArg(), preFlopDealer) }
        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers
                { dealerMock(secondArg(), allChecksDealer) }

        buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .observedBy(observerMock)
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .build()
                .play()

        verify(exactly = 4) { observerMock.notifyStartingPhase(any()) }
        verify(exactly = 0) { observerMock.notifyWinner(any()) } // winner at showdown
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

package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus
import agrfesta.k.cards.texasholdem.rules.gameplay.MutablePot
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import agrfesta.k.cards.texasholdem.rules.gameplay.ViewGameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.aGamePayments
import agrfesta.k.cards.texasholdem.rules.gameplay.alex
import agrfesta.k.cards.texasholdem.rules.gameplay.buildMutablePot
import agrfesta.k.cards.texasholdem.rules.gameplay.owns
import agrfesta.k.cards.texasholdem.rules.gameplay.poly
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName

@DisplayName("Game Observer tests")
class GameObserverTest {
    private val payments = aGamePayments()
    val table = Table(listOf(alex owns 1000, poly owns 1000), 0)

    private fun observerMock(result: CapturingSlot<GameResult>, contexts: MutableList<ViewGameContext>)
            : GameObserver {
        val observerMock = mockk<GameObserver>()
        every { observerMock.notifyStartingPhase(capture(contexts)) } just Runs
        every { observerMock.notifyWinner(capture(result)) } just Runs
        return observerMock
    }
//    private fun dealerMock(gameContext: GameContext<InGamePlayer, BoardInSequence>, collectPotBody: (Table<InGamePlayer>) -> Pot): Dealer {
//        val dealer = mockk<Dealer>()
//        every { dealer.collectPot() } returns collectPotBody.invoke(gameContext.table)
//        return dealer
//    }
    private val defaultDealer: (Table<InGamePlayer>) -> MutablePot = {
        assert(false) { "The game is not following the correct phases sequence" }
        buildMutablePot()
    }
    private val allChecksDealer: (Table<InGamePlayer>) -> MutablePot = {
        it.findPlayerBySeatName(alex.name)?.status = PlayerStatus.CALL
        it.findPlayerBySeatName(poly.name)?.status = PlayerStatus.CALL
        buildMutablePot()
    }

//    @Test
//    @DisplayName("A Game ends at pre-flop -> the observer is notified only at pre-flop and for the winner")
//    fun gameObserverStory000() {
//        val deck = DeckListImpl(cardList("Ah","Ac","3h","5s"))
//        val preFlopDealer: BuilderEnrich = {
//            it.receiveCallFrom(poly, 100)
//                    .receiveRaiseFrom(alex, 200)
//                    .receiveFoldFrom(poly)
//        }
//        val flopDealer: (Table<InGamePlayer>) -> Pot = {
//            assert(false) { "The game should finish at pre-flop but is collecting pot at flop" }
//            buildPot()
//        }
//        val dealerFactory = mockk<DealerFactory>()
//        every { dealerFactory.preFlopDealer(any(),any()) } answers
//                { dealerMockFromBuilder(firstArg(), preFlopDealer) }
//        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers { dealerMock(firstArg(), flopDealer) }
//
//        val contexts = mutableListOf<GameContext<InGamePlayer, BoardInSequence>>()
//        val result = slot<GameResult>()
//        val observerMock = observerMock(result, contexts)
//
//        buildingAGame()
//                .withPayments(payments)
//                .withTable(table)
//                .observedBy(observerMock)
//                .withDeck(deck)
//                .withDealerFactory(dealerFactory)
//                .build()
//                .play()
//
//        verify(exactly = 1) { observerMock.notifyStartingPhase(any()) }
//        verify(exactly = 1) { observerMock.notifyWinner(any()) }
//        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown
//
//        assertThat(contexts).hasSize(1)
//        assertThat(contexts[0].board).isInstanceOf(EmptyBoard::class)
//        assertThat(contexts[0].board.cards()).isEmpty()
//
//        assertThat(result.captured.winner).isEqualTo(alex)
//        assertThat(result.captured.prize).isEqualTo(300)
//    }
//
//    @Test
//    @DisplayName("A Game ends at flop -> the observer is notified only at pre-flop, flop and for the winner")
//    fun gameObserverStory001() {
//        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h"))
//        val preFlopDealer: BuilderEnrich = {
//            it.receiveCallFrom(alex, 200)
//                    .receiveRaiseFrom(poly, 200)
//        }
//        val flopDealer: BuilderEnrich = {
//            it.receiveCallFrom(poly, 200)
//                    .receiveRaiseFrom(alex, 400)
//                    .receiveFoldFrom(poly)
//        }
//        val turnDealer: (Table<InGamePlayer>) -> Pot = {
//            assert(false) { "The game should finish at flop but is collecting pot at turn" }
//            buildPot()
//        }
//        val dealerFactory = mockk<DealerFactory>()
//        every { dealerFactory.preFlopDealer(any(),any()) } answers
//                { dealerMockFromBuilder(firstArg(), preFlopDealer) }
//        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers {
//            when (secondArg<GameContext<InGamePlayer, BoardInSequence>>().board) {
//                is FlopBoard -> dealerMockFromBuilder(secondArg(), flopDealer)
//                is TurnBoard -> dealerMock(firstArg(), turnDealer)
//                else -> dealerMock(firstArg(), defaultDealer)
//            }
//        }
//
//        val contexts = mutableListOf<GameContext<InGamePlayer, BoardInSequence>>()
//        val result = slot<GameResult>()
//        val observerMock = observerMock(result, contexts)
//
//        buildingAGame()
//                .withPayments(payments)
//                .withTable(table)
//                .observedBy(observerMock)
//                .withDeck(deck)
//                .withDealerFactory(dealerFactory)
//                .build()
//                .play()
//
//        verify(exactly = 2) { observerMock.notifyStartingPhase(any()) }
//        verify(exactly = 1) { observerMock.notifyWinner(any()) }
//        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown
//
//        assertThat(contexts).hasSize(2)
//        assertThat(contexts[0].board).isInstanceOf(EmptyBoard::class)
//        assertThat(contexts[0].board.cards()).isEmpty()
//        assertThat(contexts[1].board).isInstanceOf(FlopBoard::class)
//        assertThat(contexts[1].board.cards()).containsOnly(*cards("Jh","Js","7h"))
//
//        assertThat(result.captured.winner).isEqualTo(alex)
//        assertThat(result.captured.prize).isEqualTo(1000)
//    }
//
//    @Test
//    @DisplayName("A Game ends at turn -> the observer is notified only at pre-flop, flop, turn and for the winner")
//    fun gameObserverStory002() {
//        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h", "5d"))
//        val preFlopDealer: BuilderEnrich = {
//            it.receiveCallFrom(alex, 200)
//                    .receiveCallFrom(poly, 200)
//        }
//        val turnDealer: BuilderEnrich = {
//            it.receiveCallFrom(poly, 200)
//                    .receiveRaiseFrom(alex, 500)
//                    .receiveFoldFrom(poly)
//        }
//        val riverDealer: (Table<InGamePlayer>) -> Pot = {
//            assert(false) { "The game should finish at turn but is collecting pot at river" }
//            buildPot()
//        }
//        val dealerFactory = mockk<DealerFactory>()
//        every { dealerFactory.preFlopDealer(any(),any()) } answers
//                { dealerMockFromBuilder(firstArg(), preFlopDealer) }
//        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers {
//            when (secondArg<GameContext<InGamePlayer, BoardInSequence>>().board) {
//                is FlopBoard -> dealerMock(secondArg(), allChecksDealer)
//                is TurnBoard -> dealerMockFromBuilder(secondArg(), turnDealer)
//                is RiverBoard -> dealerMock(secondArg(), riverDealer)
//                else -> dealerMock(secondArg(), defaultDealer)
//            }
//        }
//
//        val contexts = mutableListOf<GameContext<InGamePlayer, BoardInSequence>>()
//        val result = slot<GameResult>()
//        val observerMock = observerMock(result, contexts)
//
//        buildingAGame()
//                .withPayments(payments)
//                .withTable(table)
//                .observedBy(observerMock)
//                .withDeck(deck)
//                .withDealerFactory(dealerFactory)
//                .build()
//                .play()
//
//        verify(exactly = 3) { observerMock.notifyStartingPhase(any()) }
//        verify(exactly = 1) { observerMock.notifyWinner(any()) }
//        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown
//
//        assertThat(contexts).hasSize(3)
//        assertThat(contexts[0].board).isInstanceOf(EmptyBoard::class)
//        assertThat(contexts[0].board.cards()).isEmpty()
//        assertThat(contexts[1].board).isInstanceOf(FlopBoard::class)
//        assertThat(contexts[1].board.cards()).containsOnly(*cards("Jh","Js","7h"))
//        assertThat(contexts[2].board).isInstanceOf(TurnBoard::class)
//        assertThat(contexts[2].board.cards()).containsOnly(*cards("Jh","Js","7h", "5d"))
//
//        assertThat(result.captured.winner).isEqualTo(alex)
//        assertThat(result.captured.prize).isEqualTo(1100)
//    }
//
//    @Test
//    @DisplayName("A Game ends at river -> the observer is notified in all phases and for the winner")
//    fun gameObserverStory003() {
//        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h", "5d", "Td"))
//        val preFlopDealer: BuilderEnrich = {
//            it.receiveCallFrom(alex, 200)
//                    .receiveCallFrom(poly, 200)
//        }
//        val riverDealer: BuilderEnrich = {
//            it.receiveCallFrom(poly, 200)
//                    .receiveRaiseFrom(alex, 300)
//                    .receiveFoldFrom(poly)
//        }
//        val dealerFactory = mockk<DealerFactory>()
//        every { dealerFactory.preFlopDealer(any(),any()) } answers
//                { dealerMockFromBuilder(firstArg(), preFlopDealer) }
//        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers {
//            when (secondArg<GameContext<InGamePlayer, BoardInSequence>>().board) {
//                is FlopBoard -> dealerMock(secondArg(), allChecksDealer)
//                is TurnBoard -> dealerMock(secondArg(), allChecksDealer)
//                is RiverBoard -> dealerMockFromBuilder(secondArg(), riverDealer)
//                else -> dealerMock(secondArg(), defaultDealer)
//            }
//        }
//
//        val contexts = mutableListOf<GameContext<InGamePlayer, BoardInSequence>>()
//        val result = slot<GameResult>()
//        val observerMock = observerMock(result, contexts)
//
//        buildingAGame()
//                .withPayments(payments)
//                .withTable(table)
//                .observedBy(observerMock)
//                .withDeck(deck)
//                .withDealerFactory(dealerFactory)
//                .build()
//                .play()
//
//        verify(exactly = 4) { observerMock.notifyStartingPhase(any()) }
//        verify(exactly = 1) { observerMock.notifyWinner(any()) }
//        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown
//
//        assertThat(contexts).hasSize(4)
//        assertThat(contexts[0].board).isInstanceOf(EmptyBoard::class)
//        assertThat(contexts[0].board.cards()).isEmpty()
//        assertThat(contexts[1].board).isInstanceOf(FlopBoard::class)
//        assertThat(contexts[1].board.cards()).containsOnly(*cards("Jh","Js","7h"))
//        assertThat(contexts[2].board).isInstanceOf(TurnBoard::class)
//        assertThat(contexts[2].board.cards()).containsOnly(*cards("Jh","Js","7h", "5d"))
//        assertThat(contexts[3].board).isInstanceOf(RiverBoard::class)
//        assertThat(contexts[3].board.cards()).containsOnly(*cards("Jh","Js","7h", "5d", "Td"))
//
//        assertThat(result.captured.winner).isEqualTo(alex)
//        assertThat(result.captured.prize).isEqualTo(900)
//    }
//
//    @Test
//    @DisplayName("A Game ends at showdown -> the observer is notified in all phases and with result of showdown")
//    fun gameObserverStory004() {
//        val deck = DeckListImpl(cardList("Ah","Ac", "3h","5s", "Jh","Js","7h", "5d", "Td"))
//        val preFlopDealer: BuilderEnrich = {
//            it.receiveCallFrom(alex, 200)
//                    .receiveCallFrom(poly, 200)
//        }
//
//        val contexts = mutableListOf<GameContext<InGamePlayer, BoardInSequence>>()
//        val observerMock = mockk<GameObserver>()
//        every { observerMock.notifyStartingPhase(capture(contexts)) } just Runs
//        every { observerMock.notifyResult(any()) } just Runs
//
//        val dealerFactory = mockk<DealerFactory>()
//        every { dealerFactory.preFlopDealer(any(),any()) } answers
//                { dealerMockFromBuilder(firstArg(), preFlopDealer) }
//        every { dealerFactory.postFlopDealer(any(),any(),any()) } answers
//                { dealerMock(secondArg(), allChecksDealer) }
//
//        buildingAGame()
//                .withPayments(payments)
//                .withTable(table)
//                .observedBy(observerMock)
//                .withDeck(deck)
//                .withDealerFactory(dealerFactory)
//                .build()
//                .play()
//
//        verify(exactly = 4) { observerMock.notifyStartingPhase(any()) }
//        verify(exactly = 0) { observerMock.notifyWinner(any()) } // winner at showdown
//        verify(exactly = 1) { observerMock.notifyResult(any()) }
//
//        assertThat(contexts).hasSize(4)
//        assertThat(contexts[0].board).isInstanceOf(EmptyBoard::class)
//        assertThat(contexts[0].board.cards()).isEmpty()
//        assertThat(contexts[1].board).isInstanceOf(FlopBoard::class)
//        assertThat(contexts[1].board.cards()).containsOnly(*cards("Jh","Js","7h"))
//        assertThat(contexts[2].board).isInstanceOf(TurnBoard::class)
//        assertThat(contexts[2].board.cards()).containsOnly(*cards("Jh","Js","7h", "5d"))
//        assertThat(contexts[3].board).isInstanceOf(RiverBoard::class)
//        assertThat(contexts[3].board.cards()).containsOnly(*cards("Jh","Js","7h", "5d", "Td"))
//    }
}

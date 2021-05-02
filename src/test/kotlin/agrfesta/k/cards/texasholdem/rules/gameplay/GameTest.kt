package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.rules.gameplay.utils.getPlayer
import io.mockk.MockKAnswerScope
import io.mockk.clearMocks
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName

class ShowdownMock(private val showdownBody: (MutablePot, Board) -> Unit) : Showdown {
    override fun execute(pot: MutablePot, board: Board) = showdownBody.invoke(pot, board)
}

@DisplayName("Game tests")
class GameTest {
    private val payments = aGamePayments()
    private var alex = aPlayerWithName("Alex")
    private var poly = aPlayerWithName("Poly")
    private var jane = aPlayerWithName("Jane")
    private var dave = aPlayerWithName("Dave")
    private val dealerFactory = mockk<DealerFactory>()

    private val defaultDealer: (Table<InGamePlayer>) -> MutablePot = {
        assert(false) { "The game is not following the correct phases sequence" }
        buildMutablePot()
    }

    @AfterEach
    fun clear() {
        clearMocks(dealerFactory)
    }

//    @Test
//    @DisplayName("In pre-flop phase takes two cards from deck for each player at the table")
//    fun inPreFlopPhaseTakesTwoCardsFromDeckForEachPlayerAtTheTable() {
//        val deck = DeckListImpl(cardList("Ah", "Ac", "3h", "5s", "Kh", "Qc"))
//        val table = aTableWith(alex, poly, jane)
//        val preFlopDealer: (Table<InGamePlayer>) -> Pot = {
//            it.findPlayerBySeatName(alex.getSeatName())?.status = PlayerStatus.RAISE
//            it.findPlayerBySeatName(poly.getSeatName())?.status = PlayerStatus.FOLD
//            it.findPlayerBySeatName(jane.getSeatName())?.status = PlayerStatus.FOLD
//            buildPot()
//        }
//        val flopDealer: (Table<InGamePlayer>) -> Pot = {
//            assert(false) { "The game should finish at pre-flop but is collecting pot at flop" }
//            buildPot()
//        }
//        every { dealerFactory.preFlopDealer(any(),any()) }  answers {
//            assertThat(firstArg<GameContext<InGamePlayer, BoardInSequence>>().table.findPlayerBySeatName(alex.getSeatName())?.cards ?: setOf() )
//                    .containsOnly(*cards("Ah", "Ac"))
//            dealerMock(firstArg(), preFlopDealer)
//        }
//        every { dealerFactory.postFlopDealer(any(),any()) } answers { dealerMock(secondArg(), flopDealer) }
//
//        buildingATestGame(table)
//                .withDeck(deck)
//                .build()
//                .play()
//    }
//
//    @Test
//    @DisplayName("Game story: Alex is the remaining player in pre-flop and takes all the pot")
//    fun gameStory000() {
//        val table = aTableWith(alex, poly)
//        val preFlopDealer: BuilderEnrich = {
//            it.receiveCallFrom(poly, 200)
//                    .receiveRaiseFrom(alex, 500)
//                    .receiveFoldFrom(poly)
//        }
//        val flopDealer: (Table<InGamePlayer>) -> Pot = {
//            assert(false) { "The game should finish at pre-flop but is collecting pot at flop" }
//            buildPot()
//        }
//        every { dealerFactory.preFlopDealer(any(),any()) }  answers {
//            assertThat(firstArg<GameContext<InGamePlayer, BoardInSequence>>().board).isInstanceOf(EmptyBoard::class)
//            dealerMockFromBuilder(firstArg(), preFlopDealer)
//        }
//        every { dealerFactory.postFlopDealer(any(),any()) } answers { dealerMock(secondArg(), flopDealer) }
//
//        val result = buildingATestGame(table)
//                .build()
//                .play()
//
//        assertThat(result).extracting { it.player to it.stack }
//                .containsOnly(alex to 1200, poly to 800)
//    }
//
//    @Test
//    @DisplayName("Game story: Alex is the remaining player at flop and takes all the pot")
//    fun gameStory001() {
//        val table = aTableWith(alex, poly, jane)
//        val preFlopDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveCallFrom(poly, 200)
//                    .receiveFoldFrom(jane)
//        }
//
//        val flopDealer: BuilderEnrich = {
//            it.receiveCallFrom(poly, 200)
//                    .receiveRaiseFrom(alex, 400)
//                    .receiveFoldFrom(poly)
//        }
//
//        val turnDealer: (Table<InGamePlayer>) -> Pot = {
//            assert(false) { "The game should finish at flop but is collecting pot at turn" }
//            buildPot()
//        }
//        every { dealerFactory.preFlopDealer(any(),any()) } answers {
//            assertThat(firstArg<GameContext<InGamePlayer, BoardInSequence>>().board).isInstanceOf(EmptyBoard::class)
//            dealerMockFromBuilder(firstArg(), preFlopDealer)
//        }
//        every { dealerFactory.postFlopDealer(any(),any()) }  answers {
//            assertThat(firstArg<Pot>()).containsOnly(getPlayer(poly) to 200, getPlayer(alex) to 200)
//            when (secondArg<GameContext<InGamePlayer, BoardInSequence>>().board) {
//                is FlopBoard -> dealerMockFromBuilder(secondArg(), flopDealer)
//                is TurnBoard -> dealerMock(secondArg(), turnDealer)
//                else -> dealerMock(secondArg(), defaultDealer)
//            }
//        }
//
//        val result = buildingATestGame(table)
//                .build()
//                .play()
//
//        assertThat(result).extracting { it.player to it.stack }
//                .containsOnly(alex to 1400, poly to 600, jane to 1000)
//
//    }
//
//    @Test
//    @DisplayName("Game story: Alex is the remaining player at turn and takes all the pot")
//    fun gameStory002() {
//        val table = aTableWith(alex, poly, jane, dave)
//        val preFlopDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveCallFrom(poly, 200)
//                    .receiveFoldFrom(jane)
//                    .receiveCallFrom(dave, 200)
//        }
//        val flopDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveFoldFrom(poly)
//                    .receiveCallFrom(dave, 200)
//        }
//        val turnDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveFoldFrom(dave)
//        }
//        val riverDealer: (Table<InGamePlayer>) -> Pot = {
//            assert(false) { "The game should finish at turn but is collecting pot at river" }
//            buildPot()
//        }
//        every { dealerFactory.preFlopDealer(any(),any()) }  answers {
//            assertThat(firstArg<GameContext<InGamePlayer, BoardInSequence>>().board).isInstanceOf(EmptyBoard::class)
//            dealerMockFromBuilder(firstArg(), preFlopDealer)
//        }
//        every { dealerFactory.postFlopDealer(any(),any()) }  answers {
//            when (secondArg<GameContext<InGamePlayer, BoardInSequence>>().board) {
//                is FlopBoard -> {
//                    assertThat(firstArg<Pot>())
//                            .containsOnly(getPlayer(poly) to 200, getPlayer(alex) to 200,
//                                    getPlayer(dave) to 200)
//                    dealerMockFromBuilder(secondArg(), flopDealer)
//                }
//                is TurnBoard -> {
//                    assertThat(firstArg<Pot>())
//                            .containsOnly(getPlayer(poly) to 200, getPlayer(alex) to 400,
//                                    getPlayer(dave) to 400)
//                    dealerMockFromBuilder(secondArg(), turnDealer)
//                }
//                is RiverBoard -> dealerMock(firstArg(), riverDealer)
//                else -> dealerMock(secondArg(), defaultDealer)
//            }
//        }
//
//        val result = buildingATestGame(table)
//                .build()
//                .play()
//
//        assertThat(result).extracting { it.player to it.stack }
//                .containsOnly(alex to 1600,poly to 800, jane to 1000, dave to 600)
//    }
//
//    @Test
//    @DisplayName("Game story: Alex is the remaining player at river and takes all the pot")
//    fun gameStory003() {
//        val table = aTableWith(alex, poly, jane, dave)
//        val preFlopDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveCallFrom(poly, 200)
//                    .receiveFoldFrom(jane)
//                    .receiveCallFrom(dave, 200)
//        }
//        val flopDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveFoldFrom(poly)
//                    .receiveCallFrom(dave, 200)
//        }
//        val turnDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveCallFrom(dave, 200)
//        }
//        val riverDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveFoldFrom(dave)
//        }
//        every { dealerFactory.preFlopDealer(any(),any()) }  answers {
//            assertThat(firstArg<GameContext<InGamePlayer, BoardInSequence>>().board).isInstanceOf(EmptyBoard::class)
//            dealerMockFromBuilder(firstArg(), preFlopDealer)
//        }
//        every { dealerFactory.postFlopDealer(any(),any()) }  answers {
//            when (secondArg<GameContext<InGamePlayer, BoardInSequence>>().board) {
//                is FlopBoard -> {
//                    assertThat(firstArg<Pot>())
//                            .containsOnly(getPlayer(poly) to 200, getPlayer(alex) to 200,
//                                    getPlayer(dave) to 200)
//                    dealerMockFromBuilder(secondArg(), flopDealer)
//                }
//                is TurnBoard -> {
//                    assertThat(firstArg<Pot>())
//                            .containsOnly(getPlayer(poly) to 200, getPlayer(alex) to 400,
//                                    getPlayer(dave) to 400)
//                    dealerMockFromBuilder(secondArg(), turnDealer)
//                }
//                is RiverBoard -> {
//                    assertThat(firstArg<Pot>())
//                            .containsOnly(getPlayer(poly) to 200, getPlayer(alex) to 600,
//                                    getPlayer(dave) to 600)
//                    dealerMockFromBuilder(secondArg(), riverDealer)
//                }
//                else -> dealerMock(secondArg(), defaultDealer)
//            }
//        }
//
//        val result = buildingATestGame(table)
//                .build()
//                .play()
//
//        assertThat(result).extracting { it.player to it.stack }
//                .containsOnly(alex to 1800,poly to 800, jane to 1000, dave to 400)
//    }
//
//    @Test
//    @DisplayName("Game story: Alex wins the pot at showdown")
//    fun gameStory004() {
//        val table = aTableWith(alex, poly, jane, dave)
//        val preFlopDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveCallFrom(poly, 200)
//                    .receiveFoldFrom(jane)
//                    .receiveCallFrom(dave, 200)
//        }
//
//        val flopDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveFoldFrom(poly)
//                    .receiveCallFrom(dave, 200)
//        }
//
//        val turnDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveCallFrom(dave, 200)
//        }
//
//        val riverDealer: BuilderEnrich = {
//            it.receiveRaiseFrom(alex, 200)
//                    .receiveCallFrom(dave, 200)
//        }
//
//        every { dealerFactory.preFlopDealer(any(),any()) }  answers {
//            assertThat(firstArg<GameContext<InGamePlayer, BoardInSequence>>().board).isInstanceOf(EmptyBoard::class)
//            dealerMockFromBuilder(firstArg(), preFlopDealer)
//        }
//        every { dealerFactory.postFlopDealer(any(),any()) }  answers {
//            when (secondArg<GameContext<InGamePlayer, BoardInSequence>>().board) {
//                is FlopBoard -> {
//                    assertThat(firstArg<Pot>())
//                            .containsOnly(getPlayer(poly) to 200, getPlayer(alex) to 200, getPlayer(dave) to 200)
//                    dealerMockFromBuilder(secondArg(), flopDealer)
//                }
//                is TurnBoard -> {
//                    assertThat(firstArg<Pot>())
//                            .containsOnly(getPlayer(poly) to 200, getPlayer(alex) to 400, getPlayer(dave) to 400)
//                    dealerMockFromBuilder(secondArg(), turnDealer)
//                }
//                is RiverBoard -> {
//                    assertThat(firstArg<Pot>())
//                            .containsOnly(getPlayer(poly) to 200, getPlayer(alex) to 600, getPlayer(dave) to 600)
//                    dealerMockFromBuilder(secondArg(), riverDealer)
//                }
//                else -> dealerMock(secondArg(), defaultDealer)
//            }
//        }
//
//        val result = buildingATestGame(table)
//                .showdown(ShowdownMock { pot, board ->
//                    assertThat(board).isInstanceOf(RiverBoard::class)
//                    assertThat(pot).containsOnly(
//                            pot.getPlayer(poly) to 200,
//                            pot.getPlayer(alex) to 800,
//                            pot.getPlayer(dave) to 800)
//                })
//                .build()
//                .play()
//
//        assertThat(result).extracting { it.player to it.stack }
//                .containsOnly(alex to 200,poly to 800, jane to 1000, dave to 200)
//
//    }
//
//    private fun buildingATestGame(table: Table<InGamePlayer>): GameBuilder = buildingAGame()
//            .withPayments(payments)
//            .withTable(table.map { it.player owns it.stack })
//            .withDealerFactory(dealerFactory)
//
//}

//private fun dealerMock(context: GameContext<InGamePlayer, BoardInSequence>, collectPotBody: (Table<InGamePlayer>) -> Pot): Dealer {
//    val dealer = mockk<Dealer>()
//    every { dealer.collectPot() } returns collectPotBody.invoke(context.table)
//    return dealer
//}

    fun <T, B> MockKAnswerScope<T, B>.getPlayer(player: Player): InGamePlayer {
        return secondArg<GameContext<InGamePlayer, BoardInSequence>>().getPlayer(player)
    }

}

fun aTableWith(vararg players: Player): Table<InGamePlayer> {
    val inGamePlayers = players
        .map { InGamePlayer(it, 1000, aPlayerCardsSet()) }
        .toList()
    return Table(inGamePlayers, 0)
}

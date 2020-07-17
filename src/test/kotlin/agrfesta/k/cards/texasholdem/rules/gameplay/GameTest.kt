package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.DeckListImpl
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

private fun dealerMock(collectPotBody: () -> MutableMap<GamePlayer, Int>): Dealer {
    val dealer = mockk<Dealer>()
    every { dealer.collectPot() } returns collectPotBody.invoke()
    return dealer
}
class ShowdownMock(private val showdownBody: (MutableMap<GamePlayer, Int>, Board) -> Unit): Showdown {
    override fun execute(pot: MutableMap<GamePlayer, Int>, board: Board) = showdownBody.invoke(pot,board)
}

@DisplayName("Game tests")
class GameTest {
    private val payments = aGamePayments()
    private var alex = aPlayer()
    private var poly = aPlayer()
    private var jane = aPlayer()
    private var dave = aPlayer()

    private val defaultDealer: () -> MutableMap<GamePlayer, Int> = {
        assert(false) { "The game is not following the correct phases sequence" }
        buildPot()
    }

    @BeforeEach
    fun init() {
        alex = aPlayer("Alex",1000)
        poly = aPlayer("Poly",1000)
        jane = aPlayer("Jane",1000)
        dave = aPlayer("Dave",1000)
    }

    @Test
    @DisplayName("In pre-flop phase takes two cards from deck for each player at the table")
    fun inPreFlopPhaseTakesTwoCardsFromDeckForEachPlayerAtTheTable() {
        val deck = DeckListImpl(cardList("Ah","Ac","3h","5s","Kh","Qc"))
        val table = Table(listOf(alex,poly,jane), 0)
        val preFlopDealer: () -> MutableMap<GamePlayer, Int> = {
            alex.status = PlayerStatus.RAISE
            poly.status = PlayerStatus.FOLD
            jane.status = PlayerStatus.FOLD
            buildPot()
        }
        val flopDealer: () -> MutableMap<GamePlayer, Int> = {
            assert(false) { "The game should finish at pre-flop but is collecting pot at flop" }
            buildPot()
        }

        GameBuilder()
                .deck(deck)
                .preFlopDealerProvider { _,_ ->
                    assertThat(alex.cards).containsOnly(*cards("Ah","Ac"))
                    dealerMock(preFlopDealer)
                }
                .dealerProvider { _,_,_ -> dealerMock(flopDealer) }
                .build(payments,table)
                .play()
    }

    @Test
    @DisplayName("Game story: Alex is the remaining player in pre-flop and takes all the pot")
    fun gameStory000() {
        val table = Table(listOf(alex,poly), 0)
        val preFlopDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,500)
            poly.status = PlayerStatus.FOLD
            pot.receiveFrom(poly,200)
            pot
        }
        val flopDealer: () -> MutableMap<GamePlayer, Int> = {
            assert(false) { "The game should finish at pre-flop but is collecting pot at flop" }
            buildPot()
        }

        GameBuilder()
                .preFlopDealerProvider { gc,_ ->
                    assertThat(gc.table === table).isTrue()
                    assertThat(gc.payments === payments).isTrue()
                    assertThat(gc.board).isInstanceOf(EmptyBoard::class)
                    dealerMock(preFlopDealer)
                }
                .dealerProvider { _,_,_ -> dealerMock(flopDealer) }
                .build(payments,table)
            .play()

        assertThat(alex.stack).isEqualTo(1200)
        assertThat(poly.stack).isEqualTo(800)
    }

    @Test
    @DisplayName("Game story: Alex is the remaining player at flop and takes all the pot")
    fun gameStory001() {
        val table = Table(listOf(alex,poly,jane), 0)
        val preFlopDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            poly.status = PlayerStatus.CALL
            pot.receiveFrom(poly,200)
            jane.status = PlayerStatus.FOLD
            pot
        }
        val flopDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,400)
            poly.status = PlayerStatus.FOLD
            pot.receiveFrom(poly,200)
            pot
        }
        val turnDealer: () -> MutableMap<GamePlayer, Int> = {
            assert(false) { "The game should finish at flop but is collecting pot at turn" }
            buildPot()
        }

        GameBuilder()
                .preFlopDealerProvider { gc,_ ->
                    assertThat(gc.table === table).isTrue()
                    assertThat(gc.payments === payments).isTrue()
                    assertThat(gc.board).isInstanceOf(EmptyBoard::class)
                    dealerMock(preFlopDealer)
                }
                .dealerProvider { pot,context,_ ->
                    assertThat(pot).containsOnly(poly to 200, alex to 200)
                    assertThat(context.table === table).isTrue()
                    assertThat(context.payments === payments).isTrue()
                    when (context.board) {
                        is FlopBoard -> dealerMock(flopDealer)
                        is TurnBoard -> dealerMock(turnDealer)
                        else -> dealerMock(defaultDealer)
                    }
                }
                .build(payments,table)
                .play()

        assertThat(alex.stack).isEqualTo(1400)
        assertThat(poly.stack).isEqualTo(600)
        assertThat(jane.stack).isEqualTo(1000)
    }

    @Test
    @DisplayName("Game story: Alex is the remaining player at turn and takes all the pot")
    fun gameStory002() {
        val table = Table(listOf(alex,poly,jane,dave), 0)
        val preFlopDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            poly.status = PlayerStatus.CALL
            pot.receiveFrom(poly,200)
            jane.status = PlayerStatus.FOLD
            dave.status = PlayerStatus.CALL
            pot.receiveFrom(dave,200)
            pot
        }
        val flopDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            poly.status = PlayerStatus.FOLD
            dave.status = PlayerStatus.CALL
            pot.receiveFrom(dave,200)
            pot
        }
        val turnDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            dave.status = PlayerStatus.FOLD
            pot
        }
        val riverDealer: () -> MutableMap<GamePlayer, Int> = {
            assert(false) { "The game should finish at turn but is collecting pot at river" }
            buildPot()
        }

        GameBuilder()
                .preFlopDealerProvider { gc,_ ->
                    assertThat(gc.table === table).isTrue()
                    assertThat(gc.payments === payments).isTrue()
                    assertThat(gc.board).isInstanceOf(EmptyBoard::class)
                    dealerMock(preFlopDealer)
                }
                .dealerProvider { pot,context,_ ->
                    assertThat(context.table === table).isTrue()
                    assertThat(context.payments === payments).isTrue()
                    when (context.board) {
                        is FlopBoard -> {
                            assertThat(pot).containsOnly(poly to 200, alex to 200, dave to 200)
                            dealerMock(flopDealer)
                        }
                        is TurnBoard -> {
                            assertThat(pot).containsOnly(poly to 200, alex to 400, dave to 400)
                            dealerMock(turnDealer)
                        }
                        is RiverBoard -> dealerMock(riverDealer)
                        else -> dealerMock(defaultDealer)
                    }
                }
                .build(payments,table)
                .play()

        assertThat(alex.stack).isEqualTo(1600)
        assertThat(poly.stack).isEqualTo(800)
        assertThat(jane.stack).isEqualTo(1000)
        assertThat(dave.stack).isEqualTo(600)
    }

    @Test
    @DisplayName("Game story: Alex is the remaining player at river and takes all the pot")
    fun gameStory003() {
        val table = Table(listOf(alex,poly,jane,dave), 0)
        val preFlopDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            poly.status = PlayerStatus.CALL
            pot.receiveFrom(poly,200)
            jane.status = PlayerStatus.FOLD
            dave.status = PlayerStatus.CALL
            pot.receiveFrom(dave,200)
            pot
        }
        val flopDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            poly.status = PlayerStatus.FOLD
            dave.status = PlayerStatus.CALL
            pot.receiveFrom(dave,200)
            pot
        }
        val turnDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            dave.status = PlayerStatus.CALL
            pot.receiveFrom(dave,200)
            pot
        }
        val riverDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            dave.status = PlayerStatus.FOLD
            pot
        }

        GameBuilder()
                .preFlopDealerProvider { gc,_ ->
                    assertThat(gc.table === table).isTrue()
                    assertThat(gc.payments === payments).isTrue()
                    assertThat(gc.board).isInstanceOf(EmptyBoard::class)
                    dealerMock(preFlopDealer)
                }
                .dealerProvider { pot,context,_ ->
                    assertThat(context.table === table).isTrue()
                    assertThat(context.payments === payments).isTrue()
                    when (context.board) {
                        is FlopBoard -> {
                            assertThat(pot).containsOnly(poly to 200, alex to 200, dave to 200)
                            dealerMock(flopDealer)
                        }
                        is TurnBoard -> {
                            assertThat(pot).containsOnly(poly to 200, alex to 400, dave to 400)
                            dealerMock(turnDealer)
                        }
                        is RiverBoard -> {
                            assertThat(pot).containsOnly(poly to 200, alex to 600, dave to 600)
                            dealerMock(riverDealer)
                        }
                        else -> dealerMock(defaultDealer)
                    }
                }
                .build(payments,table)
                .play()

        assertThat(alex.stack).isEqualTo(1800)
        assertThat(poly.stack).isEqualTo(800)
        assertThat(jane.stack).isEqualTo(1000)
        assertThat(dave.stack).isEqualTo(400)
    }

    @Test
    @DisplayName("Game story: Alex wins the pot at showdown")
    fun gameStory004() {
        val table = Table(listOf(alex,poly,jane,dave), 0)
        val preFlopDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            poly.status = PlayerStatus.CALL
            pot.receiveFrom(poly,200)
            jane.status = PlayerStatus.FOLD
            dave.status = PlayerStatus.CALL
            pot.receiveFrom(dave,200)
            pot
        }
        val flopDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            poly.status = PlayerStatus.FOLD
            dave.status = PlayerStatus.CALL
            pot.receiveFrom(dave,200)
            pot
        }
        val turnDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            dave.status = PlayerStatus.CALL
            pot.receiveFrom(dave,200)
            pot
        }
        val riverDealer: () -> MutableMap<GamePlayer, Int> = {
            val pot = buildPot()
            alex.status = PlayerStatus.RAISE
            pot.receiveFrom(alex,200)
            dave.status = PlayerStatus.CALL
            pot.receiveFrom(dave,200)
            pot
        }

        GameBuilder()
                .preFlopDealerProvider { gc,_ ->
                    assertThat(gc.table === table).isTrue()
                    assertThat(gc.payments === payments).isTrue()
                    assertThat(gc.board).isInstanceOf(EmptyBoard::class)
                    dealerMock(preFlopDealer)
                }
                .dealerProvider { pot,context,_ ->
                    assertThat(context.table === table).isTrue()
                    assertThat(context.payments === payments).isTrue()
                    when (context.board) {
                        is FlopBoard -> {
                            assertThat(pot).containsOnly(poly to 200, alex to 200, dave to 200)
                            dealerMock(flopDealer)
                        }
                        is TurnBoard -> {
                            assertThat(pot).containsOnly(poly to 200, alex to 400, dave to 400)
                            dealerMock(turnDealer)
                        }
                        is RiverBoard -> {
                            assertThat(pot).containsOnly(poly to 200, alex to 600, dave to 600)
                            dealerMock(riverDealer)
                        }
                        else -> dealerMock(defaultDealer)
                    }
                }
                .showdown(ShowdownMock {
                    pot, board ->
                    assertThat(board).isInstanceOf(RiverBoard::class)
                    assertThat(pot).containsOnly(poly to 200, alex to 800, dave to 800)
                })
                .build(payments,table)
                .play()

        assertThat(alex.stack).isEqualTo(200)
        assertThat(poly.stack).isEqualTo(800)
        assertThat(jane.stack).isEqualTo(1000)
        assertThat(dave.stack).isEqualTo(200)
    }

}
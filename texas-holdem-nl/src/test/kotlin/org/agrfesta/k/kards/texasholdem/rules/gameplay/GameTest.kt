package org.agrfesta.k.kards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.containsOnly
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.agrfesta.k.kards.texasholdem.observers.GameObserver
import org.agrfesta.k.kards.texasholdem.observers.GameResult
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.PRE_FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.RIVER
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.TURN
import org.agrfesta.k.kards.texasholdem.testing.mothers.CircularStrategy.Companion.aStrategy
import org.agrfesta.k.kards.texasholdem.testing.mothers.aGamePayments
import org.agrfesta.k.kards.texasholdem.testing.mothers.aSittingPlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.blinds
import org.agrfesta.k.kards.texasholdem.testing.mothers.folder
import org.agrfesta.k.kards.texasholdem.testing.mothers.limper
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.aSittingPlayerTable
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.aTenSittingPlayerTable
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.bigB
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.button
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.smallB
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

@DisplayName("Game tests")
class GameTest {

    @Test
    @DisplayName("play(): at Pre-Flop, everyone fold -> game ends at Pre-Flop, Big Blind wins the pot")
    fun playTest000() {
        val observerMock: GameObserver = mockk(relaxed = true)
        val table = aTenSittingPlayerTable {
            button with { aSittingPlayer(it, stack = 100u, strategy = folder) }
            smallB with { aSittingPlayer(it, stack = 100u, strategy = folder) }
            bigB with { aSittingPlayer(it, stack = 100u, strategy = folder) }
        }
        val game = GameImpl( payments = blinds(5u, 10u), table = table, observer = observerMock )
        val contexts: MutableList<GameContextImpl> = mutableListOf()
        every { observerMock.notifyStartingPhase(capture(contexts)) } just Runs
        val result: CapturingSlot<GameResult> = slot()
        every { observerMock.notifyWinner(capture(result)) } just Runs

        val players = game.play()

        assertThat(contexts).extracting { it.board.phase }
            .containsOnly(PRE_FLOP)
        val capturedObservedResult = result.captured
        assertThat(capturedObservedResult.prize).isEqualTo(15u)
        assertThat(capturedObservedResult.winner).isEqualTo(bigB)
        assertThat(capturedObservedResult.players).extracting({it}, {it.stack})
            .containsAll(button to 100u, smallB to 95u, bigB to 105u)
        assertThat(players).extracting({it}, {it.stack})
            .containsAll(button to 100u, smallB to 95u, bigB to 105u)
    }

    @Test
    @DisplayName("play(): at Pre-Flop, everyone fold, no observer -> " +
            "game ends at Pre-Flop, Big Blind wins the pot, no observer notification")
    fun playTest005() {
        val table = aTenSittingPlayerTable {
            button with { aSittingPlayer(it, stack = 100u, strategy = folder) }
            smallB with { aSittingPlayer(it, stack = 100u, strategy = folder) }
            bigB with { aSittingPlayer(it, stack = 100u, strategy = folder) }
        }
        val game = GameImpl( payments = blinds(5u, 10u), table = table )

        val players = game.play()

        assertThat(players).extracting({it}, {it.stack})
            .containsAll(button to 100u, smallB to 95u, bigB to 105u)
    }

    @Test
    @DisplayName("""play(): at Pre-Flop, everyone call, at Flop everyone fold -> 
            game ends at Flop, Button wins the pot""")
    fun playTest001() {
        val observerMock: GameObserver = mockk(relaxed = true)
        val table = aTenSittingPlayerTable {
            button with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), fold())) }
            smallB with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), fold())) }
            bigB with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), fold())) }
        }
        val game = GameImpl( payments = blinds(5u, 10u), table = table, observer = observerMock )
        val contexts: MutableList<GameContextImpl> = mutableListOf()
        every { observerMock.notifyStartingPhase(capture(contexts)) } just Runs
        val result: CapturingSlot<GameResult> = slot()
        every { observerMock.notifyWinner(capture(result)) } just Runs

        val players = game.play()

        assertThat(contexts).extracting { it.board.phase }
            .containsOnly(PRE_FLOP, FLOP)
        val capturedObservedResult = result.captured
        assertThat(capturedObservedResult.prize).isEqualTo(30u)
        assertThat(capturedObservedResult.winner).isEqualTo(button)
        assertThat(capturedObservedResult.players).extracting({it}, {it.stack})
            .containsAll(button to 120u, smallB to 90u, bigB to 90u)
        assertThat(players).extracting({it}, {it.stack})
            .containsAll(button to 120u, smallB to 90u, bigB to 90u)
    }

    @Test
    @DisplayName("""play(): at Pre-Flop, everyone call, at Flop everyone fold, no observer -> 
            game ends at Flop, Button wins the pot, no observer notified""")
    fun playTest006() {
        val table = aTenSittingPlayerTable {
            button with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), fold())) }
            smallB with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), fold())) }
            bigB with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), fold())) }
        }
        val game = GameImpl( payments = blinds(5u, 10u), table = table )

        val players = game.play()

        assertThat(players).extracting({it}, {it.stack})
            .containsAll(button to 120u, smallB to 90u, bigB to 90u)
    }

    @Test
    @DisplayName("""play(): at Pre-Flop, everyone call, at Flop everyone call, at Turn everyone fold -> 
            game ends at Turn, Button wins the pot""")
    fun playTest002() {
        val observerMock: GameObserver = mockk(relaxed = true)
        val table = aTenSittingPlayerTable {
            button with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), call(), fold())) }
            smallB with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), call(), fold())) }
            bigB with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), call(), fold())) }
        }
        val game = GameImpl( payments = blinds(5u, 10u), table = table, observer = observerMock )
        val contexts: MutableList<GameContextImpl> = mutableListOf()
        every { observerMock.notifyStartingPhase(capture(contexts)) } just Runs
        val result: CapturingSlot<GameResult> = slot()
        every { observerMock.notifyWinner(capture(result)) } just Runs

        val players = game.play()

        assertThat(contexts).extracting { it.board.phase }
            .containsOnly(PRE_FLOP, FLOP, TURN)
        val capturedObservedResult = result.captured
        assertThat(capturedObservedResult.prize).isEqualTo(30u)
        assertThat(capturedObservedResult.winner).isEqualTo(button)
        assertThat(capturedObservedResult.players).extracting({it}, {it.stack})
            .containsAll(button to 120u, smallB to 90u, bigB to 90u)
        assertThat(players).extracting({it}, {it.stack})
            .containsAll(button to 120u, smallB to 90u, bigB to 90u)
    }

    @Test
    @DisplayName("""play(): at Pre-Flop, everyone call, at Flop everyone call, at Turn everyone call, 
        at River everyone fold -> game ends at River, Button wins the pot""")
    fun playTest003() {
        val observerMock: GameObserver = mockk(relaxed = true)
        val table = aTenSittingPlayerTable {
            button with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), call(), call(), fold())) }
            smallB with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), call(), call(), fold())) }
            bigB with { aSittingPlayer(it, stack = 100u, strategy = aStrategy(call(), call(), call(), fold())) }
        }
        val game = GameImpl( payments = blinds(5u, 10u), table = table, observer = observerMock )
        val contexts: MutableList<GameContextImpl> = mutableListOf()
        every { observerMock.notifyStartingPhase(capture(contexts)) } just Runs
        val result: CapturingSlot<GameResult> = slot()
        every { observerMock.notifyWinner(capture(result)) } just Runs

        val players = game.play()

        assertThat(contexts).extracting { it.board.phase }
            .containsOnly(PRE_FLOP, FLOP, TURN, RIVER)
        val capturedObservedResult = result.captured
        assertThat(capturedObservedResult.prize).isEqualTo(30u)
        assertThat(capturedObservedResult.winner).isEqualTo(button)
        assertThat(capturedObservedResult.players).extracting({it}, {it.stack})
            .containsAll(button to 120u, smallB to 90u, bigB to 90u)
        assertThat(players).extracting({it}, {it.stack})
            .containsAll(button to 120u, smallB to 90u, bigB to 90u)
    }

    @Test
    @DisplayName("""play(): at every phase everyone call -> game ends at Showdown, Button wins the pot""")
    fun playTest004() {
        val observerMock: GameObserver = mockk(relaxed = true)
        val table = aTenSittingPlayerTable {
            button with { aSittingPlayer(it, stack = 100u, strategy = limper) }
            smallB with { aSittingPlayer(it, stack = 100u, strategy = limper) }
            bigB with { aSittingPlayer(it, stack = 100u, strategy = limper) }
        }
        val showdownMock: Showdown = mockk()
        val game = GameImpl( payments = blinds(5u, 10u), table = table, observer = observerMock,
            config = GameConfig(createShowdown = {showdownMock}) )
        val contexts: MutableList<GameContextImpl> = mutableListOf()
        every { observerMock.notifyStartingPhase(capture(contexts)) } just Runs
        val showdownInitialPot: CapturingSlot<MutablePot> = slot()
        val showdownBoard: CapturingSlot<Board> = slot()
        every { showdownMock.execute(capture(showdownInitialPot), capture(showdownBoard), any()) } just Runs

        val players = game.play()

        verify(exactly = 0) { observerMock.notifyWinner(any()) }
        assertThat(contexts).extracting { it.board.phase }
            .containsOnly(PRE_FLOP, FLOP, TURN, RIVER)
        val capturedPot = showdownInitialPot.captured
        assertThat(capturedPot.amount()).isEqualTo(30u)
        assertThat(capturedPot.keys)
            .containsAll(button, smallB, bigB)
        assertThat(showdownBoard.captured.phase).isEqualTo(RIVER)
        assertThat(players).extracting({it}, {it.stack})
            .containsAll(button to 90u, smallB to 90u, bigB to 90u)
    }

    @Test
    @DisplayName("""constructor(): creating a Game with a specific UUID -> instantiated Game has that UUID""")
    fun constructor_creatingAGameWithASpecificUUID_instantiatedGameHasThatUUID() {
        val uuid = UUID.randomUUID()

        val game = GameImpl( uuid = uuid, payments = aGamePayments(), table = aSittingPlayerTable() )

        assertThat(game.uuid).isEqualTo(uuid)
    }

    @Test
    @DisplayName("""constructor(): creating a Game without an UUID, providing a specific generator -> 
        |instantiated Game has the UUID from the provider""")
    fun constructor_creatingAGameWithoutAnUUID_instantiatedGameHasTheUUIDFromTheProvider() {
        val uuid = UUID.randomUUID()

        val game = GameImpl( payments = aGamePayments(), table = aSittingPlayerTable(),
            config = GameConfig(uuidProvider = { uuid }))

        assertThat(game.uuid).isEqualTo(uuid)
    }

    @Test
    @DisplayName("""constructor(): creating a Game with a specific UUID and providing a specific generator -> 
        |instantiated Game has that UUID ignoring the one from the provider""")
    fun constructor_creatingAGameWithASpecificUUIDAndProvidingASpecificGenerator_instantiatedGameHasThatUUID() {
        val uuidA = UUID.randomUUID()
        val uuidB = UUID.randomUUID()

        val game = GameImpl( uuid = uuidA, payments = aGamePayments(), table = aSittingPlayerTable(),
            config = GameConfig(uuidProvider = { uuidB }))

        assertThat(game.uuid).isEqualTo(uuidA)
    }

}

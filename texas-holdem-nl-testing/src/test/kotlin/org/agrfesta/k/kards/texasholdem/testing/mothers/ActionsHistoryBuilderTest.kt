package org.agrfesta.k.kards.texasholdem.testing.mothers

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.key
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.PRE_FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.RIVER
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.TURN
import org.agrfesta.k.kards.texasholdem.rules.gameplay.call
import org.agrfesta.k.kards.texasholdem.rules.gameplay.does
import org.agrfesta.k.kards.texasholdem.rules.gameplay.fold
import org.agrfesta.k.kards.texasholdem.rules.gameplay.raise
import org.agrfesta.k.kards.texasholdem.testing.mothers.ActionsHistoryBuilder.Companion.history
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.button
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.smallB
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.utg
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ActionsHistoryBuilderTest {

    @Test
    @DisplayName("atPreFlop(): adding three actions to the pre-flop -> at pre-flop the history has three actions")
    fun atPreFlop_addingThreeActionsToThePreFlop_atPreFlopTheHistoryHasThreeActions() {
        val history = history {
            atPreFlop(
                utg does call(),
                button does fold(),
                smallB does raise(200u))
        }

        assertThat(history).key(PRE_FLOP)
            .containsExactly(
                utg does call(),
                button does fold(),
                smallB does raise(200u))
    }
    @Test
    @DisplayName("""atPreFlop(): adding two actions to the pre-flop and than one -> 
        |at pre-flop the history has only one action""")
    fun atPreFlop_addingTwoActionsToThePreFlopAndThanOne_atPreFlopTheHistoryHasOnlyOneAction() {
        val history = history {
            atPreFlop(
                utg does call(),
                button does fold())
            atPreFlop(
                smallB does raise(200u))
        }

        assertThat(history).key(PRE_FLOP)
            .containsExactly(smallB does raise(200u))
    }

    @Test
    @DisplayName("atFlop(): adding three actions to the flop -> at flop the history has three actions")
    fun atFlop_addingThreeActionsToTheFlop_atFlopTheHistoryHasThreeActions() {
        val history = history {
            atFlop(
                utg does call(),
                button does fold(),
                smallB does raise(200u))
        }

        assertThat(history).key(FLOP)
            .containsExactly(
                utg does call(),
                button does fold(),
                smallB does raise(200u))
    }
    @Test
    @DisplayName("""atFlop(): adding two actions to the flop and than one -> 
        |at flop the history has only one action""")
    fun atFlop_addingTwoActionsToTheFlopAndThanOne_atFlopTheHistoryHasOnlyOneAction() {
        val history = history {
            atFlop(
                utg does call(),
                button does fold())
            atFlop(
                smallB does raise(200u))
        }

        assertThat(history).key(FLOP)
            .containsExactly(smallB does raise(200u))
    }

    @Test
    @DisplayName("atTurn(): adding three actions to the turn -> at turn the history has three actions")
    fun atTurn_addingThreeActionsToTheTurn_atTurnTheHistoryHasThreeActions() {
        val history = history {
            atTurn(
                utg does call(),
                button does fold(),
                smallB does raise(200u))
        }

        assertThat(history).key(TURN)
            .containsExactly(
                utg does call(),
                button does fold(),
                smallB does raise(200u))
    }
    @Test
    @DisplayName("""atTurn(): adding two actions to the turn and than one -> 
        |at turn the history has only one action""")
    fun atTurn_addingTwoActionsToTheTurnAndThanOne_atTurnTheHistoryHasOnlyOneAction() {
        val history = history {
            atTurn(
                utg does call(),
                button does fold())
            atTurn(
                smallB does raise(200u))
        }

        assertThat(history).key(TURN)
            .containsExactly(smallB does raise(200u))
    }

    @Test
    @DisplayName("atRiver(): adding three actions to the river -> at river the history has three actions")
    fun atRiver_addingThreeActionsToTheRiver_atRiverTheHistoryHasThreeActions() {
        val history = history {
            atRiver(
                utg does call(),
                button does fold(),
                smallB does raise(200u))
        }

        assertThat(history).key(RIVER)
            .containsExactly(
                utg does call(),
                button does fold(),
                smallB does raise(200u))
    }
    @Test
    @DisplayName("""atRiver(): adding two actions to the river and than one -> 
        |at river the history has only one action""")
    fun atRiver_addingTwoActionsToTheRiverAndThanOne_atRiverTheHistoryHasOnlyOneAction() {
        val history = history {
            atRiver(
                utg does call(),
                button does fold())
            atRiver(
                smallB does raise(200u))
        }

        assertThat(history).key(RIVER)
            .containsExactly(smallB does raise(200u))
    }

}

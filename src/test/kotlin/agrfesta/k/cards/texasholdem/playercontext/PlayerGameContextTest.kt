package agrfesta.k.cards.texasholdem.playercontext

import agrfesta.k.cards.texasholdem.rules.gameplay.BoardInfo
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase.FLOP
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase.PRE_FLOP
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase.RIVER
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase.TURN
import agrfesta.k.cards.texasholdem.rules.gameplay.aPlayerCardsSet
import agrfesta.k.cards.texasholdem.rules.gameplay.alex
import agrfesta.k.cards.texasholdem.rules.gameplay.call
import agrfesta.k.cards.texasholdem.rules.gameplay.dave
import agrfesta.k.cards.texasholdem.rules.gameplay.fold
import agrfesta.k.cards.texasholdem.rules.gameplay.jane
import agrfesta.k.cards.texasholdem.rules.gameplay.poly
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("PlayerGameContext tests")
class PlayerGameContextTest {

    @Test
    @DisplayName("PlayerAction.toString(): player alex fold -> returns 'Alex FOLD'")
    fun testPlayerActionToString() {
        assertThat( (alex does fold()).toString() ).isEqualTo("Alex Fold")
    }

    @Test
    @DisplayName("getActions(): history is empty -> returns an empty list")
    fun historyIsEmptyReturnsAnEmptyList() {
        val context = buildingAPlayerGameContext()
                .withHistory(emptyMap()).build()
        assertThat( context.getActions(PRE_FLOP) ).isEmpty()
        assertThat( context.getActions(FLOP) ).isEmpty()
        assertThat( context.getActions(TURN) ).isEmpty()
        assertThat( context.getActions(RIVER) ).isEmpty()
    }
    @Test
    @DisplayName("getActions(): has history on pre-flop only -> returns a not empty list for pre-flop only")
    fun hasHistoryOnPreFlopOnlyReturnsANotEmptyListForPreFlopOnly() {
        val context = buildingAPlayerGameContext()
                .withPreFlopHistoryWhere( alex does fold(), dave does fold() )
                .build()
        assertThat( context.getActions(PRE_FLOP) )
                .containsExactly( alex does fold(), dave does fold() )
        assertThat( context.getActions(FLOP) ).isEmpty()
        assertThat( context.getActions(TURN) ).isEmpty()
        assertThat( context.getActions(RIVER) ).isEmpty()
    }

    @Test
    @DisplayName("getActualActions(): game is at flop -> returns history of the flop")
    fun gameIsAtFlopReturnsHistoryOfTheFlop() {
        val boardInfo = BoardInfo(aPlayerCardsSet(), FLOP)
        val context = buildingAPlayerGameContext()
                .withBoard(boardInfo)
                .withPreFlopHistoryWhere( alex does call(), dave does fold(), poly does fold(), jane does fold() )
                .withFlopHistoryWhere( alex does call(), dave does fold() )
                .build()

        assertThat( context.getActions(PRE_FLOP) )
                .containsExactly( alex does call(), dave does fold(), poly does fold(), jane does fold() )
        assertThat( context.getActions(FLOP) )
                .containsExactly( alex does call(), dave does fold() )
        assertThat( context.getActions(TURN) ).isEmpty()
        assertThat( context.getActions(RIVER) ).isEmpty()

        assertThat( context.getActualActions() )
                .containsExactly( alex does call(), dave does fold() )
    }

}

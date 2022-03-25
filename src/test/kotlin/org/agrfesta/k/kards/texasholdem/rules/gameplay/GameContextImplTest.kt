package org.agrfesta.k.kards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.PRE_FLOP
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GameContextImplTest {

    @Test
    @DisplayName("getGlobalPot(): getting global pot when there is none -> returns empty pot")
    fun getGlobalPot_gettingGlobalPotWhenThereIsNone_returnsEmptyPot() {
        val gameContext = aGameContextImplWith(phasePots = emptyMap())

        val result = gameContext.getGlobalPot()

        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("getGlobalPot(): getting global pot when there is an empty one only -> returns empty pot")
    fun getGlobalPot_gettingGlobalPotWhenThereIsAnEmptyOneOnly_returnsEmptyPot() {
        val gameContext = aGameContextImplWith(phasePots = mapOf(
            PRE_FLOP to emptyMap()
        ))

        val result = gameContext.getGlobalPot()

        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("getGlobalPot(): getting global pot when there is one -> returns that pot")
    fun getGlobalPot_gettingGlobalPotWhenThereIsOne_returnsThatPot() {
        val gameContext = aGameContextImplWith(phasePots = mapOf(
            PRE_FLOP to mapOf(alex to 50u, poly to 77u)
        ))

        val result = gameContext.getGlobalPot()

        assertThat(result).containsOnly(alex to 50u, poly to 77u)
    }

    @Test
    @DisplayName("""getGlobalPot(): getting global pot when there are more than one -> returns pot accumulating all 
        |entries""")
    fun getGlobalPot_gettingGlobalPotWhenThereAreMoreThanOne_returnsPotAccumulatingAllEntries() {
        val gameContext = aGameContextImplWith(phasePots = mapOf(
            PRE_FLOP to mapOf(alex to 50u, poly to 77u),
            FLOP to mapOf(alex to 50u, jane to 1u)
        ))

        val result = gameContext.getGlobalPot()

        assertThat(result).containsOnly(alex to 100u, poly to 77u, jane to 1u)
    }

    @Test
    @DisplayName("getPhaseHistory(): getting phase history when there is none -> returns empty list")
    fun getPhaseHistory_gettingPhaseHistoryWhenThereIsNone_returnsEmptyList() {
        val gameContext = aGameContextImplWith(
            board = aBoardAt(PRE_FLOP),
            history = emptyMap())

        val result = gameContext.getPhaseHistory()

        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("getPhaseHistory(): getting phase history when is empty -> returns empty list")
    fun getPhaseHistory_gettingPhaseHistoryWhenIsEmpty_returnsEmptyList() {
        val gameContext = aGameContextImplWith(
            board = aBoardAt(FLOP),
            history = mapOf(FLOP to listOf()))

        val result = gameContext.getPhaseHistory()

        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("getPhaseHistory(): getting phase history when is the empty one -> returns empty list")
    fun getPhaseHistory_gettingPhaseHistoryWhenIsTheEmptyOne_returnsEmptyList() {
        val actionList = listOf(alex does fold())
        val gameContext = aGameContextImplWith(
            board = aBoardAt(FLOP),
            history = mapOf(PRE_FLOP to actionList, FLOP to listOf()))

        val result = gameContext.getPhaseHistory()

        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("getPhaseHistory(): getting phase history when there was action -> returns phase's list")
    fun getPhaseHistory_gettingPhaseHistoryWhenThereWasAction_returnsPhasesList() {
        val actionList = listOf(alex does fold())
        val gameContext = aGameContextImplWith(
            board = aBoardAt(FLOP),
            history = mapOf(PRE_FLOP to listOf(), FLOP to actionList))

        val result = gameContext.getPhaseHistory()

        assertThat(result).isEqualTo(actionList)
    }

}

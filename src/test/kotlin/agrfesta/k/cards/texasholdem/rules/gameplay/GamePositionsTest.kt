package agrfesta.k.cards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Game Positions tests")
class GamePositionsTest {

    @Test
    @DisplayName("2 players, button=0 -> SB=0, BB=1, UTG=0")
    fun ifPlayersAre2AndButtonIs0SB0BB1UTG0() {
        assertThat(getSBPosition(0, 2)).isEqualTo(0)
        assertThat(getBBPosition(0, 2)).isEqualTo(1)
        assertThat(getUTGPosition(0, 2)).isEqualTo(0)
    }
    @Test
    @DisplayName("2 players, button=1 -> SB=1, BB=0, UTG=1")
    fun ifPlayersAre2AndButtonIs1SB1BB0UTG1() {
        assertThat(getSBPosition(1, 2)).isEqualTo(1)
        assertThat(getBBPosition(1, 2)).isEqualTo(0)
        assertThat(getUTGPosition(1, 2)).isEqualTo(1)
    }
    @Test
    @DisplayName("2 players, button=5 -> SB=1, BB=0, UTG=1")
    fun ifPlayersAre2AndButtonIs5SB1BB0UTG1() {
        assertThat(getSBPosition(5, 2)).isEqualTo(1)
        assertThat(getBBPosition(5, 2)).isEqualTo(0)
        assertThat(getUTGPosition(5, 2)).isEqualTo(1)
    }
    @Test
    @DisplayName("2 players, button=-6 -> SB=0, BB=1, UTG=0")
    fun ifPlayersAre2AndButtonIsMinus6SB0BB1UTG0() {
        assertThat(getSBPosition(-6, 2)).isEqualTo(0)
        assertThat(getBBPosition(-6, 2)).isEqualTo(1)
        assertThat(getUTGPosition(-6, 2)).isEqualTo(0)
    }

    @Test
    @DisplayName("3 players, button=0 -> SB=1, BB=2, UTG=0")
    fun ifPlayersAre3AndButtonIs0SB1BB2UTG0() {
        assertThat(getSBPosition(0, 3)).isEqualTo(1)
        assertThat(getBBPosition(0, 3)).isEqualTo(2)
        assertThat(getUTGPosition(0, 3)).isEqualTo(0)
    }
    @Test
    @DisplayName("3 players, button=1 -> SB=2, BB=0, UTG=1")
    fun ifPlayersAre3AndButtonIs1SB2BB0UTG1() {
        assertThat(getSBPosition(1, 3)).isEqualTo(2)
        assertThat(getBBPosition(1, 3)).isEqualTo(0)
        assertThat(getUTGPosition(1, 3)).isEqualTo(1)
    }
    @Test
    @DisplayName("3 players, button=2 -> SB=0, BB=1, UTG=2")
    fun ifPlayersAre3AndButtonIs2SB0BB1UTG2() {
        assertThat(getSBPosition(2, 3)).isEqualTo(0)
        assertThat(getBBPosition(2, 3)).isEqualTo(1)
        assertThat(getUTGPosition(2, 3)).isEqualTo(2)
    }
    @Test
    @DisplayName("3 players, button=3 -> SB=1, BB=2, UTG=0")
    fun ifPlayersAre3AndButtonIs3SB1BB2UTG0() {
        assertThat(getSBPosition(3, 3)).isEqualTo(1)
        assertThat(getBBPosition(3, 3)).isEqualTo(2)
        assertThat(getUTGPosition(3, 3)).isEqualTo(0)
    }
    @Test
    @DisplayName("3 players, button=7 -> SB=2, BB=0, UTG=1")
    fun ifPlayersAre3AndButtonIs7SB2BB0UTG1() {
        assertThat(getSBPosition(7, 3)).isEqualTo(2)
        assertThat(getBBPosition(7, 3)).isEqualTo(0)
        assertThat(getUTGPosition(7, 3)).isEqualTo(1)
    }
    @Test
    @DisplayName("3 players, button=-1 -> SB=0, BB=1, UTG=2")
    fun ifPlayersAre3AndButtonIsMinus1SB0BB1UTG2() {
        assertThat(getSBPosition(-1, 3)).isEqualTo(0)
        assertThat(getBBPosition(-1, 3)).isEqualTo(1)
        assertThat(getUTGPosition(-1, 3)).isEqualTo(2)
    }
    @Test
    @DisplayName("3 players, button=-5 -> SB=2, BB=0, UTG=1")
    fun ifPlayersAre3AndButtonIsMinus5SB2BB0UTG1() {
        assertThat(getSBPosition(-5, 3)).isEqualTo(2)
        assertThat(getBBPosition(-5, 3)).isEqualTo(0)
        assertThat(getUTGPosition(-5, 3)).isEqualTo(1)
    }

}
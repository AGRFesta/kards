package org.agrfesta.k.kards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Table tests")
class TableTest {

    @Test
    @DisplayName("creating a Table with one player -> raises an Exception")
    fun creatingATableWithOnePlayerRaisesAnException() {
        val failure = assertThat {
            buildTable<Player> {
                withButtonInPosition(0)
                withPlayers(alex)
            }
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum number of players for a Table is 2, actual number: 1")
    }
    @Test
    @DisplayName("creating a Table with no players -> raises an Exception")
    fun creatingATableWithNoPlayerRaisesAnException() {
        val failure = assertThat {
            buildTable<Player> {
                withButtonInPosition(0)
            }
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum number of players for a Table is 2, actual number: 0")
    }

    @Test
    @DisplayName("2 players table, button=0 -> SB=0, BB=1, UTG=0")
    fun twoPlayersTableWithButton0HasSBIn0AndBBIn1AndUTGIn0() {
        val table = buildTable<Player> {
            withButtonInPosition(0)
            withPlayers(alex,juno)
        }
        assertThat(alex).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(juno).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(alex)
        assertThat(table.iterateFromUTG().next()).isEqualTo(alex)
    }
    @Test
    @DisplayName("2 players table, button=1 -> SB=1, BB=0, UTG=1")
    fun twoPlayersTableWithButton1HasSBIn1AndBBIn0AndUTGIn1() {
        val table = buildTable<Player> {
            withButtonInPosition(1)
            withPlayers(alex,juno)
        }
        assertThat(juno).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(juno)
        assertThat(table.iterateFromUTG().next()).isEqualTo(juno)
    }
    @Test
    @DisplayName("2 players table, button=5 -> SB=1, BB=0, UTG=1")
    fun twoPlayersTableWithButton5HasSBIn1AndBBIn0AndUTGIn1() {
        val table = buildTable<Player> {
            withButtonInPosition(5)
            withPlayers(alex,juno)
        }
        assertThat(juno).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(juno)
        assertThat(table.iterateFromUTG().next()).isEqualTo(juno)
    }
    @Test
    @DisplayName("2 players table, button=-6 -> SB=0, BB=1, UTG=0")
    fun twoPlayersTableWithButtonMinus6HasSBIn0AndBBIn1AndUTGIn0() {
        val table = buildTable<Player> {
            withButtonInPosition(-6)
            withPlayers(alex,juno)
        }
        assertThat(alex).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(juno).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(alex)
        assertThat(table.iterateFromUTG().next()).isEqualTo(alex)
    }

    @Test
    @DisplayName("3 players table, button=0 -> SB=1, BB=2, UTG=0")
    fun threePlayersTableWithButton0HasSBIn1AndBBIn2AndUTGIn0() {
        val table = buildTable<Player> {
            withButtonInPosition(0)
            withPlayers(alex, juno, maya)
        }
        assertThat(juno).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(maya).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(juno)
        assertThat(table.iterateFromUTG().next()).isEqualTo(alex)
    }
    @Test
    @DisplayName("3 players table, button=1 -> SB=2, BB=0, UTG=1")
    fun threePlayersTableWithButton1HasSBIn2AndBBIn0AndUTGIn1() {
        val table = buildTable<Player> {
            withButtonInPosition(1)
            withPlayers(alex, juno, maya)
        }
        assertThat(maya).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(maya)
        assertThat(table.iterateFromUTG().next()).isEqualTo(juno)
    }
    @Test
    @DisplayName("3 players table, button=2 -> SB=0, BB=1, UTG=2")
    fun threePlayersTableWithButton2HasSBIn0AndBBIn1AndUTGIn2() {
        val table = buildTable<Player> {
            withButtonInPosition(2)
            withPlayers(alex, juno, maya)
        }
        assertThat(alex).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(juno).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(alex)
        assertThat(table.iterateFromUTG().next()).isEqualTo(maya)
    }
    @Test
    @DisplayName("3 players table, button=3 -> SB=1, BB=2, UTG=0")
    fun threePlayersTableWithButton3HasSBIn1AndBBIn2AndUTGIn0() {
        val table = buildTable<Player> {
            withButtonInPosition(3)
            withPlayers(alex, juno, maya)
        }
        assertThat(juno).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(maya).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(juno)
        assertThat(table.iterateFromUTG().next()).isEqualTo(alex)
    }
    @Test
    @DisplayName("3 players table, button=7 -> SB=2, BB=0, UTG=1")
    fun threePlayersTableWithButton7HasSBIn2AndBBIn0AndUTGIn1() {
        val table = buildTable<Player> {
            withButtonInPosition(7)
            withPlayers(alex, juno, maya)
        }
        assertThat(maya).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(maya)
        assertThat(table.iterateFromUTG().next()).isEqualTo(juno)
    }
    @Test
    @DisplayName("3 players table, button=-1 -> SB=0, BB=1, UTG=2")
    fun threePlayersTableWithButtonMinus1HasSBIn0AndBBIn1AndUTGIn2() {
        val table = buildTable<Player> {
            withButtonInPosition(-1)
            withPlayers(alex, juno, maya)
        }
        assertThat(alex).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(juno).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(alex)
        assertThat(table.iterateFromUTG().next()).isEqualTo(maya)
    }
    @Test
    @DisplayName("3 players table, button=-5 -> SB=2, BB=0, UTG=1")
    fun threePlayersTableWithButtonMinus5HasSBIn2AndBBIn0AndUTGIn1() {
        val table = buildTable<Player> {
            withButtonInPosition(-5)
            withPlayers(alex, juno, maya)
        }
        assertThat(maya).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(maya)
        assertThat(table.iterateFromUTG().next()).isEqualTo(juno)
    }

    @Test
    @DisplayName("TableIterator allow to iterate in a cyclic way trough next()")
    fun tableIteratorAllowToIterateInACyclicWayTroughNextMethod() {
        val table = buildTable<Player> {
            withButtonInPosition(0)
            withPlayers(alex, juno, maya)
        }
        val iterator = table.iterateFromSB()
        assertThat(iterator.next()).isEqualTo(juno)
        assertThat(iterator.next()).isEqualTo(maya)
        assertThat(iterator.next()).isEqualTo(alex)
        assertThat(iterator.next()).isEqualTo(juno)
        assertThat(iterator.next()).isEqualTo(maya)
        assertThat(iterator.next()).isEqualTo(alex)
    }

    @Test
    @DisplayName("findPlayerBySeatName: looking for a player not at table -> returns null")
    fun lookingForAPlayerNotAtTableReturnsNull() {
        val table = buildTable<Player> {
            withPlayers(juno, maya)
        }
        assertThat(table.findPlayerBySeatName("Alex")).isNull()
    }
    @Test
    @DisplayName("findPlayerBySeatName: looking for a player at table -> returns the player")
    fun lookingForAPlayerAtTableReturnsThePlayer() {
        val table = buildTable<Player> {
            withPlayers(juno, maya)
        }
        assertThat(table.findPlayerBySeatName("Maya")).isEqualTo(maya)
    }

    @Test
    @DisplayName("position(): table with [alex, poly], alex position -> returns 0")
    fun position_twoPlayersTableFirstPosition_returns0() {
        val table = buildTable<Player> {
            withPlayers(alex, poly)
        }
        assertThat(table.position(alex)).isEqualTo(0)
    }
    @Test
    @DisplayName("position(): table with [alex, poly], juno position -> throws an exception")
    fun position_missingPlayer_throwsAnException() {
        val table = buildTable<Player> {
            withPlayers(alex, poly)
        }

        assertThat { assertThat(table.position(juno)) }
                .isFailure()
                .hasClass(NoSuchElementException::class)
    }

}

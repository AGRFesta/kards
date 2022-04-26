package org.agrfesta.k.kards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import org.agrfesta.k.kards.texasholdem.utils.DistinctList.Companion.distinctListOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Table tests")
class TableTest {

    @Test
    @DisplayName("creating a Table with one player -> raises an Exception")
    fun creatingATableWithOnePlayerRaisesAnException() {
        val failure = assertThat {
            TableImpl( players = distinctListOf(alex) )
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum number of players for a Table is 2, actual number: 1")
    }
    @Test
    @DisplayName("creating a Table with no players -> raises an Exception")
    fun creatingATableWithNoPlayerRaisesAnException() {
        val failure = assertThat {
            TableImpl( players = distinctListOf() )
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum number of players for a Table is 2, actual number: 0")
    }

    @Test
    @DisplayName("2 players table, button=0 -> SB=0, BB=1, UTG=0")
    fun twoPlayersTableWithButton0HasSBIn0AndBBIn1AndUTGIn0() {
        val table = TableImpl( players = distinctListOf(alex, juno), button = 0u)
        assertThat(alex).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(juno).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFrom(Position.SMALL_BLIND).next()).isEqualTo(alex)
        assertThat(table.iterateFrom(Position.UNDER_THE_GUN).next()).isEqualTo(alex)
    }
    @Test
    @DisplayName("2 players table, button=1 -> SB=1, BB=0, UTG=1")
    fun twoPlayersTableWithButton1HasSBIn1AndBBIn0AndUTGIn1() {
        val table = TableImpl( players = distinctListOf(alex, juno), button = 1u)
        assertThat(juno).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFrom(Position.SMALL_BLIND).next()).isEqualTo(juno)
        assertThat(table.iterateFrom(Position.UNDER_THE_GUN).next()).isEqualTo(juno)
    }
    @Test
    @DisplayName("2 players table, button=5 -> SB=1, BB=0, UTG=1")
    fun twoPlayersTableWithButton5HasSBIn1AndBBIn0AndUTGIn1() {
        val table = TableImpl( players = distinctListOf(alex, juno), button = 5u)
        assertThat(juno).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFrom(Position.SMALL_BLIND).next()).isEqualTo(juno)
        assertThat(table.iterateFrom(Position.UNDER_THE_GUN).next()).isEqualTo(juno)
    }

    @Test
    @DisplayName("3 players table, button=0 -> SB=1, BB=2, UTG=0")
    fun threePlayersTableWithButton0HasSBIn1AndBBIn2AndUTGIn0() {
        val table = TableImpl( players = distinctListOf(alex, juno, maya), button = 0u)
        assertThat(juno).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(maya).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFrom(Position.SMALL_BLIND).next()).isEqualTo(juno)
        assertThat(table.iterateFrom(Position.UNDER_THE_GUN).next()).isEqualTo(alex)
    }
    @Test
    @DisplayName("3 players table, button=1 -> SB=2, BB=0, UTG=1")
    fun threePlayersTableWithButton1HasSBIn2AndBBIn0AndUTGIn1() {
        val table = TableImpl( players = distinctListOf(alex, juno, maya), button = 1u)
        assertThat(maya).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFrom(Position.SMALL_BLIND).next()).isEqualTo(maya)
        assertThat(table.iterateFrom(Position.UNDER_THE_GUN).next()).isEqualTo(juno)
    }
    @Test
    @DisplayName("3 players table, button=2 -> SB=0, BB=1, UTG=2")
    fun threePlayersTableWithButton2HasSBIn0AndBBIn1AndUTGIn2() {
        val table = TableImpl( players = distinctListOf(alex, juno, maya), button = 2u)
        assertThat(alex).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(juno).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFrom(Position.SMALL_BLIND).next()).isEqualTo(alex)
        assertThat(table.iterateFrom(Position.UNDER_THE_GUN).next()).isEqualTo(maya)
    }
    @Test
    @DisplayName("3 players table, button=3 -> SB=1, BB=2, UTG=0")
    fun threePlayersTableWithButton3HasSBIn1AndBBIn2AndUTGIn0() {
        val table = TableImpl( players = distinctListOf(alex, juno, maya), button = 3u)
        assertThat(juno).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(maya).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFrom(Position.SMALL_BLIND).next()).isEqualTo(juno)
        assertThat(table.iterateFrom(Position.UNDER_THE_GUN).next()).isEqualTo(alex)
    }
    @Test
    @DisplayName("3 players table, button=7 -> SB=2, BB=0, UTG=1")
    fun threePlayersTableWithButton7HasSBIn2AndBBIn0AndUTGIn1() {
        val table = TableImpl( players = distinctListOf(alex, juno, maya), button = 7u)
        assertThat(maya).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFrom(Position.SMALL_BLIND).next()).isEqualTo(maya)
        assertThat(table.iterateFrom(Position.UNDER_THE_GUN).next()).isEqualTo(juno)
    }

    @Test
    @DisplayName("TableIterator allow to iterate in a cyclic way trough next()")
    fun tableIteratorAllowToIterateInACyclicWayTroughNextMethod() {
        val table = TableImpl( players = distinctListOf(alex, juno, maya), button = 0u)
        val iterator = table.iterateFrom(Position.SMALL_BLIND)
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
        val table = TableImpl( players = distinctListOf(juno, maya) )
        assertThat(table.findPlayerByName("Alex")).isNull()
    }
    @Test
    @DisplayName("findPlayerBySeatName: looking for a player at table -> returns the player")
    fun lookingForAPlayerAtTableReturnsThePlayer() {
        val table = TableImpl( players = distinctListOf(juno, maya) )
        assertThat(table.findPlayerByName("Maya")).isEqualTo(maya)
    }

    @Test
    @DisplayName("position(): table with [alex, poly], alex position -> returns 0")
    fun position_twoPlayersTableFirstPosition_returns0() {
        val table = TableImpl( players = distinctListOf(alex, poly) )

        assertThat(table.position(alex)).isEqualTo(0u)
    }
    @Test
    @DisplayName("position(): table with [alex, poly], juno position -> throws an exception")
    fun position_missingPlayer_throwsAnException() {
        val table = TableImpl( players = distinctListOf(alex, poly) )

        val failure = assertThat {
            table.position(juno)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Player Juno is not sitting at the table")
    }

}

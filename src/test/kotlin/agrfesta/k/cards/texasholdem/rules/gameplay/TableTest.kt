package agrfesta.k.cards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Table tests")
class TableTest {
    private val alex = aPlayer("Alex")
    private val juno = aPlayer("Juno")
    private val maya = aPlayer("Maya")

    @Test
    @DisplayName("creating a Table with one player -> raises an Exception")
    fun creatingATableWithOnePlayerRaisesAnException() {
        val failure = assertThat {
            Table(listOf(alex),0)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum number of players for a Table is 2, actual number: 1")
    }
    @Test
    @DisplayName("creating a Table with no players -> raises an Exception")
    fun creatingATableWithNoPlayerRaisesAnException() {
        val failure = assertThat {
            Table(listOf(),0)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum number of players for a Table is 2, actual number: 0")
    }

    @Test
    @DisplayName("2 players table, button=0 -> SB=0, BB=1, UTG=0")
    fun twoPlayersTableWithButton0HasSBIn0AndBBIn1AndUTGIn0() {
        val table = Table(listOf(alex,juno),0)
        assertThat(alex.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(juno.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(alex)
        assertThat(table.iterateFromUTG().next()).isEqualTo(alex)
    }
    @Test
    @DisplayName("2 players table, button=1 -> SB=1, BB=0, UTG=1")
    fun twoPlayersTableWithButton1HasSBIn1AndBBIn0AndUTGIn1() {
        val table = Table(listOf(alex,juno),1)
        assertThat(juno.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(juno)
        assertThat(table.iterateFromUTG().next()).isEqualTo(juno)
    }
    @Test
    @DisplayName("2 players table, button=5 -> SB=1, BB=0, UTG=1")
    fun twoPlayersTableWithButton5HasSBIn1AndBBIn0AndUTGIn1() {
        val table = Table(listOf(alex,juno),5)
        assertThat(juno.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(juno)
        assertThat(table.iterateFromUTG().next()).isEqualTo(juno)
    }
    @Test
    @DisplayName("2 players table, button=-6 -> SB=0, BB=1, UTG=0")
    fun twoPlayersTableWithButtonMinus6HasSBIn0AndBBIn1AndUTGIn0() {
        val table = Table(listOf(alex,juno),-6)
        assertThat(alex.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(juno.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(alex)
        assertThat(table.iterateFromUTG().next()).isEqualTo(alex)
    }

    @Test
    @DisplayName("3 players table, button=0 -> SB=1, BB=2, UTG=0")
    fun threePlayersTableWithButton0HasSBIn1AndBBIn2AndUTGIn0() {
        val table = Table(listOf(alex,juno,maya),0)
        assertThat(juno.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(maya.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(juno)
        assertThat(table.iterateFromUTG().next()).isEqualTo(alex)
    }
    @Test
    @DisplayName("3 players table, button=1 -> SB=2, BB=0, UTG=1")
    fun threePlayersTableWithButton1HasSBIn2AndBBIn0AndUTGIn1() {
        val table = Table(listOf(alex,juno,maya),1)
        assertThat(maya.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(maya)
        assertThat(table.iterateFromUTG().next()).isEqualTo(juno)
    }
    @Test
    @DisplayName("3 players table, button=2 -> SB=0, BB=1, UTG=2")
    fun threePlayersTableWithButton2HasSBIn0AndBBIn1AndUTGIn2() {
        val table = Table(listOf(alex,juno,maya),2)
        assertThat(alex.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(juno.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(alex)
        assertThat(table.iterateFromUTG().next()).isEqualTo(maya)
    }
    @Test
    @DisplayName("3 players table, button=3 -> SB=1, BB=2, UTG=0")
    fun threePlayersTableWithButton3HasSBIn1AndBBIn2AndUTGIn0() {
        val table = Table(listOf(alex,juno,maya),3)
        assertThat(juno.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(maya.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(juno)
        assertThat(table.iterateFromUTG().next()).isEqualTo(alex)
    }
    @Test
    @DisplayName("3 players table, button=7 -> SB=2, BB=0, UTG=1")
    fun threePlayersTableWithButton7HasSBIn2AndBBIn0AndUTGIn1() {
        val table = Table(listOf(alex,juno,maya),7)
        assertThat(maya.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(maya)
        assertThat(table.iterateFromUTG().next()).isEqualTo(juno)
    }
    @Test
    @DisplayName("3 players table, button=-1 -> SB=0, BB=1, UTG=2")
    fun threePlayersTableWithButtonMinus1HasSBIn0AndBBIn1AndUTGIn2() {
        val table = Table(listOf(alex,juno,maya),-1)
        assertThat(alex.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(juno.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(alex)
        assertThat(table.iterateFromUTG().next()).isEqualTo(maya)
    }
    @Test
    @DisplayName("3 players table, button=-5 -> SB=2, BB=0, UTG=1")
    fun threePlayersTableWithButtonMinus5HasSBIn2AndBBIn0AndUTGIn1() {
        val table = Table(listOf(alex,juno,maya),-5)
        assertThat(maya.player).isSittingOn(table, Position.SMALL_BLIND)
        assertThat(alex.player).isSittingOn(table, Position.BIG_BLIND)
        assertThat(table.iterateFromSB().next()).isEqualTo(maya)
        assertThat(table.iterateFromUTG().next()).isEqualTo(juno)
    }

    @Test
    @DisplayName("TableIterator allow to iterate in a cyclic way trough next()")
    fun tableIteratorAllowToIterateInACyclicWayTroughNextMethod() {
        val table = Table(listOf(alex,juno,maya),0)
        val iterator = table.iterateFromSB()
        assertThat(iterator.next()).isEqualTo(juno)
        assertThat(iterator.next()).isEqualTo(maya)
        assertThat(iterator.next()).isEqualTo(alex)
        assertThat(iterator.next()).isEqualTo(juno)
        assertThat(iterator.next()).isEqualTo(maya)
        assertThat(iterator.next()).isEqualTo(alex)
    }

}
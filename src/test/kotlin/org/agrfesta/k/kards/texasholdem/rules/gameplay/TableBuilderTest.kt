package org.agrfesta.k.kards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("TableBuilder tests")
class TableBuilderTest {

    @Test
    @DisplayName("withPlayers(): add a player that was already added -> raises an exception")
    fun addAPlayerThatWasAlreadyAddedRaisesAnException() {
        val failure = assertThat {
            buildTable<Player> {
                withPlayers(jane, alex)
                withPlayers(alex, juno)
            }
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Player 'Alex' is already sitting at the table!")
    }
    @Test
    @DisplayName("withPlayers(): add players multiple times -> table contains all players added")
    fun addPlayersMultipleTimesTableContainsAllPlayersAdded() {
        val table = buildTable<Player> {
            withPlayers(jane, poly)
            withPlayers(alex, juno)
        }

        assertThat(table.players).containsOnly(alex, jane, juno, poly)
    }

    @Test
    @DisplayName("withButtonInPosition(): assign button position -> table has same button position")
    fun assignButtonPositionTableHasSameButtonPosition() {
        val table = buildTable<Player> {
            withButtonInPosition(2)
            withPlayers(jane, poly, alex)
        }

        assertThat(table.button).isEqualTo(2)
    }
    @Test
    @DisplayName("withButtonInPosition(): button position not assigned -> table has button position on 0")
    fun buttonPositionNotAssignedTableHasButtonPositionOn0() {
        val table = buildTable<Player> {
            withPlayers(jane, poly, alex)
        }

        assertThat(table.button).isEqualTo(0)
    }

}

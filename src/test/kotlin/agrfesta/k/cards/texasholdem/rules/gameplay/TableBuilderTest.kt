package agrfesta.k.cards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("TableBuilder tests")
class TableBuilderTest {

    @Test
    @DisplayName("withPlayers(): add a player that was already added -> raises an exception")
    fun addAPlayerThatWasAlreadyAddedRaisesAnException() {
        val failure = assertThat {
            buildingTable<Player>()
                    .withPlayers(jane, alex)
                    .withPlayers(alex, juno)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Player 'Alex' is already sitting at the table!")
    }
    @Test
    @DisplayName("withPlayers(): add players multiple times -> table contains all players added")
    fun addPlayersMultipleTimesTableContainsAllPlayersAdded() {
        val table = buildingTable<Player>()
                .withPlayers(jane, poly)
                .withPlayers(alex, juno)
                .build()

        assertThat(table.players).containsOnly(alex, jane, juno, poly)
    }

    @Test
    @DisplayName("withButtonInPosition(): assign button position -> table has same button position")
    fun assignButtonPositionTableHasSameButtonPosition() {
        val table = buildingTable<Player>()
                .withButtonInPosition(2)
                .withPlayers(jane, poly, alex)
                .build()

        assertThat(table.button).isEqualTo(2)
    }
    @Test
    @DisplayName("withButtonInPosition(): button position not assigned -> table has button position on 0")
    fun buttonPositionNotAssignedTableHasButtonPositionOn0() {
        val table = buildingTable<Player>()
                .withPlayers(jane, poly, alex)
                .build()

        assertThat(table.button).isEqualTo(0)
    }

}

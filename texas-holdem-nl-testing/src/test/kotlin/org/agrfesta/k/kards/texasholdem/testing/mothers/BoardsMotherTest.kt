package org.agrfesta.k.kards.texasholdem.testing.mothers

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.PRE_FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.RIVER
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.TURN
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BoardsMotherTest {

    @Test
    @DisplayName("board(): empty list of cards -> returns a Pre-Flop board")
    fun board_emptyListOfCards_returnsAPreFlopBoard() {
        val board = board()

        assertThat(board.phase).isEqualTo(PRE_FLOP)
        assertThat(board.cards).isEmpty()
    }

    @Test
    @DisplayName("board(): three cards list -> returns a Flop board")
    fun board_threeCardsList_returnsAFlopBoard() {
        val board = board("7s", "Kd", "Jh")

        assertThat(board.phase).isEqualTo(FLOP)
        assertThat(board.cards).containsOnly(card("Jh"), card("7s"), card("Kd"))
    }

    @Test
    @DisplayName("board(): four cards list -> returns a Turn board")
    fun board_fourCardsList_returnsATurnBoard() {
        val board = board("7s", "Kd", "Jh",  "8c")

        assertThat(board.phase).isEqualTo(TURN)
        assertThat(board.cards).containsOnly(card("Jh"), card("7s"), card("8c"), card("Kd"))
    }

    @Test
    @DisplayName("board(): five cards list -> returns a River board")
    fun board_fiveCardsList_returnsARiverBoard() {
        val board = board("7s", "Kd", "Jh",  "8c", "Qd")

        assertThat(board.phase).isEqualTo(RIVER)
        assertThat(board.cards).containsOnly(card("Jh"), card("7s"), card("8c"), card("Kd"), card("Qd"))
    }

    @Test
    @DisplayName("board(): two cards list -> returns an exception")
    fun board_twoCardsList_returnsAnException() {
        val failure = assertThat {
            board("7s", "Kd")
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to get phase from invalid board, cards:[7♠, K♢]")
    }

}
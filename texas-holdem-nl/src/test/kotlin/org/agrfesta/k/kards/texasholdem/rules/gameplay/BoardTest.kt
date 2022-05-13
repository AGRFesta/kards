package org.agrfesta.k.kards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEmpty
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import org.agrfesta.k.kards.texasholdem.testing.mothers.aDeck
import org.agrfesta.k.kards.texasholdem.testing.mothers.cardList
import org.agrfesta.k.kards.texasholdem.testing.mothers.cards
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Board tests")
class BoardTest {

    @Test
    @DisplayName("EmptyBoard cards -> []")
    fun emptyBoardHasNoCards() {
        val deck = aDeck(cardList("Ad","2s","Ts","2c","Jh","5h"))
        assertThat(EmptyBoard(deck).cards).isEmpty()
    }
    @Test
    @DisplayName("EmptyBoard next -> FlopBoard")
    fun emptyBoardNextReturnsFlopBoard() {
        val deck = aDeck(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val nextBoard = EmptyBoard(deck).next()
        assertThat(nextBoard).isInstanceOf(FlopBoard::class)
    }

    @Test
    @DisplayName("FlopBoard cards -> first three cards drawn from deck")
    fun flopBoardReturnsFirstThreeCardsDrawnFromDeck() {
        val deck = aDeck(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val board = FlopBoard(deck)
        assertThat(board.cards).containsOnly(*cards("Ad","2s","Ts"))
    }
    @Test
    @DisplayName("FlopBoard next -> TurnBoard")
    fun flopBoardNextReturnsTurnBoard() {
        val deck = aDeck(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val turn = FlopBoard(deck).next()
        assertThat(turn).isInstanceOf(TurnBoard::class)
    }

    @Test
    @DisplayName("TurnBoard cards -> flop cards + first card drawn from deck")
    fun turnBoardReturnsFlopCardsPlusFirstCardDrawnFromDeck() {
        val deck = aDeck(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val turn = FlopBoard(deck).next()
        assertThat(turn).isInstanceOf(TurnBoard::class)
        assertThat(turn.cards).containsOnly(*cards("Ad","2s","Ts","2c"))
    }
    @Test
    @DisplayName("TurnBoard next -> RiverBoard")
    fun turnBoardNextReturnsRiverBoard() {
        val deck = aDeck(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val river = FlopBoard(deck).next().next()
        assertThat(river).isInstanceOf(RiverBoard::class)
    }

    @Test
    @DisplayName("RiverBoard cards -> flop cards + turn + first card drawn from deck")
    fun riverBoardReturnsFlopCarsPlusTurnPlusFirstCardDrawnFromDeck() {
        val deck = aDeck(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val river = FlopBoard(deck).next().next()
        assertThat(river).isInstanceOf(RiverBoard::class)
        assertThat(river.cards).containsOnly(*cards("Ad","2s","Ts","2c","Jh"))
    }
    @Test
    @DisplayName("TurnBoard next -> RiverBoard")
    fun riverBoardNextReturnsItself() {
        val deck = aDeck(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val river = FlopBoard(deck).next().next()
        assertThat(river.next() === river).isTrue()
    }
}

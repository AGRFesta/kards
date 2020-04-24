package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.DeckListImpl
import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.suits.createFrenchCard
import agrfesta.kcards.playingcards.suits.createFrenchHand
import agrfesta.kcards.playingcards.suits.frenchCardsSet
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Board tests")
class BoardTest {
    private fun cards(vararg strings: String): Array<Card> = frenchCardsSet(*strings).toTypedArray()
    private fun card(string: String): Card = createFrenchCard(string)
    private fun cardList(vararg strings: String): List<Card> = createFrenchHand(*strings)

    @Test
    @DisplayName("EmptyBoard cards -> []")
    fun emptyBoardHasNoCards() {
        val deck = DeckListImpl(cardList("Ad","2s","Ts","2c","Jh","5h"))
        assertThat(EmptyBoard(deck).cards()).isEmpty()
    }
    @Test
    @DisplayName("EmptyBoard next -> FlopBoard")
    fun emptyBoardNextReturnsFlopBoard() {
        val deck = DeckListImpl(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val nextBoard = EmptyBoard(deck).next()
        assertThat(nextBoard).isInstanceOf(FlopBoard::class)
    }

    @Test
    @DisplayName("FlopBoard cards -> first three cards drawn from deck")
    fun flopBoardReturnsFirstThreeCardsDrawnFromDeck() {
        val deck = DeckListImpl(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val board = FlopBoard(deck)
        assertThat(board.flop).containsOnly(*cards("Ad","2s","Ts"))
        assertThat(board.cards()).containsOnly(*cards("Ad","2s","Ts"))
    }
    @Test
    @DisplayName("FlopBoard next -> TurnBoard")
    fun flopBoardNextReturnsTurnBoard() {
        val deck = DeckListImpl(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val turn = FlopBoard(deck).next()
        assertThat(turn).isInstanceOf(TurnBoard::class)
    }

    @Test
    @DisplayName("TurnBoard cards -> flop cards + first card drawn from deck")
    fun turnBoardReturnsFlopCardsPlusFirstCardDrawnFromDeck() {
        val deck = DeckListImpl(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val turn = FlopBoard(deck).next()
        assertThat(turn).isInstanceOf(TurnBoard::class)
        if (turn is TurnBoard) {
            assertThat(turn.flop()).containsOnly(*cards("Ad","2s","Ts"))
            assertThat(turn.turn).isEqualTo(card("2c"))
        }
        assertThat(turn.cards()).containsOnly(*cards("Ad","2s","Ts","2c"))
    }
    @Test
    @DisplayName("TurnBoard next -> RiverBoard")
    fun turnBoardNextReturnsRiverBoard() {
        val deck = DeckListImpl(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val river = FlopBoard(deck).next().next()
        assertThat(river).isInstanceOf(RiverBoard::class)
    }

    @Test
    @DisplayName("RiverBoard cards -> flop cards + turn + first card drawn from deck")
    fun riverBoardReturnsFlopCarsPlusTurnPlusFirstCardDrawnFromDeck() {
        val deck = DeckListImpl(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val river = FlopBoard(deck).next().next()
        assertThat(river).isInstanceOf(RiverBoard::class)
        if (river is RiverBoard) {
            assertThat(river.flop()).containsOnly(*cards("Ad","2s","Ts"))
            assertThat(river.turn()).isEqualTo(card("2c"))
            assertThat(river.river).isEqualTo(card("Jh"))
        }
        assertThat(river.cards()).containsOnly(*cards("Ad","2s","Ts","2c","Jh"))
    }
    @Test
    @DisplayName("TurnBoard next -> RiverBoard")
    fun riverBoardNextReturnsItself() {
        val deck = DeckListImpl(cardList("Ad","2s","Ts","2c","Jh","5h"))
        val river = FlopBoard(deck).next().next()
        assertThat(river.next() === river).isTrue()
    }
}
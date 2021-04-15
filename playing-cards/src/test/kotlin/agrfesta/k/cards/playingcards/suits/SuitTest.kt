package agrfesta.k.cards.playingcards.suits

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import agrfesta.k.cards.playingcards.suits.Suit.*

@DisplayName("Suits Tests")
class SuitTest {

    @Test
    @DisplayName("French suit creates a full deck")
    fun frenchSuitCreatesFullDeck() {
        assertIsFullFrenchDeck(FRENCH.createDeck())
    }
    @Test
    @DisplayName("French suit cards set is complete")
    fun frenchSuitCardsSetIsComplete() {
        assertIsFullFrenchCardList(FRENCH.cards)
    }

    @Test
    @DisplayName("Italian suit creates a full deck")
    fun italianSuitCreatesFullDeck() {
        assertIsFullItalianDeck(ITALIAN.createDeck())
    }
    @Test
    @DisplayName("Italian suit cards set is complete")
    fun italianSuitCardsSetIsComplete() {
        assertIsFullItalianCardList(ITALIAN.cards)
    }

}

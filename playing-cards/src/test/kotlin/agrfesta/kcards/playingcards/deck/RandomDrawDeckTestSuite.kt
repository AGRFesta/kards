package agrfesta.kcards.playingcards.deck

import org.junit.jupiter.api.DisplayName

@DisplayName("Random Draw Deck tests")
class RandomDrawDeckTestSuite : DeckTestSuite {
    override fun testingDeck() = RandomDrawDeck()
}
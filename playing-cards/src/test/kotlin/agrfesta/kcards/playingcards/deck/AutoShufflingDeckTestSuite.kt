package agrfesta.kcards.playingcards.deck

import org.junit.jupiter.api.DisplayName

@DisplayName("Auto Shuffling Deck tests")
class AutoShufflingDeckTestSuite : DeckTestSuite {
    override fun testingDeck() = AutoShufflingDeck(ReverseStackShufflingService())
}
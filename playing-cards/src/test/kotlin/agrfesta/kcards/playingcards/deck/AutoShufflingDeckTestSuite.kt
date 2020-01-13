package agrfesta.kcards.playingcards.deck

class AutoShufflingDeckTestSuite : DeckTestSuite {
    override fun testingDeck() = AutoShufflingDeck(ReverseStackShufflingService())
}
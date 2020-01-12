package agrfesta.kcards.playingcards.deck

class AutoShufflingDeckTestSuite : DeckTestSuite {
    override fun deckUnderTest() = AutoShufflingDeck(ReverseStackShufflingService())
}
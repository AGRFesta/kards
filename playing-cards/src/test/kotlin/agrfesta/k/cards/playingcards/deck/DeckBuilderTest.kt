package agrfesta.k.cards.playingcards.deck

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.cardOf
import agrfesta.k.cards.playingcards.suits.Suit.FRENCH
import agrfesta.k.cards.playingcards.suits.assertIsFullFrenchDeck
import agrfesta.k.cards.playingcards.utils.ShufflingService
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isTrue
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Deck Fluent Builder Tests")
class DeckBuilderTest {
    private val defaultShuffler = mockk<ShufflingService>(relaxed = true)
    private val providedShuffler = mockk<ShufflingService>(relaxed = true)

    private val cardA = cardOf(rankOf('A'), aSeed())
    private val cardB = cardOf(rankOf('B'), aSeed())
    private val cardC = cardOf(rankOf('C'), aSeed())
    private val cards: Collection<Card> = listOf(cardA,cardB,cardC)

    @Test
    @DisplayName("Building setting card collection, no shuffler provided -> use the default")
    fun test000() {
        DeckBuilder(defaultShuffler)
                .withCards(cards)
                .build()
        verify(exactly = 1) { defaultShuffler.shuffle(any()) }
        verify(exactly = 0) { providedShuffler.shuffle(any()) }
    }
    @Test
    @DisplayName("Building setting card vararg, no shuffler provided -> use the default")
    fun test001() {
        DeckBuilder(defaultShuffler)
                .withCards(cardA,cardB,cardC)
                .build()
        verify(exactly = 1) { defaultShuffler.shuffle(any()) }
        verify(exactly = 0) { providedShuffler.shuffle(any()) }
    }
    @Test
    @DisplayName("Building setting card collection, the provided shuffling service is used building the deck")
    fun test002() {
        DeckBuilder(defaultShuffler)
                .shuffleWith(providedShuffler)
                .withCards(cards)
                .build()
        verify(exactly = 1) { providedShuffler.shuffle(any()) }
        verify(exactly = 0) { defaultShuffler.shuffle(any()) }
    }
    @Test
    @DisplayName("Building setting card vararg, the provided shuffling service is used building the deck")
    fun test003() {
        DeckBuilder(defaultShuffler)
                .shuffleWith(providedShuffler)
                .withCards(cardA,cardB,cardC)
                .build()
        verify(exactly = 1) { providedShuffler.shuffle(any()) }
        verify(exactly = 0) { defaultShuffler.shuffle(any()) }
    }

    @Test
    @DisplayName("Build without setting cards -> empty deck")
    fun test004() {
        val deck = buildDeck {}
        assertThat(deck.isEmpty()).isTrue()
    }
    @Test
    @DisplayName("Build setting empty cards collection -> empty deck")
    fun test007() {
        val deck = buildDeck { withCards(listOf()) }
        assertThat(deck.isEmpty()).isTrue()
    }
    @Test
    @DisplayName("Build setting empty cards array -> empty deck")
    fun test008() {
        val deck = buildDeck { withCards() }
        assertThat(deck.isEmpty()).isTrue()
    }
    @Test
    @DisplayName("Build setting two card -> deck containing only those two cards")
    fun test005() {
        val deck = buildDeck { withCards(cardA, cardB) }
        assertThat(deckContent(deck)).containsOnly(cardB,cardA)
    }
    @Test
    @DisplayName("Build setting collection of cards -> deck containing only the cards from the collection")
    fun test006() {
        val deck = buildDeck { withCards(cards) }
        assertThat(deckContent(deck)).containsOnly(cardB,cardA,cardC)
    }

    @Test
    @DisplayName("Setting cards from Collection overrides previous cards setting")
    fun test009() {
        val collection = listOf(cardB,cardC)
        val deck = buildDeck {
            withCards(cardA)
            withCards(collection)
        }
        assertThat(deckContent(deck)).containsOnly(cardB,cardC)
    }
    @Test
    @DisplayName("Setting cards from Array overrides previous cards setting")
    fun test010() {
        val deck = buildDeck {
            withCards(cardA)
            withCards(cardB,cardC)
        }
        assertThat(deckContent(deck)).containsOnly(cardB,cardC)
    }
    @Test
    @DisplayName("Setting cards from card suit overrides previous cards setting")
    fun test011() {
        val deck = buildDeck {
            withCards(cardA)
            withCards(cardB,cardC)
            withCardsFromSuit(FRENCH)
        }
        assertIsFullFrenchDeck(deck)
    }
}

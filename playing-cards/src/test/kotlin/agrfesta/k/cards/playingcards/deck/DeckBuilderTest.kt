package agrfesta.k.cards.playingcards.deck

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.cardOf
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
    @DisplayName("Building from card collection, no shuffler provided -> use the default")
    fun test000() {
        DeckBuilder(defaultShuffler).build(cards)
        verify(exactly = 1) { defaultShuffler.shuffle(any()) }
        verify(exactly = 0) { providedShuffler.shuffle(any()) }
    }
    @Test
    @DisplayName("Building from card vararg, no shuffler provided -> use the default")
    fun test001() {
        DeckBuilder(defaultShuffler).build(cardA,cardB,cardC)
        verify(exactly = 1) { defaultShuffler.shuffle(any()) }
        verify(exactly = 0) { providedShuffler.shuffle(any()) }
    }
    @Test
    @DisplayName("Building from card collection, the provided shuffling service is used building the deck")
    fun test002() {
        DeckBuilder(defaultShuffler)
                .shuffleWith(providedShuffler)
                .build(cards)
        verify(exactly = 1) { providedShuffler.shuffle(any()) }
        verify(exactly = 0) { defaultShuffler.shuffle(any()) }
    }
    @Test
    @DisplayName("Building from card vararg, the provided shuffling service is used building the deck")
    fun test003() {
        DeckBuilder(defaultShuffler)
                .shuffleWith(providedShuffler)
                .build(cardA,cardB,cardC)
        verify(exactly = 1) { providedShuffler.shuffle(any()) }
        verify(exactly = 0) { defaultShuffler.shuffle(any()) }
    }

    @Test
    @DisplayName("Build from no params -> empty deck")
    fun test004() {
        val deck = DeckBuilder(defaultShuffler).build()
        assertThat(deck.isEmpty()).isTrue()
    }
    @Test
    @DisplayName("Build from empty collection -> empty deck")
    fun test007() {
        val deck = DeckBuilder(defaultShuffler).build(listOf())
        assertThat(deck.isEmpty()).isTrue()
    }
    @Test
    @DisplayName("Build from two card -> deck containing only those two cards")
    fun test005() {
        val deck = DeckBuilder(defaultShuffler).build(cardA,cardB)
        assertThat(deckContent(deck)).containsOnly(cardB,cardA)
    }
    @Test
    @DisplayName("Build from collection of cards -> deck containing only the cards from the collection")
    fun test006() {
        val deck = DeckBuilder(defaultShuffler).build(cards)
        assertThat(deckContent(deck)).containsOnly(cardB,cardA,cardC)
    }
}
package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.cardOf
import agrfesta.kcards.playingcards.cards.rankOf
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DeckShuffleEngineTest {

    @Test
    @DisplayName("Draw a card from an empty deck -> throws EmptyDeckException")
    fun drawACardFromEmptyDeckThrowsException() {
        val engine = DeckShuffleEngine(ReverseStackShufflingService())
        assertThat(engine.size()).isEqualTo(0)

        assertThat {
            engine.draw()
        }.isFailure()
                //TODO .hasClass(EmptyDeckException.class)
                .hasMessage("Trying to draw a card from an empty deck")
    }

    @Test
    @DisplayName("Add a Card to an empty deck engine -> draw that card")
    fun addACardAndDrawThatCard() {
        val engine = DeckShuffleEngine(ReverseStackShufflingService())
        assertThat(engine.size()).isEqualTo(0)

        //val rank = rankOf('R',1)
        //val seed = seedOf('s', 1)
        val card = aCard()
        engine.add(card)

        assertThat(engine.size()).isEqualTo(1)
        val draw = engine.draw()
        //assertThat(optionalDraw.isPresent).isTrue()
        assertThat(draw).isEqualTo(card)
    }

    @Test
    @DisplayName("Add three cards to an empty deck engine -> draw those cards")
    fun addThreeCardAndDrawThoseCards() {
        val engine = DeckShuffleEngine(ReverseStackShufflingService())
        assertThat(engine.size()).isEqualTo(0)

        // Add first card
        val rankA = rankOf('R',1)
        val cardA = cardOf(rankA, aSeed())
        engine.add(cardA)
        assertThat(engine.size()).isEqualTo(1)

/*        assertThat(engine.size()).isEqualTo(1)
        val optionalDraw = engine.draw()
        assertThat(optionalDraw.isPresent).isTrue()
        assertThat(optionalDraw.get()).isEqualTo(card)*/
    }

}
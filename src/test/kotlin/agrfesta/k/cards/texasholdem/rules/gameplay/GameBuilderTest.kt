package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.kcards.playingcards.deck.AutoShufflingDeck
import agrfesta.kcards.playingcards.deck.Deck
import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GameMockImpl(
        val payments: GamePayments,
        val deck: Deck,
        val table: Table,
        val preFlopDealerProvider: (GameContext) -> Dealer,
        val dealerProvider: (MutableMap<Player,Int>,GameContext) -> Dealer,
        val showdown: Showdown
): Game {
    override fun play() {}
}

@DisplayName("GameBuilder tests")
class GameBuilderTest {

    @Test
    @DisplayName("Default Game implementation is GameImpl")
    fun defaultGameImplementationIsGameImpl() {
        val game = GameBuilder()
                .build(aGamePayments(), aTable())
        assertThat(game).isInstanceOf(GameImpl::class)
    }
    @Test
    @DisplayName("Default internal implementations: Deck is AutoShufflingDeck, Dealer is PostFlopDealer," +
            " PreFlop Dealer is PreFlopDealer, Showdown is Showdown")
    fun defaultDeckImplementationIsAutoShufflingDeck() {
        val game = GameBuilder()
                .implementation( ::GameMockImpl )
                .build(aGamePayments(), aTable())
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            assertThat(game.deck).isInstanceOf(AutoShufflingDeck::class)
            assertThat(game.dealerProvider.invoke(buildPot(),aContext())).isInstanceOf(PostFlopDealer::class)
            assertThat(game.preFlopDealerProvider.invoke(aContext())).isInstanceOf(PreFlopDealer::class)
            assertThat(game.showdown).isInstanceOf(Showdown::class)
        }
    }

    @Test
    @DisplayName("Builder inject in Game provided GamePayments and Table")
    fun builderInjectInGameProvidedGamePaymentsAndTable() {
        val payments = aGamePayments()
        val table = aTable()
        val game = GameBuilder()
                .implementation( ::GameMockImpl )
                .build(payments, table)
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            assertThat(game.payments === payments).isTrue()
            assertThat(game.table === table).isTrue()
        }
    }

    @Test
    @DisplayName("Builder inject provided internal implementations")
    fun builderInjectProvidedInternalImplementations() {
        val deck = aDeck()
        val showdown = object : Showdown {
            override fun execute(pot: MutableMap<Player, Int>, board: Board) {}
        }
        val dealer = PostFlopDealer(buildPot(),aContext())
        val preFlopDealer = PreFlopDealer(aContext())
        val game = GameBuilder()
                .deck(deck)
                .dealerProvider {_,_ -> dealer}
                .preFlopDealerProvider { preFlopDealer }
                .showdown(showdown)
                .implementation( ::GameMockImpl )
                .build(aGamePayments(), aTable())
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            assertThat(game.deck === deck).isTrue()
            assertThat(game.dealerProvider.invoke(buildPot(),aContext()) === dealer).isTrue()
            assertThat(game.preFlopDealerProvider.invoke(aContext()) === preFlopDealer).isTrue()
            assertThat(game.showdown === showdown).isTrue()
        }
    }

}
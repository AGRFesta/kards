package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.deck.DeckImpl
import agrfesta.k.cards.texasholdem.observers.DealerObserver
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.rules.CardsEvaluatorBaseImpl
import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import assertk.assertions.isTrue
import io.mockk.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

//TODO use mockk
class GameMockImpl(
        val payments: GamePayments,
        val deck: Deck,
        val table: Table,
        val preFlopDealerProvider: (GameContext, DealerObserver?) -> Dealer,
        val dealerProvider: (MutableMap<GamePlayer,Int>, GameContext, DealerObserver?) -> Dealer,
        val showdown: Showdown,
        val observer: GameObserver?
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
    @DisplayName("Default internal implementations: deck is AutoShufflingDeck, dealer is PostFlopDealer," +
            " pre-flop dealer is PreFlopDealer, showdown is Showdown, no observer")
    fun defaultDeckImplementationIsAutoShufflingDeck() {
        val game = GameBuilder()
                .implementation( ::GameMockImpl )
                .build(aGamePayments(), aTable())
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            assertThat(game.deck).isInstanceOf(DeckImpl::class)
            assertThat(game.dealerProvider.invoke(buildPot(),aContext(),null)).isInstanceOf(PostFlopDealer::class)
            assertThat(game.preFlopDealerProvider.invoke(aContext(),null)).isInstanceOf(PreFlopDealer::class)
            assertThat(game.showdown).isInstanceOf(Showdown::class)
            assertThat(game.observer).isNull()
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
            override fun execute(pot: MutableMap<GamePlayer, Int>, board: Board) {}
        }
        val dealer = PostFlopDealer(buildPot(),aContext())
        val preFlopDealer = PreFlopDealer(aContext())
        val game = GameBuilder()
                .deck(deck)
                .dealerProvider {_,_,_ -> dealer}
                .preFlopDealerProvider { _,_ -> preFlopDealer }
                .showdown(showdown)
                .implementation( ::GameMockImpl )
                .build(aGamePayments(), aTable())
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            assertThat(game.deck === deck).isTrue()
            assertThat(game.dealerProvider.invoke(buildPot(),aContext(),null) === dealer).isTrue()
            assertThat(game.preFlopDealerProvider.invoke(aContext(),null) === preFlopDealer).isTrue()
            assertThat(game.showdown === showdown).isTrue()
        }
    }

    @Test
    @DisplayName("Provide an observer to the Builder -> the provided observer will observe the default showdown")
    fun gameBuildingStory000() {
        val observerMock = mockk<GameObserver>()
        every { observerMock.notifyResult(any()) } just Runs
        val game = GameBuilder()
                .observer(observerMock)
                .implementation( ::GameMockImpl )
                .build(aGamePayments(), aTable())
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            game.showdown.execute(buildPot(),EmptyBoard(aDeck()))
            verify(exactly = 1) { observerMock.notifyResult(any()) }
        }
    }
    @Test
    @DisplayName("Provide an observer and an already instanced showdown to the Builder -> " +
            "the provided observer will not observe the provided showdown")
    fun gameBuildingStory001() {
        val showdownObserverMock = mockk<GameObserver>()
        every { showdownObserverMock.notifyResult(any()) } just Runs
        val showdown = ShowdownImpl(CardsEvaluatorBaseImpl(),showdownObserverMock)

        val observerMock = mockk<GameObserver>()
        every { observerMock.notifyResult(any()) } just Runs
        val game = GameBuilder()
                .showdown(showdown)
                .observer(observerMock)
                .implementation( ::GameMockImpl )
                .build(aGamePayments(), aTable())
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            game.showdown.execute(buildPot(),EmptyBoard(aDeck()))
            verify(exactly = 0) { observerMock.notifyResult(any()) }
            verify(exactly = 1) { showdownObserverMock.notifyResult(any()) }
        }
    }
    @Test
    @DisplayName("Provide an observer and a showdown, that will use the game observer, to the Builder -> " +
            "the provided observer will observe the provided showdown")
    fun gameBuildingStory002() {
        val observerMock = mockk<GameObserver>()
        every { observerMock.notifyResult(any()) } just Runs
        val game = GameBuilder()
                .showdown { ShowdownImpl(CardsEvaluatorBaseImpl(),it) }
                .observer(observerMock)
                .implementation( ::GameMockImpl )
                .build(aGamePayments(), aTable())
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            game.showdown.execute(buildPot(),EmptyBoard(aDeck()))
            verify(exactly = 1) { observerMock.notifyResult(any()) }
        }
    }

}
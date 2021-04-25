package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.suits.Suit
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.rules.CardsEvaluatorBaseImpl
import agrfesta.k.cards.texasholdem.rules.gameplay.GameBuilder.Companion.buildingAGame
import agrfesta.k.cards.texasholdem.utils.UuidProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import assertk.assertions.isTrue
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class GameMockImpl(
        val context: GameContext,
        val dealerFactory: DealerFactory,
        val showdown: Showdown,
        val observer: GameObserver?
): Game {
    override fun getId() = context.uuid
    override fun play(): List<PlayerStack> = listOf()
}

@DisplayName("GameBuilder tests")
class GameBuilderTest {

    @Test
    @DisplayName("Default Game implementation is GameImpl")
    fun defaultGameImplementationIsGameImpl() {
        val game = buildingAGame()
                .withPayments(aGamePayments())
                .withTable(aTable().map { it.player owns it.stack })
                .build()
        assertThat(game).isInstanceOf(GameImpl::class)
    }
    @Test
    @DisplayName("Default internal implementations: deck is AutoShufflingDeck, dealerFactory is DealerFactoryImpl," +
            " showdown is Showdown, no observer")
    fun defaultDeckImplementationIsAutoShufflingDeck() {
        val game = buildingAGame()
                .withPayments(aGamePayments())
                .withTable(aTable().map { it.player owns it.stack })
                .implementedBy( ::GameMockImpl )
                .build()
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            assertThat(game.dealerFactory).isInstanceOf(DealerFactoryImpl::class)
            assertThat(game.showdown).isInstanceOf(Showdown::class)
            assertThat(game.observer).isNull()
        }
    }

    @Test
    @DisplayName("Builder inject in Game provided GamePayments and Table")
    fun builderInjectInGameProvidedGamePaymentsAndTable() {
        val payments = aGamePayments()
        val table = aTable().map { it.player owns it.stack }
        val game = buildingAGame()
                .withPayments(payments)
                .withTable(table)
                .implementedBy( ::GameMockImpl )
                .build()
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            assertThat(game.context.payments === payments).isTrue()
            assertThat(game.context.table).hasSameContentOf(table)
        }
    }

    @Test
    @DisplayName("Builder inject provided internal implementations")
    fun builderInjectProvidedInternalImplementations() {
        val deck = Suit.FRENCH.createDeck()
        val showdown = mockk<Showdown>()
        val dealerFactory = mockk<DealerFactory>()
        val game = buildingAGame()
                .withPayments(aGamePayments())
                .withTable(aTable().map { it.player owns it.stack })
                .withDeck(deck)
                .withDealerFactory(dealerFactory)
                .showdown(showdown)
                .implementedBy( ::GameMockImpl )
                .build()
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            assertThat(game.dealerFactory === dealerFactory).isTrue()
            assertThat(game.showdown === showdown).isTrue()
        }
    }

    @Test
    @DisplayName("Provide an observer to the Builder -> the provided observer will observe the default showdown")
    fun gameBuildingStory000() {
        val observerMock = mockk<GameObserver>()
        every { observerMock.notifyResult(any()) } just Runs
        val game = buildingAGame()
                .withPayments(aGamePayments())
                .withTable(aTable().map { it.player owns it.stack })
                .observedBy(observerMock)
                .implementedBy( ::GameMockImpl )
                .build()
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
        val game = buildingAGame()
                .withPayments(aGamePayments())
                .withTable(aTable().map { it.player owns it.stack })
                .showdown(showdown)
                .observedBy(observerMock)
                .implementedBy( ::GameMockImpl )
                .build()
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
        val game = buildingAGame()
                .withPayments(aGamePayments())
                .withTable(aTable().map { it.player owns it.stack })
                .showdown { ShowdownImpl(CardsEvaluatorBaseImpl(),it) }
                .observedBy(observerMock)
                .implementedBy( ::GameMockImpl )
                .build()
        assertThat(game).isInstanceOf(GameMockImpl::class)
        if (game is GameMockImpl) {
            game.showdown.execute(buildPot(),EmptyBoard(aDeck()))
            verify(exactly = 1) { observerMock.notifyResult(any()) }
        }
    }

    @Test
    @DisplayName("build(): building providing a uuid provider -> built game with uuid provided")
    fun buildTest000() {
        val uuid = UUID.randomUUID()
        val uuidProvider: UuidProvider = mockk()
        every { uuidProvider.invoke() } answers { uuid }

        val game = buildingAGame()
            .withPayments(aGamePayments())
            .withTable(aTable().map { it.player owns it.stack })
            .withUuidProvider(uuidProvider)
            .build()

        assertThat(game.getId()).isEqualTo(uuid)
    }

}

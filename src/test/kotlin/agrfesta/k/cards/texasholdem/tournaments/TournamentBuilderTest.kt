package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.playingcards.utils.RandomGenerator
import agrfesta.k.cards.playingcards.utils.SimpleRandomGenerator
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.observers.TournamentObserver
import agrfesta.k.cards.texasholdem.rules.gameplay.Game
import agrfesta.k.cards.texasholdem.rules.gameplay.GameBuilder.Companion.buildingAGame
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import agrfesta.k.cards.texasholdem.rules.gameplay.aStrategy
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tournament Builder tests")
class TournamentBuilderTest {
    private val payments = mockk<IncreasingGamePayments>(relaxed = true)

    private fun testBuilder(trnImplementer: (Set<Player>, Int, IncreasingGamePayments,
                                             (Int) -> Int,
                                             (IncreasingGamePayments, Table<InGamePlayer>, GameObserver?) -> Game,
                                             TournamentObserver?) -> Tournament) =
            TournamentBuilder(SimpleRandomGenerator(),
                    { payments,table,_ -> buildingAGame()
                            .withPayments(payments)
                            .withTable(table).build() },
                    trnImplementer)

    @Test
    @DisplayName("Observer not provided -> the observer is not provided to the tournament")
    fun story000() {
        testBuilder { _, _, _, _, _,observer ->
                assertThat(observer).isNull()
                mockk() }
            .subscriptions( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
            .build(2000,payments)
    }
    @Test
    @DisplayName("Observer provided -> the observer is provided to the tournament")
    fun story001() {
        val providedObserver = mockk<TournamentObserver>()
        testBuilder { _,_,_,_,_,observer ->
                assertThat(observer).isEqualTo(providedObserver)
                mockk() }
            .subscriptions( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
            .observer(providedObserver)
            .build(2000,payments)
    }

    @Test
    @DisplayName("Tournament implementation injected -> build a tournament that is the one provided from the " +
            "injected provider")
    fun story002() {
        val expectedTournament: Tournament = mockk()
        val tournament = testBuilder { _,_,_,_,_,_ -> expectedTournament}
                .subscriptions( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
                .build(2000,payments)
        assertThat(tournament).isEqualTo(expectedTournament)
    }

    @Test
    @DisplayName("Button provider not provided -> the initial button position will be the nextInt on the " +
            "random generator")
    fun story003() {
        val rndGenerator: RandomGenerator = mockk()
        every { rndGenerator.nextInt(10) } returns 5
        TournamentBuilder(rndGenerator,
                { payments,table,_ -> buildingAGame()
                        .withPayments(payments)
                        .withTable(table).build() },
                { _,_,_,buttonProvider,_,_ ->
                    assertThat(buttonProvider.invoke(10)).isEqualTo(5)
                    mockk() })
            .subscriptions( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
            .build(2000,payments)
    }
    @Test
    @DisplayName("Button provider provided -> the initial button position will be provided by the provider and " +
            "not from the default one")
    fun story004() {
        val rndGenerator: RandomGenerator = mockk()
        every { rndGenerator.nextInt(10) } returns 5
        TournamentBuilder(rndGenerator,
                { payments,table,_ -> buildingAGame()
                        .withPayments(payments)
                        .withTable(table).build() },
                { _,_,_,buttonProvider,_,_ ->
                    assertThat(buttonProvider.invoke(10)).isEqualTo(10)
                    mockk() })
            .subscriptions( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
            .buttonProvider { it }
            .build(2000,payments)
    }

    @Test
    @DisplayName("Zero subscriptions -> throws an exception")
    fun story005() {
        val failure = assertThat {
            tournamentBuilder()
                    .build(2000,payments)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Unable to create a tournament with zero players!")
    }
    @Test
    @DisplayName("One subscription -> throws an exception")
    fun story006() {
        val failure = assertThat {
            tournamentBuilder()
                    .subscriptions( Player("Alex",aStrategy()) )
                    .build(2000,payments)
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Unable to create a tournament with only one player!")
    }
    @Test
    @DisplayName("Tournament with three subscriptions -> all and oly those three are provided to the tournament")
    fun story007() {
        val alex = Player("Alex",aStrategy())
        val poly = Player("Poly",aStrategy())
        val jane = Player("Jane",aStrategy())
        testBuilder { subscriptions,_,_,_,_,_ ->
            assertThat(subscriptions)
                    .containsOnly(alex, poly, jane)
            mockk() }
                .subscriptions(alex, poly, jane)
                .build(2000,payments)
    }

    @Test
    @DisplayName("Game provider not provided -> the default game provider is provided to the tournament")
    fun story008() {
        val expectedDefaultGameProvider:
                (IncreasingGamePayments, Table<InGamePlayer>, GameObserver?) -> Game = { _, _, _ -> mockk() }
        TournamentBuilder(SimpleRandomGenerator(), expectedDefaultGameProvider) {
            _, _, _, _, gameProvider, _ ->
            assertThat(gameProvider).isEqualTo(expectedDefaultGameProvider)
            mockk()
        }
                .subscriptions( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
                .build(2000,payments)
    }
    @Test
    @DisplayName("Game provider provided -> the provided provider is provided to the tournament")
    fun story009() {
        val expectedGameProvider:
                (IncreasingGamePayments, Table<InGamePlayer>, GameObserver?) -> Game = {_,_,_ -> mockk() }
        val defaultGameProvider:
                (IncreasingGamePayments, Table<InGamePlayer>, GameObserver?) -> Game = {_,_,_ -> mockk() }
        TournamentBuilder(SimpleRandomGenerator(), defaultGameProvider) {
            _, _, _, _, gameProvider, _ ->
            assertThat(gameProvider).isEqualTo(expectedGameProvider)
            mockk()
        }
                .subscriptions( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
                .gameProvider(expectedGameProvider)
                .build(2000,payments)
    }

}

package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.playingcards.utils.RandomGenerator
import agrfesta.k.cards.texasholdem.observers.TournamentObserver
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
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

    @Test
    @DisplayName("Observer not provided -> the observer is not provided to the tournament")
    fun story000() {
        TournamentBuilder.buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .implementedBy { _, _, _, _, _, observer ->
                    assertThat(observer).isNull()
                    mockk() }
            .withSubscribers( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
            .build()
    }
    @Test
    @DisplayName("Observer provided -> the observer is provided to the tournament")
    fun story001() {
        val providedObserver = mockk<TournamentObserver>()
        TournamentBuilder.buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .implementedBy { _, _, _, _, _, observer ->
                    assertThat(observer).isEqualTo(providedObserver)
                    mockk() }
            .withSubscribers( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
            .observerBy(providedObserver)
            .build()
    }

    @Test
    @DisplayName("Tournament implementation injected -> build a tournament that is the one provided from the " +
            "injected provider")
    fun story002() {
        val expectedTournament: Tournament = mockk()
        val tournament = TournamentBuilder.buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .implementedBy { _, _, _, _, _, _ -> expectedTournament}
                .withSubscribers( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
                .build()
        assertThat(tournament).isEqualTo(expectedTournament)
    }

    @Test
    @DisplayName("Button provider not provided -> the initial button position will be the nextInt on the " +
            "random generator")
    fun story003() {
        val rndGenerator: RandomGenerator = mockk()
        every { rndGenerator.nextInt(10) } returns 5
        TournamentBuilder.buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .implementedBy { _, _, _, buttonProvider, _, _ ->
                    assertThat(buttonProvider.invoke(10)).isEqualTo(5)
                    mockk() }
                .withRandomGenerator(rndGenerator)
                .withSubscribers( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
                .build()
    }
    @Test
    @DisplayName("Button provider provided -> the initial button position will be provided by the provider and " +
            "not from the default one")
    fun story004() {
        val rndGenerator: RandomGenerator = mockk()
        every { rndGenerator.nextInt(10) } returns 5
        TournamentBuilder.buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .implementedBy { _, _, _, buttonProvider, _, _ ->
                    assertThat(buttonProvider.invoke(10)).isEqualTo(10)
                    mockk() }
                .withRandomGenerator(rndGenerator)
                .withSubscribers( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
                .withButtonProvider { it }
                .build()
    }

    @Test
    @DisplayName("Zero subscriptions -> throws an exception")
    fun story005() {
        val failure = assertThat {
            TournamentBuilder.buildingTournament()
                    .withAnInitialStackOf(2000)
                    .withPayments(payments)
                    .build()
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Unable to create a tournament with zero players!")
    }
    @Test
    @DisplayName("One subscription -> throws an exception")
    fun story006() {
        val failure = assertThat {
            TournamentBuilder.buildingTournament()
                    .withAnInitialStackOf(2000)
                    .withPayments(payments)
                    .withSubscribers( Player("Alex",aStrategy()) )
                    .build()
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
        TournamentBuilder.buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .implementedBy { subscriptions, _, _, _, _, _ ->
                    assertThat(subscriptions).containsOnly(alex, poly, jane)
                    mockk() }
                .withSubscribers(alex, poly, jane)
                .build()
    }

    @Test
    @DisplayName("Game provider not provided -> the default game provider is provided to the tournament")
    fun story008() {
        TournamentBuilder.buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .implementedBy { _, _, _, _, gameProvider, _ ->
                    assertThat(gameProvider).isEqualTo(defaultGameProvider)
                    mockk() }
                .withSubscribers( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
                .build()
    }
    @Test
    @DisplayName("Game provider provided -> the provided provider is provided to the tournament")
    fun story009() {
        val expectedGameProvider: GameProvider = { _, _, _ -> mockk() }
        TournamentBuilder.buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .implementedBy { _, _, _, _, gameProvider, _ ->
                    assertThat(gameProvider).isEqualTo(expectedGameProvider)
                    mockk() }
                .withSubscribers( Player("Alex",aStrategy()), Player("Poly",aStrategy()) )
                .withGameProvider(expectedGameProvider)
                .build()
    }

}

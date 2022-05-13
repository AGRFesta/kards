package org.agrfesta.k.kards.texasholdem.tournaments

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerIdentityImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStrategyInterface
import org.agrfesta.k.kards.texasholdem.rules.gameplay.call
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SeedBasedTournamentTest {
    private val levels = listOf(
        LevelPayments(25u, 50u),
        LevelPayments(50u, 100u),
        LevelPayments(125u, 250u),
        LevelPayments(200u, 400u),
        LevelPayments(500u, 1000u),
        LevelPayments(1000u, 2000u),
        LevelPayments(2000u, 4000u),
        LevelPayments(5000u, 10000u)
    )
    private val paymentsDefinition = IncreasingGamePaymentsDefinition(structure = levels, gamesPerLevel = 20u)
    private val descriptor: TournamentDescriptor =
        TournamentDescriptorImpl(initialStack = 2000u, paymentsDefinition = paymentsDefinition)

    private val limperStrategy: PlayerStrategyInterface = { _, _ -> call() }

    private val alex: Player = PlayerImpl(identity = PlayerIdentityImpl(name = "Alex") , strategy = limperStrategy)
    private val poly: Player = PlayerImpl(identity = PlayerIdentityImpl(name = "Poly") , strategy = limperStrategy)
    private val jane: Player = PlayerImpl(identity = PlayerIdentityImpl(name = "Jane") , strategy = limperStrategy)
    private val paul: Player = PlayerImpl(identity = PlayerIdentityImpl(name = "Paul") , strategy = limperStrategy)
    private val mark: Player = PlayerImpl(identity = PlayerIdentityImpl(name = "Mark") , strategy = limperStrategy)

    @Test
    @DisplayName("Tournaments based on same seed -> returns same result")
    fun tournamentsBasedOnSameSeed_returnsSameResult() {
        val seed = 290580L

        val firstTournament = tournamentBySeed(seed = seed, descriptor = descriptor,
            subscriptions = setOf(alex, poly, jane, paul, mark))
        val firstResult = firstTournament.play()

        val secondTournament = tournamentBySeed(seed = seed, descriptor = descriptor,
            subscriptions = setOf(alex, poly, jane, paul, mark))
        val secondResult = secondTournament.play()

        assertThat(firstResult).isEqualTo(secondResult)
    }

    @Test
    @DisplayName("Tournaments based on different seeds -> returns different result")
    fun tournamentsBasedOnDifferentSeed_returnsDifferentResult() {
        val firstTournament = tournamentBySeed(seed = 800529L, descriptor = descriptor,
            subscriptions = setOf(alex, poly, jane, paul, mark))
        val firstResult = firstTournament.play()

        val secondTournament = tournamentBySeed(seed = 290580L, descriptor = descriptor,
            subscriptions = setOf(alex, poly, jane, paul, mark))
        val secondResult = secondTournament.play()

        assertThat(firstResult).isNotEqualTo(secondResult)
    }

}

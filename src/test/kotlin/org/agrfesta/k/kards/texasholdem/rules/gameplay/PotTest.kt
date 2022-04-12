package org.agrfesta.k.kards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Pot tests")
class PotTest {

    @Test
    @DisplayName("Pot [aPlayer=1,aPlayer=3,aPlayer=5] -> amount is 9")
    fun potAmountReturnsSumOfThreePlayersValues() {
        val pot = buildMutablePot()
        pot[anInGamePlayer()] = 1u
        pot[anInGamePlayer()] = 3u
        pot[anInGamePlayer()] = 5u
        assertThat(pot.amount()).isEqualTo(9u)
    }
    @Test
    @DisplayName("Pot [player=1] -> amount is 1")
    fun potAmountReturnsValueOfTheOnlyPlayers() {
        val pot = buildMutablePot()
        pot[anInGamePlayer()] = 1u
        assertThat(pot.amount()).isEqualTo(1u)
    }
    @Test
    @DisplayName("Empty pot -> amount is 0")
    fun emptyPotAmountReturnsZero() {
        assertThat(buildMutablePot().amount()).isEqualTo(0u)
    }

    @Test
    @DisplayName("Pot [APlayer=1,FoldedPlayer=3,CallingPlayer=5,AllInPlayer=7,RaisingPlayer=13] -> " +
                            "[APlayer,FoldedPlayer,CallingPlayer,AllInPlayer,RaisingPlayer]")
    fun playersReturnsASetOfAllInvolvedPlayers() {
        val pot = buildMutablePot()
        val player = anInGamePlayer()
        val foldedPlayer = foldedPlayer()
        val callingPlayer = callingPlayer()
        val allInPlayer = allInPlayer()
        val raisingPlayer = raisingPlayer()
        pot[player] = 1u
        pot[foldedPlayer] = 3u
        pot[callingPlayer] = 5u
        pot[allInPlayer] = 7u
        pot[raisingPlayer] = 13u
        val result = pot.players()
        assertThat(result !== pot.keys).isTrue()
        assertThat(result).containsOnly(player,foldedPlayer,callingPlayer,allInPlayer,raisingPlayer)
    }
    @Test
    @DisplayName("Pot [APlayer=1] -> [APlayer]")
    fun playersReturnsWithASingleElementIfThereIsOnlyAPlayerInvolved() {
        val pot = buildMutablePot()
        val player = anInGamePlayer()
        pot[player] = 1u
        val result = pot.players()
        assertThat(result !== pot.keys).isTrue()
        assertThat(result).containsOnly(player)
    }
    @Test
    @DisplayName("Empty Pot -> empty Set")
    fun playersReturnsEmptySetIfThePotIsEmpty() {
        val pot = buildMutablePot()
        val result = pot.players()
        assertThat(result !== pot.keys).isTrue()
        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("Missing Player -> 0")
    fun payedByReturns0IfThePlayerIsNotInThePot() {
        val pot = buildMutablePot()
        assertThat(pot.payedBy(anInGamePlayer())).isEqualTo(0u)
    }
    @Test
    @DisplayName("Pot [APlayer=5] -> 5")
    fun payedByReturnsThePayedAmountOfASpecificPlayer() {
        val pot = buildMutablePot()
        val player = anInGamePlayer()
        pot[player] = 5u
        assertThat(pot.payedBy(player)).isEqualTo(5u)
    }

    @Test
    @DisplayName("[] + [] -> []")
    fun sumOfTwoEmptyPotReturnsAEmptyPot() {
        assertThat(buildMutablePot() + buildMutablePot()).isEmpty()
    }
    @Test
    @DisplayName("[aPlayer=5] + [] -> [aPlayer=5]")
    fun sumOfAPotNotEmptyWithAnEmptyOneReturnsAPotWithSameContentOfTheFirstOne() {
        val pot = buildMutablePot()
        val player = anInGamePlayer()
        pot[player] = 5u
        assertThat((pot + buildMutablePot())).containsOnly(player to 5u)
    }
    @Test
    @DisplayName("[Jane=5,Giulia=2] + [Alex=3,Jane=1] -> [Jane=6,Alex=3,Giulia=2]")
    fun sumOfPotsWithPlayersInCommonReturnsPotWithThosePlayerWithSummedValues() {
        val jane = anInGamePlayer(name = "Jane")
        val alex = anInGamePlayer(name = "Alex")
        val giulia = anInGamePlayer(name = "Giulia")
        val potA = buildMutablePot()
        potA[jane] = 5u
        potA[giulia] = 2u
        val potB = buildMutablePot()
        potB[alex] = 3u
        potB[jane] = 1u
        assertThat(potA + potB).containsOnly(jane to 6u, alex to 3u, giulia to 2u)
    }
    @Test
    @DisplayName("[Jane=200,Alex=200] + [Alex=400,Jane=200] -> [Jane=400,Alex=600]")
    fun potSumStory000() {
        val jane = anInGamePlayer(name = "Jane")
        val alex = anInGamePlayer(name = "Alex")
        val potA = buildMutablePot()
        potA[alex] = 200u
        potA[jane] = 200u
        val potB = buildMutablePot()
        potB[alex] = 400u
        potB[jane] = 200u
        assertThat(potA + potB).containsOnly(jane to 400u, alex to 600u)
    }

    @Test
    @DisplayName("APlayer(1000) sends 200 to Pot[] -> APlayer(800), Pot[APlayer=200]")
    fun receiveFromAddEffectivePaymentsToAnEmptyPotAndRemovesItFromThePlayer() {
        val pot = buildMutablePot()
        val player = anInGamePlayer(stack = 1000u)
        assertThat(pot.receiveFrom(player, 200u)).isEqualTo(200u)
        assertThat(pot.payedBy(player)).isEqualTo(200u)
        assertThat(player.stack).isEqualTo(800u)
    }
    @Test
    @DisplayName("APlayer(300) sends 800 to Pot[] -> APlayer(0), Pot[APlayer=300]")
    fun receiveFromAddOnlyTheEffectivePaymentsToAnEmptyPotAndRemovesItFromThePlayer() {
        val pot = buildMutablePot()
        val player = anInGamePlayer(stack = 300u)
        assertThat(pot.receiveFrom(player, 800u)).isEqualTo(300u)
        assertThat(pot.payedBy(player)).isEqualTo(300u)
        assertThat(player.stack).isEqualTo(0u)
    }
    @Test
    @DisplayName("APlayer(300) sends 100 to Pot[APlayer=500] -> APlayer(200), Pot[APlayer=600]")
    fun receiveFromAddTheEffectivePaymentsToThePreviousPaymentMadeByThePlayer() {
        val pot = buildMutablePot()
        val player = anInGamePlayer(stack = 300u)
        pot[player] = 500u

        assertThat(pot.receiveFrom(player, 100u)).isEqualTo(100u)
        assertThat(pot.payedBy(player)).isEqualTo(600u)
        assertThat(player.stack).isEqualTo(200u)
    }

    @Test
    @DisplayName("Extract balanced pot from Pot[] ->  bp=Pot[] origin=Pot[]")
    fun extractBalancedPotFromAnEmptyPotReturnsAnEmptyPot() {
        val pot = buildMutablePot()
        assertThat(pot.extractBalancedPot()).isEmpty()
        assertThat(pot).isEmpty()
    }
    @Test
    @DisplayName("Extract balanced pot from Pot[hero=100] -> bp=Pot[hero=100] origin=Pot[]")
    fun extractBalancedPotFromAPotContainingASinglePlayerReturnsAPotWithSameDataAndOriginalOneEmpty() {
        val hero = anInGamePlayer(name = "hero")
        val pot = buildMutablePot()
        pot[hero] = 100u
        assertThat(pot.extractBalancedPot()).containsOnly(hero to 100u)
        assertThat(pot).isEmpty()
    }
    @Test
    @DisplayName("Extract balanced pot from Pot[Alex=100,Giulia=100,Jane=100] -> " +
            "bp=Pot[Alex=100,Giulia=100,Jane=100] origin=Pot[]")
    fun extractBalancedPotFromABalancedPotReturnsAPotWithSameDataAndOriginalOneEmpty() {
        val alex = anInGamePlayer(name = "Alex")
        val giulia = anInGamePlayer(name = "Giulia")
        val jane = anInGamePlayer(name = "Jane")
        val pot = buildMutablePot()
        pot[alex] = 100u
        pot[giulia] = 100u
        pot[jane] = 100u
        assertThat(pot.extractBalancedPot()).containsOnly(alex to 100u,jane to 100u, giulia to 100u)
        assertThat(pot).isEmpty()
    }
    @Test
    @DisplayName("Extract balanced pot from Pot[Alex=100,Giulia=200] -> " +
            "               bp=Pot[Alex=100,Giulia=100] origin=Pot[Giulia=100]")
    fun extractBalancedPotFromAPotContainingTwoPlayersWithDifferentValuesReturnsBalancedPotAndRemoveFromOriginalPot() {
        val alex = anInGamePlayer(name = "Alex")
        val giulia = anInGamePlayer(name = "Giulia")
        val pot = buildMutablePot()
        pot[alex] = 100u
        pot[giulia] = 200u
        assertThat(pot.extractBalancedPot()).containsOnly(alex to 100u, giulia to 100u)
        assertThat(pot).containsOnly(giulia to 100u)
    }

    @Test
    @DisplayName("Decompose Pot[] -> []")
    fun decomposeAnEmptyPotReturnsAnEmptyCollection() {
        val pot = buildMutablePot()
        assertThat(pot.decompose()).isEmpty()
        assertThat(pot).isEmpty()
    }
    @Test
    @DisplayName("Decompose Pot[Alex=100] -> [Pot[Alex=100]]")
    fun decomposeAPotWithOnlyAPlayerReturnsACollectionWithOnePot() {
        val alex = anInGamePlayer(name = "Alex")
        val pot = PotBuilder().contribution(alex,100u).build()
        assertThat(pot.decompose()).containsOnly(
                PotBuilder().contribution(alex,100u).build()
        )
        assertThat(pot).isEmpty()
    }
    @Test
    @DisplayName("Decompose Pot[Alex=100,Jane=150,Giulia=210,Sasha=210] -> " +
            "[Pot[Alex=100,Jane=100,Giulia=100,Sasha=100]," +
            " Pot[Jane=50,Giulia=50,Sasha=50]," +
            " Pot[Giulia=60,Sasha=60]]")
    fun decomposeAPotReturnsACollectionOfAllBalancedPotLeavingThePotEmpty() {
        val alex = anInGamePlayer(name = "Alex")
        val jane = anInGamePlayer(name = "Jane")
        val giulia = anInGamePlayer(name = "Giulia")
        val sasha = anInGamePlayer(name = "Sasha")
        val pot = PotBuilder()
                .contribution(alex,100u)
                .contribution(jane,150u)
                .contribution(giulia,210u)
                .contribution(sasha,210u)
                .build()
        assertThat(pot.decompose()).containsOnly(
                PotBuilder()
                        .contribution(alex,100u)
                        .contribution(jane,100u)
                        .contribution(giulia,100u)
                        .contribution(sasha,100u)
                        .build(),
                PotBuilder()
                        .contribution(jane,50u)
                        .contribution(giulia,50u)
                        .contribution(sasha,50u)
                        .build(),
                PotBuilder()
                        .contribution(giulia,60u)
                        .contribution(sasha,60u)
                        .build()
        )
        assertThat(pot).isEmpty()
    }

    @Test
    @DisplayName("Max Contribution from Pot[] -> null")
    fun maxContributionFromAnEmptyPotReturnsNull() {
        assertThat(buildMutablePot().maxContribution()).isNull()
    }
    @Test
    @DisplayName("Max Contribution from Pot[Alex=100] -> {player=Alex,amount=100}")
    fun maxContributionFromASinglePlayerPotReturnsPlayerContribution() {
        val alex = anInGamePlayer(name = "Alex")
        val pot = PotBuilder().contribution(alex,100u).build()
        val max = pot.maxContribution()
        assertThat(max).isNotNull()
        assertThat(max?.player).isEqualTo(alex)
        assertThat(max?.amount).isEqualTo(100u)
    }
    @Test
    @DisplayName("Max Contribution from Pot[Alex=100,Giulia=150,Jane=90] -> {player=Giulia,amount=150}")
    fun maxContributionReturnsMaxContributionByAmountFromAllPlayerInPot() {
        val alex = anInGamePlayer(name = "Alex")
        val giulia = anInGamePlayer(name = "Giulia")
        val jane = anInGamePlayer(name = "Jane")
        val pot = PotBuilder()
                .contribution(alex,100u)
                .contribution(giulia,150u)
                .contribution(jane,90u)
                .build()
        val max = pot.maxContribution()
        assertThat(max).isNotNull()
        assertThat(max?.player).isEqualTo(giulia)
        assertThat(max?.amount).isEqualTo(150u)
    }

    @Test
    @DisplayName("An empty Pot receives an amount of 0 from a player -> the Pot is still empty")
    fun potIgnoresReceivedAmountIfZero() {
        val pot = buildMutablePot()
        pot.receiveFrom(anInGamePlayer(), 0u)
        assertThat(pot).isEmpty()
    }
}

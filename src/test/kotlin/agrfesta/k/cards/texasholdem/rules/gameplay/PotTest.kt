package agrfesta.k.cards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
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
        val pot = buildPot()
        pot[anInGamePlayer()] = 1
        pot[anInGamePlayer()] = 3
        pot[anInGamePlayer()] = 5
        assertThat(pot.amount()).isEqualTo(9)
    }
    @Test
    @DisplayName("Pot [player=1] -> amount is 1")
    fun potAmountReturnsValueOfTheOnlyPlayers() {
        val pot = buildPot()
        pot[anInGamePlayer()] = 1
        assertThat(pot.amount()).isEqualTo(1)
    }
    @Test
    @DisplayName("Empty pot -> amount is 0")
    fun emptyPotAmountReturnsZero() {
        assertThat(buildPot().amount()).isEqualTo(0)
    }

    @Test
    @DisplayName("Pot [APlayer=1,FoldedPlayer=3,CallingPlayer=5,AllInPlayer=7,RaisingPlayer=13] -> " +
                            "[APlayer,FoldedPlayer,CallingPlayer,AllInPlayer,RaisingPlayer]")
    fun playersReturnsASetOfAllInvolvedPlayers() {
        val pot = buildPot()
        val player = anInGamePlayer()
        val foldedPlayer = foldedPlayer()
        val callingPlayer = callingPlayer()
        val allInPlayer = allInPlayer()
        val raisingPlayer = raisingPlayer()
        pot[player] = 1
        pot[foldedPlayer] = 3
        pot[callingPlayer] = 5
        pot[allInPlayer] = 7
        pot[raisingPlayer] = 13
        val result = pot.players()
        assertThat(result !== pot.keys).isTrue()
        assertThat(result).containsOnly(player,foldedPlayer,callingPlayer,allInPlayer,raisingPlayer)
    }
    @Test
    @DisplayName("Pot [APlayer=1] -> [APlayer]")
    fun playersReturnsWithASingleElementIfThereIsOnlyAPlayerInvolved() {
        val pot = buildPot()
        val player = anInGamePlayer()
        pot[player] = 1
        val result = pot.players()
        assertThat(result !== pot.keys).isTrue()
        assertThat(result).containsOnly(player)
    }
    @Test
    @DisplayName("Empty Pot -> empty Set")
    fun playersReturnsEmptySetIfThePotIsEmpty() {
        val pot = buildPot()
        val result = pot.players()
        assertThat(result !== pot.keys).isTrue()
        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("Missing Player -> 0")
    fun payedByReturns0IfThePlayerIsNotInThePot() {
        val pot = buildPot()
        assertThat(pot.payedBy(anInGamePlayer())).isEqualTo(0)
    }
    @Test
    @DisplayName("Pot [APlayer=5] -> 5")
    fun payedByReturnsThePayedAmountOfASpecificPlayer() {
        val pot = buildPot()
        val player = anInGamePlayer()
        pot[player] = 5
        assertThat(pot.payedBy(player)).isEqualTo(5)
    }

    @Test
    @DisplayName("[] + [] -> []")
    fun sumOfTwoEmptyPotReturnsAEmptyPot() {
        assertThat(buildPot() + buildPot()).isEmpty()
    }
    @Test
    @DisplayName("[aPlayer=5] + [] -> [aPlayer=5]")
    fun sumOfAPotNotEmptyWithAnEmptyOneReturnsAPotWithSameContentOfTheFirstOne() {
        val pot = buildPot()
        val player = anInGamePlayer()
        pot[player] = 5
        assertThat((pot + buildPot())).containsOnly(player to 5)
    }
    @Test
    @DisplayName("[Jane=5,Giulia=2] + [Alex=3,Jane=1] -> [Jane=6,Alex=3,Giulia=2]")
    fun sumOfPotsWithPlayersInCommonReturnsPotWithThosePlayerWithSummedValues() {
        val jane = anInGamePlayer("Jane")
        val alex = anInGamePlayer("Alex")
        val giulia = anInGamePlayer("Giulia")
        val potA = buildPot()
        potA[jane] = 5
        potA[giulia] = 2
        val potB = buildPot()
        potB[alex] = 3
        potB[jane] = 1
        assertThat(potA + potB).containsOnly(jane to 6, alex to 3, giulia to 2)
    }
    @Test
    @DisplayName("[Jane=200,Alex=200] + [Alex=400,Jane=200] -> [Jane=400,Alex=600]")
    fun potSumStory000() {
        val jane = anInGamePlayer("Jane")
        val alex = anInGamePlayer("Alex")
        val potA = buildPot()
        potA[alex] = 200
        potA[jane] = 200
        val potB = buildPot()
        potB[alex] = 400
        potB[jane] = 200
        assertThat(potA + potB).containsOnly(jane to 400, alex to 600)
    }

    @Test
    @DisplayName("APlayer(1000) sends 200 to Pot[] -> APlayer(800), Pot[APlayer=200]")
    fun receiveFromAddEffectivePaymentsToAnEmptyPotAndRemovesItFromThePlayer() {
        val pot = buildPot()
        val player = anInGamePlayer(1000)
        assertThat(pot.receiveFrom(player, 200)).isEqualTo(200)
        assertThat(pot.payedBy(player)).isEqualTo(200)
        assertThat(player.stack).isEqualTo(800)
    }
    @Test
    @DisplayName("APlayer(300) sends 800 to Pot[] -> APlayer(0), Pot[APlayer=300]")
    fun receiveFromAddOnlyTheEffectivePaymentsToAnEmptyPotAndRemovesItFromThePlayer() {
        val pot = buildPot()
        val player = anInGamePlayer(300)
        assertThat(pot.receiveFrom(player, 800)).isEqualTo(300)
        assertThat(pot.payedBy(player)).isEqualTo(300)
        assertThat(player.stack).isEqualTo(0)
    }
    @Test
    @DisplayName("APlayer(300) sends 100 to Pot[APlayer=500] -> APlayer(200), Pot[APlayer=600]")
    fun receiveFromAddTheEffectivePaymentsToThePreviousPaymentMadeByThePlayer() {
        val pot = buildPot()
        val player = anInGamePlayer(300)
        pot[player] = 500

        assertThat(pot.receiveFrom(player, 100)).isEqualTo(100)
        assertThat(pot.payedBy(player)).isEqualTo(600)
        assertThat(player.stack).isEqualTo(200)
    }
    @Test
    @DisplayName("APlayer(300) sends -100 to Pot[] -> raises an Exception")
    fun receiveFromANegativeAmountRaisesAnException() {
        val pot = buildPot()
        val failure = assertThat {
            pot.receiveFrom(anInGamePlayer(300), -100)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Can't receive a negative amount")
    }

    @Test
    @DisplayName("Extract balanced pot from Pot[] ->  bp=Pot[] origin=Pot[]")
    fun extractBalancedPotFromAnEmptyPotReturnsAnEmptyPot() {
        val pot = buildPot()
        assertThat(pot.extractBalancedPot()).isEmpty()
        assertThat(pot).isEmpty()
    }
    @Test
    @DisplayName("Extract balanced pot from Pot[hero=100] -> bp=Pot[hero=100] origin=Pot[]")
    fun extractBalancedPotFromAPotContainingASinglePlayerReturnsAPotWithSameDataAndOriginalOneEmpty() {
        val hero = anInGamePlayer("hero")
        val pot = buildPot()
        pot[hero] = 100
        assertThat(pot.extractBalancedPot()).containsOnly(hero to 100)
        assertThat(pot).isEmpty()
    }
    @Test
    @DisplayName("Extract balanced pot from Pot[Alex=100,Giulia=100,Jane=100] -> " +
            "bp=Pot[Alex=100,Giulia=100,Jane=100] origin=Pot[]")
    fun extractBalancedPotFromABalancedPotReturnsAPotWithSameDataAndOriginalOneEmpty() {
        val alex = anInGamePlayer("Alex")
        val giulia = anInGamePlayer("Giulia")
        val jane = anInGamePlayer("Jane")
        val pot = buildPot()
        pot[alex] = 100
        pot[giulia] = 100
        pot[jane] = 100
        assertThat(pot.extractBalancedPot()).containsOnly(alex to 100,jane to 100, giulia to 100)
        assertThat(pot).isEmpty()
    }
    @Test
    @DisplayName("Extract balanced pot from Pot[Alex=100,Giulia=200] -> " +
            "               bp=Pot[Alex=100,Giulia=100] origin=Pot[Giulia=100]")
    fun extractBalancedPotFromAPotContainingTwoPlayersWithDifferentValuesReturnsBalancedPotAndRemoveFromOriginalPot() {
        val alex = anInGamePlayer("Alex")
        val giulia = anInGamePlayer("Giulia")
        val pot = buildPot()
        pot[alex] = 100
        pot[giulia] = 200
        assertThat(pot.extractBalancedPot()).containsOnly(alex to 100, giulia to 100)
        assertThat(pot).containsOnly(giulia to 100)
    }

    @Test
    @DisplayName("Decompose Pot[] -> []")
    fun decomposeAnEmptyPotReturnsAnEmptyCollection() {
        val pot = buildPot()
        assertThat(pot.decompose()).isEmpty()
        assertThat(pot).isEmpty()
    }
    @Test
    @DisplayName("Decompose Pot[Alex=100] -> [Pot[Alex=100]]")
    fun decomposeAPotWithOnlyAPlayerReturnsACollectionWithOnePot() {
        val alex = anInGamePlayer("Alex")
        val pot = PotBuilder().contribution(alex,100).build()
        assertThat(pot.decompose()).containsOnly(
                PotBuilder().contribution(alex,100).build()
        )
        assertThat(pot).isEmpty()
    }
    @Test
    @DisplayName("Decompose Pot[Alex=100,Jane=150,Giulia=210,Sasha=210] -> " +
            "[Pot[Alex=100,Jane=100,Giulia=100,Sasha=100]," +
            " Pot[Jane=50,Giulia=50,Sasha=50]," +
            " Pot[Giulia=60,Sasha=60]]")
    fun decomposeAPotReturnsACollectionOfAllBalancedPotLeavingThePotEmpty() {
        val alex = anInGamePlayer("Alex")
        val jane = anInGamePlayer("Jane")
        val giulia = anInGamePlayer("Giulia")
        val sasha = anInGamePlayer("Sasha")
        val pot = PotBuilder()
                .contribution(alex,100)
                .contribution(jane,150)
                .contribution(giulia,210)
                .contribution(sasha,210)
                .build()
        assertThat(pot.decompose()).containsOnly(
                PotBuilder()
                        .contribution(alex,100)
                        .contribution(jane,100)
                        .contribution(giulia,100)
                        .contribution(sasha,100)
                        .build(),
                PotBuilder()
                        .contribution(jane,50)
                        .contribution(giulia,50)
                        .contribution(sasha,50)
                        .build(),
                PotBuilder()
                        .contribution(giulia,60)
                        .contribution(sasha,60)
                        .build()
        )
        assertThat(pot).isEmpty()
    }

    @Test
    @DisplayName("Max Contribution from Pot[] -> null")
    fun maxContributionFromAnEmptyPotReturnsNull() {
        assertThat(buildPot().maxContribution()).isNull()
    }
    @Test
    @DisplayName("Max Contribution from Pot[Alex=100] -> {player=Alex,amount=100}")
    fun maxContributionFromASinglePlayerPotReturnsPlayerContribution() {
        val alex = anInGamePlayer("Alex")
        val pot = PotBuilder().contribution(alex,100).build()
        val max = pot.maxContribution()
        assertThat(max).isNotNull()
        assertThat(max?.player).isEqualTo(alex)
        assertThat(max?.amount).isEqualTo(100)
    }
    @Test
    @DisplayName("Max Contribution from Pot[Alex=100,Giulia=150,Jane=90] -> {player=Giulia,amount=150}")
    fun maxContributionReturnsMaxContributionByAmountFromAllPlayerInPot() {
        val alex = anInGamePlayer("Alex")
        val giulia = anInGamePlayer("Giulia")
        val jane = anInGamePlayer("Jane")
        val pot = PotBuilder()
                .contribution(alex,100)
                .contribution(giulia,150)
                .contribution(jane,90)
                .build()
        val max = pot.maxContribution()
        assertThat(max).isNotNull()
        assertThat(max?.player).isEqualTo(giulia)
        assertThat(max?.amount).isEqualTo(150)
    }

    @Test
    @DisplayName("An empty Pot receives an amount of 0 from a player -> the Pot is still empty")
    fun potIgnoresReceivedAmountIfZero() {
        val pot = buildPot()
        pot.receiveFrom(anInGamePlayer(), 0)
        assertThat(pot).isEmpty()
    }
}

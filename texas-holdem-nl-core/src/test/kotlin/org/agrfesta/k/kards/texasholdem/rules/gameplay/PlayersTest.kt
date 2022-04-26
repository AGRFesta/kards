package org.agrfesta.k.kards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.ALL_IN
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.CALL
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.NONE
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.RAISE
import org.agrfesta.k.kards.texasholdem.testing.mothers.CircularStrategy.Companion.aStrategy
import org.agrfesta.k.kards.texasholdem.testing.mothers.aGameContext
import org.agrfesta.k.kards.texasholdem.testing.mothers.aSittingPlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.allInPlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.anInGamePlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.callingPlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.card
import org.agrfesta.k.kards.texasholdem.testing.mothers.foldedPlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.raisingPlayer
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Players tests")
class PlayersTest {

    @Test
    @DisplayName("Player's status is FOLD -> player has fold")
    fun ifPlayerStatusIsFoldHasFoldedIsTrue() {
        assertThat( foldedPlayer.hasFolded() ).isTrue()
    }
    @Test
    @DisplayName("Player's status is CALL -> player has not fold")
    fun ifPlayerStatusIsCallHasFoldedIsFalse() {
        val player = anInGamePlayer(status = CALL)

        assertThat(player.hasFolded()).isFalse()
    }

    @Test
    @DisplayName("Player's status is FOLD -> player can't take part to the game")
    fun ifPlayerStatusIsFoldIsNotActive() {
        assertThat( foldedPlayer.isActive() ).isFalse()
    }
    @Test
    @DisplayName("Player's status is ALL-IN -> player can't take part to the game")
    fun ifPlayerStatusIsAllInIsNotActive() {
        assertThat( allInPlayer.isActive() ).isFalse()
    }
    @Test
    @DisplayName("Player's status is RAISE -> player can take part to the game")
    fun ifPlayerStatusIsRaiseIsActive() {
        val player = anInGamePlayer(status = RAISE)

        assertThat(player.isActive()).isTrue()
    }

    @Test
    @DisplayName("Player with a stack of 1000 receive 200 -> player have a stack of 1200")
    fun playerReceiveAPositiveAmount() {
        val player = anInGamePlayer(stack = 1000u)

        player.receive(200u)

        assertThat(player.stack).isEqualTo(1200u)
    }
    @Test
    @DisplayName("Player with a stack of 1000 receive 0 -> player have a stack of 1000")
    fun playerReceiveAZeroAmount() {
        val player = anInGamePlayer(stack = 1000u)

        player.receive(0u)

        assertThat(player.stack).isEqualTo(1000u)
    }
    @Test
    @DisplayName("Player created with no cards -> raise and Exception")
    fun playerCreatedWithNoCardsRaisesAnException() {
        val failure = assertThat {
            InGamePlayerImpl(sittingPlayer = aSittingPlayer(), cards = emptySet())
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Must hold two cards, received 0")
    }
    @Test
    @DisplayName("Player created with one card -> raise and Exception")
    fun playerCreatedWithOneCardRaisesAnException() {
        val failure = assertThat {
            InGamePlayerImpl(sittingPlayer = aSittingPlayer(), cards = setOf( card("7h") ))
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Must hold two cards, received 1")
    }
    @Test
    @DisplayName("Player created with three cards -> raise and Exception")
    fun playerCreatedWithThreeCardsRaisesAnException() {
        val failure = assertThat {
            InGamePlayerImpl(sittingPlayer = aSittingPlayer(), cards = setOf( card("7h"), card("Ah"), card("7s") ))
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Must hold two cards, received 3")
    }

    @Test
    @DisplayName("""Player with a stack of 200 pays 500 -> effective payment is 200 player have a stack of 0 and is 
        |ALL-IN""")
    fun playerPayAPositiveAmountGreaterThanStack() {
        val player = anInGamePlayer(stack = 200u)
        val payed = player.pay(500u)
        assertThat(player.stack).isEqualTo(0u)
        assertThat(player.status).isEqualTo(ALL_IN)
        assertThat(payed).isEqualTo(200u)
    }
    @Test
    @DisplayName("""Player with a stack of 500 pays 500 -> effective payment is 500 player have a stack of 0 and is 
        |ALL-IN""")
    fun playerPayAPositiveAmountEqualToStack() {
        val player = anInGamePlayer(stack = 500u)
        val payed = player.pay(500u)
        assertThat(player.stack).isEqualTo(0u)
        assertThat(player.status).isEqualTo(ALL_IN)
        assertThat(payed).isEqualTo(500u)
    }
    @Test
    @DisplayName("""Player with a stack of 1000 pays 500 -> effective payment is 500 player have a stack of 500, the 
        |status doesn't change""")
    fun playerPayAPositiveAmountLessThanStack() {
        val player = anInGamePlayer(stack = 1000u, status = NONE)
        assertThat(player.status).isEqualTo(NONE)
        val payed = player.pay(500u)
        assertThat(player.stack).isEqualTo(500u)
        assertThat(player.status).isEqualTo(NONE)
        assertThat(payed).isEqualTo(500u)
    }

    @Test
    @DisplayName("call to Player's act -> the Action from strategy")
    fun actReturnsActionFromStrategy() {
        val strategy = aStrategy( call() )
        val player = anInGamePlayer(strategy = strategy)
        assertThat( player.act(player.asOwnPlayer(), aGameContext()) ).isEqualTo( call() )
    }

    @Test
    @DisplayName("Resetting state of all possible player's states -> only the one not active are reset to NONE")
    fun resetAllActivePlayersState() {
        val players = listOf(
                foldedPlayer,
                allInPlayer,
                callingPlayer,
                raisingPlayer,
                anInGamePlayer(status = NONE)
        )
        assertThat(players).extracting { it.status }
                .containsExactly(FOLD, ALL_IN, CALL, RAISE, NONE)
        players.resetActivePlayersStatus()
        assertThat(players).extracting { it.status }
                .containsExactly(FOLD, ALL_IN, NONE, NONE, NONE)
    }

    @Test
    @DisplayName("Filtering active players from all possible player's states -> return only the one not active")
    fun getActivePlayersOnly() {
        val waitingPlayer = anInGamePlayer(status = NONE)
        val players = listOf(
                foldedPlayer,
                allInPlayer,
                callingPlayer,
                raisingPlayer,
            waitingPlayer
        )
        assertThat(players.getActive())
                .containsExactly(callingPlayer, raisingPlayer, waitingPlayer)
    }

    @Test
    @DisplayName("List of one player -> a winner")
    fun ifTheListHasOnlyOnePlayerReturnsItHasWinner() {
        val player = anInGamePlayer(status = NONE)
        val players = listOf(player)
        assertThat(players.findWinner()).isEqualTo(player)
    }
    @Test
    @DisplayName("Empty list of players -> no winner")
    fun emptyListOfPlayerReturnsNoWinner() {
        val players = listOf<InGamePlayer>()
        assertThat(players.findWinner()).isNull()
    }
    @Test
    @DisplayName("List of two players -> no winner")
    fun aListOfTwoPlayersReturnsNoWinner() {
        val players = listOf(anInGamePlayer(), anInGamePlayer())
        assertThat(players.findWinner()).isNull()
    }
    @Test
    @DisplayName("List of a player not folded and another folded -> a winner (the not folded)")
    fun aListWithAPlayerNotFoldedAndAnotherFoldedReturnsAWinner() {
        val player = anInGamePlayer(status = NONE)
        val players = listOf(player, foldedPlayer)
        assertThat(players.findWinner()).isEqualTo(player)
    }
}

package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.ALL_IN
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.CALL
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.NONE
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.RAISE
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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Players tests")
class PlayersTest {

    @Test
    @DisplayName("Player's status is FOLD -> player has fold")
    fun ifPlayerStatusIsFoldHasFoldedIsTrue() {
        val player = foldedPlayer()
        assertThat(player.hasFolded()).isTrue()
    }
    @Test
    @DisplayName("Player's status is CALL -> player has not fold")
    fun ifPlayerStatusIsCallHasFoldedIsFalse() {
        val player = InGamePlayer(Player("Alex",aStrategy()), 1000, aPlayerCardsSet())
        player.status = CALL
        assertThat(player.hasFolded()).isFalse()
    }

    @Test
    @DisplayName("Player's status is FOLD -> player can't take part to the game")
    fun ifPlayerStatusIsFoldIsNotActive() {
        val player = foldedPlayer()
        assertThat(player.isActive()).isFalse()
    }
    @Test
    @DisplayName("Player's status is ALL-IN -> player can't take part to the game")
    fun ifPlayerStatusIsAllInIsNotActive() {
        val player = allInPlayer()
        assertThat(player.isActive()).isFalse()
    }
    @Test
    @DisplayName("Player's status is RAISE -> player can take part to the game")
    fun ifPlayerStatusIsRaiseIsActive() {
        val player = InGamePlayer(Player("Alex",aStrategy()), 1000, aPlayerCardsSet())
        player.status = RAISE
        assertThat(player.isActive()).isTrue()
    }

    @Test
    @DisplayName("Player with a stack of 1000 receive 200 -> player have a stack of 1200")
    fun playerReceiveAPositiveAmount() {
        val player = InGamePlayer(Player("Alex",aStrategy()), 1000, aPlayerCardsSet())
        player.receive(200)
        assertThat(player.stack).isEqualTo(1200)
    }
    @Test
    @DisplayName("Player with a stack of 1000 receive 0 -> player have a stack of 1000")
    fun playerReceiveAZeroAmount() {
        val player = InGamePlayer(Player("Alex",aStrategy()), 1000, aPlayerCardsSet())
        player.receive(0)
        assertThat(player.stack).isEqualTo(1000)
    }
    @Test
    @DisplayName("Player receives -200 -> raise and Exception")
    fun playerReceiveANegativeAmountRaisesAnException() {
        val player = InGamePlayer(Player("Alex",aStrategy()), 1000, aPlayerCardsSet())

        val failure = assertThat {
            player.receive(-200)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Can't receive a negative amount")
    }

    @Test
    @DisplayName("Player with a stack of 200 pays 500 -> effective payment is 200 player have a stack of 0 and is " +
            "ALL-IN")
    fun playerPayAPositiveAmountGreaterThanStack() {
        val player = InGamePlayer(Player("Alex",aStrategy()), 200, aPlayerCardsSet())
        val payed = player.pay(500)
        assertThat(player.stack).isEqualTo(0)
        assertThat(player.status).isEqualTo(ALL_IN)
        assertThat(payed).isEqualTo(200)
    }
    @Test
    @DisplayName("Player with a stack of 500 pays 500 -> effective payment is 500 player have a stack of 0 and is " +
            "ALL-IN")
    fun playerPayAPositiveAmountEqualToStack() {
        val player = InGamePlayer(Player("Alex",aStrategy()), 500, aPlayerCardsSet())
        val payed = player.pay(500)
        assertThat(player.stack).isEqualTo(0)
        assertThat(player.status).isEqualTo(ALL_IN)
        assertThat(payed).isEqualTo(500)
    }
    @Test
    @DisplayName("Player with a stack of 1000 pays 500 -> effective payment is 500 player have a stack of 500, the " +
            "status doesn't change")
    fun playerPayAPositiveAmountLessThanStack() {
        val player = InGamePlayer(Player("Alex",aStrategy()), 1000, aPlayerCardsSet())
        assertThat(player.status).isEqualTo(NONE)
        val payed = player.pay(500)
        assertThat(player.stack).isEqualTo(500)
        assertThat(player.status).isEqualTo(NONE)
        assertThat(payed).isEqualTo(500)
    }
    @Test
    @DisplayName("Player pays -200 -> raise and Exception")
    fun playerPaysANegativeAmountRaisesAnException() {
        val player = InGamePlayer(Player("Alex",aStrategy()), 1000, aPlayerCardsSet())

        val failure = assertThat {
            player.pay(-200)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Can't pay a negative amount")
    }

    @Test
    @DisplayName("call to Player's act -> the Action from strategy")
    fun actReturnsActionFromStrategy() {
        val action = object : Action {
            override fun getAmount(): Int? = 100
            override fun getType() = ActionType.Call
        }
        val strategy = object : PlayerStrategyInterface {
            override fun act(context: PlayerGameContext): Action = action
        }
        val player = InGamePlayer(Player("Alex",strategy), 1000, aPlayerCardsSet())
        assertThat(player.act(aPlayerContext())).isEqualTo(action)
    }

    @Test
    @DisplayName("Resetting state of all possible player's states -> only the one not active are reset to NONE")
    fun resetAllActivePlayersState() {
        val players = listOf(
                foldedPlayer(),
                allInPlayer(),
                callingPlayer(),
                raisingPlayer(),
                anInGamePlayer()
        )
        assertThat(players).extracting { it.status }
                .containsExactly(FOLD,ALL_IN,CALL,RAISE,NONE)
        players.resetActivePlayersStatus()
        assertThat(players).extracting { it.status }
                .containsExactly(FOLD,ALL_IN,NONE,NONE,NONE)
    }

    @Test
    @DisplayName("Filtering active players from all possible player's states -> return only the one not active")
    fun getActivePlayersOnly() {
        val players = listOf(
                foldedPlayer(),
                allInPlayer(),
                callingPlayer(),
                raisingPlayer(),
                anInGamePlayer()
        )
        assertThat(players).extracting { it.name }
                .containsExactly("FoldedPlayer","AllInPlayer","CallingPlayer","RaisingPlayer","APlayer")
        assertThat(players.getActive()).extracting { it.name }
                .containsExactly("CallingPlayer","RaisingPlayer","APlayer")
    }

    @Test
    @DisplayName("List of one player -> a winner")
    fun ifTheListHasOnlyOnePlayerReturnsItHasWinner() {
        val player = anInGamePlayer()
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
        val players = listOf(anInGamePlayer(),anInGamePlayer())
        assertThat(players.findWinner()).isNull()
    }
    @Test
    @DisplayName("List of a player not folded and another folded -> a winner (the not folded)")
    fun aListWithAPlayerNotFoldedAndAnotherFoldedReturnsAWinner() {
        val player = anInGamePlayer()
        val players = listOf(player,foldedPlayer())
        assertThat(players.findWinner()).isEqualTo(player)
    }
}

package agrfesta.k.cards.texasholdem.rules.gameplay.utils

import agrfesta.k.cards.texasholdem.rules.gameplay.BoardInSequence
import agrfesta.k.cards.texasholdem.rules.gameplay.Dealer
import agrfesta.k.cards.texasholdem.rules.gameplay.GameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.CALL
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import agrfesta.k.cards.texasholdem.rules.gameplay.Pot
import agrfesta.k.cards.texasholdem.rules.gameplay.receiveFrom
import io.mockk.mockk

typealias BuilderEnrich = (TestPotBuilder) -> TestPotBuilder

/**
 * Fluent Builder for test Pot created following a game logic
 */
class TestPotBuilder(private val gameContext: GameContext<InGamePlayer, BoardInSequence>) {
    private val pot = mutableMapOf<InGamePlayer,Int>()

    fun receiveFoldFrom(player: Player): TestPotBuilder {
        val inGamePlayer = getPlayer(player)
        inGamePlayer.status = FOLD
        return this
    }

    fun receiveCallFrom(player: Player, value: Int): TestPotBuilder {
        val inGamePlayer = getPlayer(player)
        inGamePlayer.status = CALL
        pot.receiveFrom(inGamePlayer, value)
        return this
    }

    fun receiveRaiseFrom(player: Player, value: Int): TestPotBuilder {
        val inGamePlayer = getPlayer(player)
        inGamePlayer.status = PlayerStatus.RAISE
        pot.receiveFrom(inGamePlayer, value)
        return this
    }

    fun build(): Pot = pot

    private fun getPlayer(player: Player): InGamePlayer = gameContext.getPlayer(player)

}

fun GameContext<InGamePlayer, BoardInSequence>.getPlayer(player: Player): InGamePlayer {
    val inGamePlayer = this.table.findPlayerBySeatName(player.name)
    requireNotNull(inGamePlayer)
    return inGamePlayer
}

fun Pot.getPlayer(player: Player): InGamePlayer {
    val result = this.keys.firstOrNull { it.player == player }
    requireNotNull(result)
    return result
}

fun dealerMockFromBuilder(context: GameContext<InGamePlayer, BoardInSequence>, enrich: BuilderEnrich): Dealer {
    val dealer = mockk<Dealer>()
    val pot =  enrich.invoke(TestPotBuilder(context)).build()
    //every { dealer.collectPot() } returns pot
    return dealer
}

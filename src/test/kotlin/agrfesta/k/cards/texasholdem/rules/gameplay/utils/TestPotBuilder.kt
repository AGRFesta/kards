package agrfesta.k.cards.texasholdem.rules.gameplay.utils

import agrfesta.k.cards.texasholdem.rules.gameplay.Dealer
import agrfesta.k.cards.texasholdem.rules.gameplay.InGameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePot
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.CALL
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import agrfesta.k.cards.texasholdem.rules.gameplay.receiveFrom
import io.mockk.mockk

typealias BuilderEnrich = (TestPotBuilder) -> TestPotBuilder

/**
 * Fluent Builder for test Pot created following a game logic
 */
class TestPotBuilder(private val gameContext: InGameContext) {
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

    fun build(): InGamePot = pot

    private fun getPlayer(player: Player): InGamePlayer = gameContext.getPlayer(player)

}

fun InGameContext.getPlayer(player: Player): InGamePlayer {
    val inGamePlayer = table.findPlayerBySeatName(player.name)
    requireNotNull(inGamePlayer)
    return inGamePlayer
}

fun InGamePot.getPlayer(player: Player): InGamePlayer {
    val result = keys.firstOrNull { it.player == player }
    requireNotNull(result)
    return result
}

fun dealerMockFromBuilder(context: InGameContext, enrich: BuilderEnrich): Dealer {
    val dealer = mockk<Dealer>()
    enrich.invoke(TestPotBuilder(context)).build()
    return dealer
}

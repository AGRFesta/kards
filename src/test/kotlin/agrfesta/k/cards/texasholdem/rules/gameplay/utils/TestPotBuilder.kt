package agrfesta.k.cards.texasholdem.rules.gameplay.utils

import agrfesta.k.cards.texasholdem.rules.gameplay.GamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.CALL
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import agrfesta.k.cards.texasholdem.rules.gameplay.receiveFrom

/**
 * Fluent Builder for test Pot created following a game logic
 */
class TestPotBuilder {
    private val pot = mutableMapOf<GamePlayer,Int>()

    fun receiveFoldFrom(player: GamePlayer): TestPotBuilder {
        player.status = FOLD
        return this
    }

    fun receiveCallFrom(player: GamePlayer, value: Int): TestPotBuilder {
        player.status = CALL
        pot.receiveFrom(player, value)
        return this
    }

    fun receiveRaiseFrom(player: GamePlayer, value: Int): TestPotBuilder {
        player.status = PlayerStatus.RAISE
        pot.receiveFrom(player, value)
        return this
    }

    fun build(): MutableMap<GamePlayer, Int> = pot

}

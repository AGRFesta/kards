package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.GameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack

interface GameObserver: ShowdownObserver, DealerObserver {
    fun notifyWinner(result: GameResult) {}
    fun notifyStartingPhase(context: GameContext<InGamePlayer>) {}
}

class GameResult(val winner: Player, val prize: Int, val players: List<PlayerStack>)

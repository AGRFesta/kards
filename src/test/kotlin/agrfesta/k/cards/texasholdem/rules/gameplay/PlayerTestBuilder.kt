package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card

class PlayerTestBuilder {
    private var player: Player = Player("aPlayer")
    private var stack: Int = 1000
    private var strategyProvider: (p: GamePlayer) -> PlayerStrategyInterface = { aStrategy() }
    private var status: PlayerStatus = PlayerStatus.NONE
    private var cards: Set<Card> = setOf()

    fun player(player: Player): PlayerTestBuilder {
        this.player = player
        return this
    }
    fun stack(stack: Int): PlayerTestBuilder {
        this.stack = stack
        return this
    }
    fun strategy(strategyProvider: (p: GamePlayer) -> PlayerStrategyInterface): PlayerTestBuilder {
        this.strategyProvider = strategyProvider
        return this
    }
    fun status(status: PlayerStatus): PlayerTestBuilder {
        this.status = status
        return this
    }
    fun cards(cards: Set<Card>): PlayerTestBuilder {
        this.cards = cards
        return this
    }

    fun build(): GamePlayer {
        val player = GamePlayer(player, stack, strategyProvider)
        player.status = this.status
        player.cards = this.cards
        return player
    }

}
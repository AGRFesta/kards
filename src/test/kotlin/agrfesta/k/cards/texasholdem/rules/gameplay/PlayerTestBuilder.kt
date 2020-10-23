package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card

class PlayerTestBuilder {
    private var player: Player = Player("aPlayer", aStrategy())
    private var stack: Int = 1000
    private var status: PlayerStatus = PlayerStatus.NONE
    private var cards: Set<Card> = aPlayerCardsSet()

    fun player(player: Player): PlayerTestBuilder {
        this.player = player
        return this
    }
    fun stack(stack: Int): PlayerTestBuilder {
        this.stack = stack
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

    fun build(): InGamePlayer {
        val player = InGamePlayer(player, stack, this.cards)
        player.status = this.status
        return player
    }

}

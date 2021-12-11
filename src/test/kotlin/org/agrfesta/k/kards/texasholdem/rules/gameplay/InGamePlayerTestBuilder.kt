package org.agrfesta.k.kards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card

class InGamePlayerTestBuilder private constructor() {
    private var player = aPlayer()
    private var stack: Int = 1000
    private var status: PlayerStatus = PlayerStatus.NONE
    private var cards: Set<Card> = aPlayerCardsSet()

    companion object {
        fun buildingAnInGamePlayer() = InGamePlayerTestBuilder()
    }

    fun forPlayer(player: Player): InGamePlayerTestBuilder {
        this.player = player
        return this
    }
    fun withAStackOf(stack: Int): InGamePlayerTestBuilder {
        this.stack = stack
        return this
    }
    fun inStatus(status: PlayerStatus): InGamePlayerTestBuilder {
        this.status = status
        return this
    }
    fun withCards(cards: Set<Card>): InGamePlayerTestBuilder {
        this.cards = cards
        return this
    }

    fun build(): InGamePlayer {
        val player = InGamePlayer(player, stack, cards)
        player.status = status
        return player
    }

}

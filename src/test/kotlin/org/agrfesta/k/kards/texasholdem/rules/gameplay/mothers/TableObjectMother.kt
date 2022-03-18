package org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers

import agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStrategyInterface
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Table
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aPlayerCardsSet
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aStrategy

private const val BUTTON_POS = 0
private const val SMALL_BLIND_POS = 1
private const val BIG_BLIND_POS = 2
private const val UNDER_THE_GUN_POS = 3
private const val MIDDLE_POS = 4
private const val LATE_POS = 5

const val BUTTON = "Button"
const val SMALL_BLIND = "Small Blind"
const val BIG_BLIND = "Big Blind"
const val UNDER_THE_GUN = "UTG"
const val MIDDLE = "Middle"
const val LATE = "Late"

class TestTableBuilder {
    val players: MutableMap<Int, InGamePlayer> = mutableMapOf()

    fun button(stack: Int = 100, cards: Set<Card> = aPlayerCardsSet(),
               strategy: PlayerStrategyInterface = aStrategy()): TestTableBuilder {
        players[BUTTON_POS] = InGamePlayer(Player(BUTTON, strategy), stack, cards)
        return this
    }
    fun smallBlind(stack: Int = 100, cards: Set<Card> = aPlayerCardsSet(),
                   strategy: PlayerStrategyInterface = aStrategy()): TestTableBuilder {
        players[SMALL_BLIND_POS] = InGamePlayer(Player(SMALL_BLIND, strategy), stack, cards)
        return this
    }
    fun bigBlind(stack: Int = 100, cards: Set<Card> = aPlayerCardsSet(),
                 strategy: PlayerStrategyInterface = aStrategy()): TestTableBuilder {
        players[BIG_BLIND_POS] = InGamePlayer(Player(BIG_BLIND, strategy), stack, cards)
        return this
    }
    fun underTheGun(stack: Int = 100, cards: Set<Card> = aPlayerCardsSet(),
                    strategy: PlayerStrategyInterface = aStrategy()): TestTableBuilder {
        players[UNDER_THE_GUN_POS] = InGamePlayer(Player(UNDER_THE_GUN, strategy), stack, cards)
        return this
    }
    fun middle(stack: Int = 100, cards: Set<Card> = aPlayerCardsSet(),
                    strategy: PlayerStrategyInterface = aStrategy()): TestTableBuilder {
        players[MIDDLE_POS] = InGamePlayer(Player(MIDDLE, strategy), stack, cards)
        return this
    }
    fun late(stack: Int = 100, cards: Set<Card> = aPlayerCardsSet(),
                    strategy: PlayerStrategyInterface = aStrategy()): TestTableBuilder {
        players[LATE_POS] = InGamePlayer(Player(LATE, strategy), stack, cards)
        return this
    }

    fun build(): Table<InGamePlayer> = Table(
        players = players.entries
            .sortedBy { it.key }
            .map { it.value },
        button = 0)

}

fun buildTestTable(
    setup: TestTableBuilder.() -> Unit
): Table<InGamePlayer> {
    val builder = TestTableBuilder()
    builder.setup()
    return builder.build()
}

private fun Table<InGamePlayer>.getPlayerBySeatName(player: String) =
    findPlayerBySeatName(player) ?: throw MissingPlayerException(player)

fun Table<InGamePlayer>.middle() = getPlayerBySeatName(MIDDLE)
fun Table<InGamePlayer>.late() = getPlayerBySeatName(LATE)
fun Table<InGamePlayer>.underTheGun() = getPlayerBySeatName(UNDER_THE_GUN)
fun Table<InGamePlayer>.button() = getPlayerBySeatName(BUTTON)
fun Table<InGamePlayer>.smallBlind() = getPlayerBySeatName(SMALL_BLIND)
fun Table<InGamePlayer>.bigBlind() = getPlayerBySeatName(BIG_BLIND)

class MissingPlayerException(val player: String): Exception() {
    override val message: String
        get() = "$player is missing"
}

package agrfesta.k.cards.texasholdem.rules.gameplay.mothers

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStrategyInterface
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import agrfesta.k.cards.texasholdem.rules.gameplay.aPlayerCardsSet
import agrfesta.k.cards.texasholdem.rules.gameplay.aStrategy
import agrfesta.k.cards.texasholdem.rules.gameplay.buildTable

private const val BUTTON_POS = 0
private const val SMALL_BLIND_POS = 1
private const val BIG_BLIND_POS = 2
private const val UNDER_THE_GUN_POS = 3

const val BUTTON = "Button"
const val SMALL_BLIND = "Small Blind"
const val BIG_BLIND = "Big Blind"
const val UNDER_THE_GUN = "UTG"

class TestTableBuilder {
    val players: MutableMap<Int, InGamePlayer> = mutableMapOf()

    fun button(stack: Int = 100, strategy: PlayerStrategyInterface = aStrategy(),
               cards: Set<Card> = aPlayerCardsSet()): TestTableBuilder {
        players[BUTTON_POS] = InGamePlayer(Player(BUTTON, strategy), stack, cards)
        return this
    }
    fun smallBlind(stack: Int = 100, strategy: PlayerStrategyInterface = aStrategy(),
                   cards: Set<Card> = aPlayerCardsSet()): TestTableBuilder {
        players[SMALL_BLIND_POS] = InGamePlayer(Player(SMALL_BLIND, strategy), stack, cards)
        return this
    }
    fun bigBlind(stack: Int = 100, strategy: PlayerStrategyInterface = aStrategy(),
                 cards: Set<Card> = aPlayerCardsSet()): TestTableBuilder {
        players[BIG_BLIND_POS] = InGamePlayer(Player(BIG_BLIND, strategy), stack, cards)
        return this
    }
    fun underTheGun(stack: Int = 100, cards: Set<Card> = aPlayerCardsSet(),
                    strategy: PlayerStrategyInterface = aStrategy()): TestTableBuilder {
        players[UNDER_THE_GUN_POS] = InGamePlayer(Player(UNDER_THE_GUN, strategy), stack, cards)
        return this
    }

    fun build(): Table<InGamePlayer> = buildTable {
            withPlayers(
                players.entries
                    .sortedBy { it.key }
                    .map { it.value }
            )
            withButtonInPosition(0)
        }

}

fun buildTestTable(
    setup: TestTableBuilder.() -> Unit
): Table<InGamePlayer> {
    val builder = TestTableBuilder()
    builder.setup()
    return builder.build()
}

fun Table<InGamePlayer>.underTheGun(): InGamePlayer {
    return findPlayerBySeatName(UNDER_THE_GUN) ?: throw MissingPlayerException(UNDER_THE_GUN)
}

fun Table<InGamePlayer>.button(): InGamePlayer {
    return findPlayerBySeatName(BUTTON) ?: throw MissingPlayerException(BUTTON)
}

fun Table<InGamePlayer>.smallBlind(): InGamePlayer {
    return findPlayerBySeatName(SMALL_BLIND) ?: throw MissingPlayerException(SMALL_BLIND)
}

fun Table<InGamePlayer>.bigBlind(): InGamePlayer {
    return findPlayerBySeatName(BIG_BLIND) ?: throw MissingPlayerException(BIG_BLIND)
}

class MissingPlayerException(val player: String): Exception() {
    override val message: String
        get() = "$player is missing"
}

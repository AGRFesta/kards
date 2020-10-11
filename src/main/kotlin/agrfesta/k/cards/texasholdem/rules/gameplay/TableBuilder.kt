package agrfesta.k.cards.texasholdem.rules.gameplay

class TableBuilder {
    private val players: MutableList<InGamePlayer> = mutableListOf()
    private var button: Int = 0

    fun withPlayer(playerName: String, stack: Int, strategy: PlayerStrategyInterface)
            : TableBuilder {
        require(!players.contains(playerName)) { "Player '$playerName' is already sitting at the table!" }
        players.add(InGamePlayer(Player(playerName,strategy), stack))
        return this
    }
    fun buttonStartsFrom(position: Int): TableBuilder {
        this.button = position
        return this
    }

    fun build(): Table<InGamePlayer> = Table(players, button)

}

private fun MutableList<InGamePlayer>.contains(player: String) = this.any { it.player.name == player }

fun buildingTable() = TableBuilder()

package agrfesta.k.cards.texasholdem.rules.gameplay


    fun aStrategy(): PlayerStrategyInterface = object : PlayerStrategyInterface {
        override fun act(): Action = anAction()
    }
    fun anAction(): Action = object : Action {
        override fun getAmount(): Int? = null
    }
    fun aPlayer() = Player("APlayer", 1000) { aStrategy() }

    fun foldedPlayer(): Player {
        val player = Player("FoldedPlayer", 1000) { aStrategy() }
        player.status = PlayerStatus.FOLD
        return player
    }
    fun allInPlayer(): Player {
        val player = Player("AllInPlayer", 1000) { aStrategy() }
        player.status = PlayerStatus.ALL_IN
        return player
    }
    fun callingPlayer(): Player {
        val player = Player("CallingPlayer", 1000) { aStrategy() }
        player.status = PlayerStatus.CALL
        return player
    }
    fun raisingPlayer(): Player {
        val player = Player("RaisingPlayer", 1000) { aStrategy() }
        player.status = PlayerStatus.RAISE
        return player
    }

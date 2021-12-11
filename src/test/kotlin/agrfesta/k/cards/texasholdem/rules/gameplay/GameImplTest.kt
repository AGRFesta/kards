package agrfesta.k.cards.texasholdem.rules.gameplay

class GameImplTest {

//    @Test
//    @DisplayName("play(): playing a game -> all players actions are notified to the game's observer")
//    fun playTest000() {
//        val playerA = Player( "Alex", strategyMock(call()) )
//        val playerP = Player( "Poly", strategyMock(call()) )
//        val gameObserverMock = mockk<GameObserver>(relaxed = true)
//        val table: Table<PlayerStack> = aTableWith(playerA, playerP)
//            .map { PlayerStack(it.player, 1000) }
//        val game = buildingAGame()
//            .withPayments(10, 20)
//            .withTable(table)
//            .observedBy(gameObserverMock)
//            .implementedBy(::GameImpl)
//            .build()
//
//        game.play()
//
//        val phases = mutableListOf<GamePhase>()
//        verify { gameObserverMock.notifyActions(capture(phases), any()) }
//        assertThat(phases).containsExactly(
//            GamePhase.PRE_FLOP, GamePhase.FLOP, GamePhase.TURN, GamePhase.RIVER
//        )
//        val actions = mutableListOf<PlayerAction>()
//        verify { gameObserverMock.notifyAction(any(), capture(actions)) }
//        assertThat(actions).containsExactly(
//            PlayerAction("Alex", call()),
//            PlayerAction("Poly", call()),
//            PlayerAction("Alex", call()),
//            PlayerAction("Poly", call()),
//            PlayerAction("Alex", call()),
//            PlayerAction("Poly", call()),
//            PlayerAction("Alex", call()),
//            PlayerAction("Poly", call())
//        )
//    }

//    @Test
//    @DisplayName("play(): playing a game that ends at pre-flop -> observer notified with pre-flop and winner")
//    fun playTest001() {
//        val playerA = Player( "Alex", strategyMock(fold()) )
//        val playerP = Player( "Poly", strategyMock(call()) )
//        val gameObserverMock = mockk<GameObserver>(relaxed = true)
//        val table: Table<PlayerStack> = aTableWith(playerA, playerP)
//            .map { PlayerStack(it.player, 1000) }
//        val game = buildingAGame()
//            .withPayments(10, 20)
//            .withTable(table)
//            .observedBy(gameObserverMock)
//            .implementedBy(::GameImpl)
//            .build()
//
//        game.play()
//
//        verify(exactly = 1) { observerMock.notifyStartingPhase(any()) }
//        verify(exactly = 1) { observerMock.notifyWinner(any()) }
//        verify(exactly = 0) { observerMock.notifyResult(any()) } // No showdown
//
//        val phases = mutableListOf<GamePhase>()
//        verify { gameObserverMock.notifyActions(capture(phases), any()) }
//        assertThat(phases).containsExactly(
//            GamePhase.PRE_FLOP
//        )
//        val actions = mutableListOf<PlayerAction>()
//        verify { gameObserverMock.notifyAction(any(), capture(actions)) }
//        assertThat(actions).containsExactly(
//            PlayerAction("Alex", call()),
//            PlayerAction("Poly", fold())
//        )
//    }

}


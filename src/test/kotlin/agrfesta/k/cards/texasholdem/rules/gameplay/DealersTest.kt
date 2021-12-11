package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.DealerObserver
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.ALL_IN
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.CALL
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.NONE
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.RAISE
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.BIG_BLIND
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.BUTTON
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.LATE
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.MIDDLE
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.SMALL_BLIND
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.UNDER_THE_GUN
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.bigBlind
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.buildTestTable
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.button
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.smallBlind
import agrfesta.k.cards.texasholdem.rules.gameplay.mothers.underTheGun
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Dealers tests")
class DealersTest {

    @Test
    @DisplayName("Post flop (10/20) story: Alex calls, Jane raises 100, Alex folds")
    fun postFlopStory000() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(call(), fold()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(raise(100)))
        val table = Table(listOf(alex, jane), 0)
        val context = aContext(table, blinds(10, 20))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(0)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(100)
        assertThat(alex.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(2000)
        assertThat(jane.stack).isEqualTo(1900)
    }

    @Test
    @DisplayName("""collectPot(): Pre flop (10/20) Alex calls, Jane raises 100, Alex raises 200, Jane calls -> 
        Alex paid 200 and Jane 200
    """)
    fun collectPotTest004() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(call(), raise(200)))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(raise(100), call()))
        val table = Table(listOf(alex, jane), 0)
        val context = aContext(table, blinds(10, 20))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(200)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(200)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1800)
        assertThat(jane.stack).isEqualTo(1800)
    }

    @Test
    @DisplayName("Post flop (10/20) story: Alex calls, Jane raises Int.MAX_VALUE, Alex calls")
    fun postFlopStory003() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(call(), call()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(raise(Int.MAX_VALUE)))
        val table = Table(listOf(alex, jane), 0)
        val context = aContext(table, blinds(10, 20))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(2000)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(2000)
        assertThat(alex.status).isEqualTo(ALL_IN)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(alex.stack).isEqualTo(0)
        assertThat(jane.stack).isEqualTo(0)
    }

    @Test
    @DisplayName("Post flop (10/20) story: Alex raises 175, Jane calls, Dave all-in, Alex calls, Jane calls")
    fun postFlopStory004() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(raise(175), call()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(call(), call()))
        val dave = anInGamePlayer("Dave", 200, strategyMock(raise(200)))
        val table = Table(listOf(alex, jane, dave), 2)
        val context = aContext(table, blinds(10, 20))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(200)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(200)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(200)
        assertThat(alex.status).isEqualTo(CALL)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(dave.status).isEqualTo(ALL_IN)
        assertThat(alex.stack).isEqualTo(1800)
        assertThat(jane.stack).isEqualTo(1800)
        assertThat(dave.stack).isEqualTo(0)
    }

    @Test
    @DisplayName("""collectPot(): Pre flop (25/50) Alex raises 175, Jane all-in, Dave raises 200, Alex raises 200, 
        Dave calls -> Alex paid 200 and Jane 200
    """)
    fun collectPotTest005() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(raise(175), raise(400)))
        val jane = anInGamePlayer("Jane", 200, strategyMock(raise(200)))
        val dave = anInGamePlayer("Dave", 2000, strategyMock(raise(300), call()))
        val table = Table(listOf(alex, jane, dave), 2)
        val context = aContext(table, blinds(25, 50))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(575)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(200)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(575)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1425)
        assertThat(jane.stack).isEqualTo(0)
        assertThat(dave.stack).isEqualTo(1425)
    }

    @Test
    @DisplayName("Post flop (110/220) story: Dave folds")
    fun postFlopStory008() {
        val alex = anInGamePlayer("Alex", 0, ALL_IN, strategyMock(call()))
        val juno = anInGamePlayer("Juno", 20, FOLD, strategyMock(call()))
        val dave = anInGamePlayer("Dave", 2000, CALL, strategyMock(fold()))
        val jane = anInGamePlayer("Jane", 2000, CALL, strategyMock(fold()))
        val eric = anInGamePlayer("Eric", 0, ALL_IN, strategyMock(call()))
        val sara = anInGamePlayer("Sara", 8880, FOLD, strategyMock(call()))

        val table = Table(listOf(alex, juno, dave, jane, eric, sara), 5)
        val context = aContext(table, blinds(110, 220))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot()).isEmpty()
        assertThat(alex.status).isEqualTo(ALL_IN)
        assertThat(juno.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(NONE)
        assertThat(eric.status).isEqualTo(ALL_IN)
        assertThat(sara.status).isEqualTo(FOLD)
        assertThat(alex.stack).isEqualTo(0)
        assertThat(juno.stack).isEqualTo(20)
        assertThat(dave.stack).isEqualTo(2000)
        assertThat(jane.stack).isEqualTo(2000)
        assertThat(eric.stack).isEqualTo(0)
        assertThat(sara.stack).isEqualTo(8880)
    }

    @Test
    @DisplayName("Post flop (10/20) story: In previous phase both Alex and Jane call 20. " +
            "Alex calls, Jane raises 100, Alex folds")
    fun postFlopStory009() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(call(), fold()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(raise(100)))
        val table = Table(listOf(alex, jane), 0)

        val prevPot = buildMutablePot()
        prevPot.receiveFrom(alex, 20)
        prevPot.receiveFrom(jane, 20)

        val context = aContext(table, blinds(10, 20))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(0)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(100)
        assertThat(alex.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(1980)
        assertThat(jane.stack).isEqualTo(1880)
    }

    @Test
    @DisplayName("Pre flop (50/100) story: Dave calls, Alex calls, Jane calls")
    fun preFlopStory000() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(call()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(call()))
        val dave = anInGamePlayer("Dave", 2000, strategyMock(call()))
        val table = Table(listOf(alex, jane, dave), 2)
        val context = aContext(table, blinds(50, 100))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(100)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(100)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(100)
        assertThat(alex.status).isEqualTo(CALL)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1900)
        assertThat(jane.stack).isEqualTo(1900)
        assertThat(dave.stack).isEqualTo(1900)
    }

    @Test
    @DisplayName("""collectPot(): Pre flop (50/100) Dave calls, Alex raises 200, Jane calls, Dave calls -> 
        Alex paid 250, Jane 250, Dave 250 
    """)
    fun collectPotTest001() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(raise(200)))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(call()))
        val dave = anInGamePlayer("Dave", 2000, strategyMock(call(), call()))
        val table = Table(listOf(alex, jane, dave), 2)
        val context = aContext(table, blinds(50, 100))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(250)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(250)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(250)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1750)
        assertThat(jane.stack).isEqualTo(1750)
        assertThat(dave.stack).isEqualTo(1750)
    }

    @Test
    @DisplayName("""collectPot(): Pre flop (50/100) Dave raises 300, Alex calls, Jane folds -> 
        Alex paid 300, Jane 100, Dave 300 
    """)
    fun collectPotTest002() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(call()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(fold()))
        val dave = anInGamePlayer("Dave", 2000, strategyMock(raise(300)))
        val table = Table(listOf(alex, jane, dave), 2)
        val context = aContext(table, blinds(50, 100))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(300)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(100)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(300)
        assertThat(alex.status).isEqualTo(CALL)
        assertThat(jane.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(1700)
        assertThat(jane.stack).isEqualTo(1900)
        assertThat(dave.stack).isEqualTo(1700)
    }

    @Test
    @DisplayName("""collectPot(): Pre flop (50/100) Dave raises 300, Alex re-raises 600, Dave calls -> 
        Alex paid 650, Jane 50, Dave 650 
    """)
    fun collectPotTest003() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(raise(600)))
        val jane = anInGamePlayer("Jane", 50, strategyMock())
        val dave = anInGamePlayer("Dave", 2000, strategyMock(raise(300), call()))
        val table = Table(listOf(alex, jane, dave), 2)
        val context = aContext(table, blinds(50, 100))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(650)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(50)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(650)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1350)
        assertThat(jane.stack).isEqualTo(0)
        assertThat(dave.stack).isEqualTo(1350)
    }

    @Test
    @DisplayName("Pre flop (50/100) story: Alex calls, Jane raises 300, Alex folds")
    fun preFlopStory004() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(call(), fold()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(raise(300)))
        val table = Table(listOf(alex, jane), 0)
        val context = aContext(table, blinds(50, 100))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(100)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(400)
        assertThat(alex.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(1900)
        assertThat(jane.stack).isEqualTo(1600)
    }

    @Test
    @DisplayName("Pre flop (50/100) story: Dave folds, Jane calls, Eric all-in, Alex calls, Juno folds, Jane calls")
    fun preFlopStory005() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(call()))
        val juno = anInGamePlayer("Juno", 2000, strategyMock(fold()))
        val dave = anInGamePlayer("Dave", 2000, strategyMock(fold()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(call(), call()))
        val eric = anInGamePlayer("Eric", 2000, strategyMock(raise(2000)))
        val table = Table(listOf(alex, juno, dave, jane, eric), 4)
        val context = aContext(table, blinds(50, 100))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(2000)
        assertThat(context.getGlobalPot().payedBy(juno)).isEqualTo(100)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(0)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(2000)
        assertThat(context.getGlobalPot().payedBy(eric)).isEqualTo(2000)
        assertThat(alex.status).isEqualTo(ALL_IN)
        assertThat(juno.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(eric.status).isEqualTo(ALL_IN)
        assertThat(alex.stack).isEqualTo(0)
        assertThat(juno.stack).isEqualTo(1900)
        assertThat(dave.stack).isEqualTo(2000)
        assertThat(jane.stack).isEqualTo(0)
        assertThat(eric.stack).isEqualTo(0)
    }

    @Test
    @DisplayName("Pre flop (50/100) story: Dave folds, Alex folds")
    fun preFlopStory006() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(fold()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(fold()))
        val dave = anInGamePlayer("Dave", 2000, strategyMock(fold()))
        val table = Table(listOf(alex, jane, dave), 2)
        val context = aContext(table, blinds(50, 100))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(50)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(100)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(0)
        assertThat(alex.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(NONE)
        assertThat(dave.status).isEqualTo(FOLD)
        assertThat(alex.stack).isEqualTo(1950)
        assertThat(jane.stack).isEqualTo(1900)
        assertThat(dave.stack).isEqualTo(2000)
    }

    @Test
    @DisplayName("Pre flop (60/120) story: Button calls, Small Blind folds, Big Blind folds")
    fun preFlopStory007() {
        val table: Table<InGamePlayer> = buildTestTable {
            button(stack = 9000, strategy = limper() )
            smallBlind(stack = 140, strategy = folder() )
            bigBlind(stack = 50, strategy = folder() )
        }
        val context = aContext(table, blinds(60, 120))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(table.button())).isEqualTo(120)
        assertThat(context.getGlobalPot().payedBy(table.smallBlind())).isEqualTo(60)
        assertThat(context.getGlobalPot().payedBy(table.bigBlind())).isEqualTo(50)
        assertThat(table.button().status).isEqualTo(CALL)
        assertThat(table.smallBlind().status).isEqualTo(FOLD)
        assertThat(table.bigBlind().status).isEqualTo(ALL_IN)
        assertThat(table.button().stack).isEqualTo(8880)
        assertThat(table.smallBlind().stack).isEqualTo(80)
        assertThat(table.bigBlind().stack).isEqualTo(0)
    }

    @Test
    @DisplayName("Pre flop (100/200) story: Dave calls, Alex folds")
    fun preFlopStory008() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(fold()))
        val jane = anInGamePlayer("Jane", 50, strategyMock(fold()))
        val dave = anInGamePlayer("Dave", 40, strategyMock(call()))
        val table = Table(listOf(alex, jane, dave), 2)
        val context = aContext(table, blinds(100, 200))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(100)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(50)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(40)
        assertThat(alex.status).isEqualTo(NONE)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(dave.status).isEqualTo(ALL_IN)
        assertThat(alex.stack).isEqualTo(1900)
        assertThat(jane.stack).isEqualTo(0)
        assertThat(dave.stack).isEqualTo(0)
    }

//    @Test
//    @DisplayName("Players receive a context to help them to act")
//    fun playersReceiveAContextToHelpThemToAct() {
//        val payments = blinds(10, 20)
//        val alexContexts = mutableListOf<GameContext<Opponent, Board>>()
//        val janeContexts = mutableListOf<GameContext<Opponent, Board>>()
//        val alexStrategy = strategyMock(alexContexts, call(), fold())
//        val janeStrategy = strategyMock(janeContexts, raise(100))
//        val alex = anInGamePlayer("Alex", 2000, alexStrategy)
//        val jane = anInGamePlayer("Jane", 2000, janeStrategy)
//        val table = Table(listOf(alex, jane), 0)
//        val context = aContext(table, payments)
//        val dealer = PostFlopDealer(context)
//
//        dealer.collectPot()
//
//        assertThat(alexContexts).hasSize(2)
//
//        assertThat(alexContexts[0].board.phase()).isEqualTo(GamePhase.PRE_FLOP)
//        assertThat(alexContexts[0].hero.name).isEqualTo("Alex")
//        assertThat(alexContexts[0].hero.stack).isEqualTo(2000)
//        assertThat(alexContexts[0].payments).isEqualTo(payments)
//        assertThat(alexContexts[0].potAmount).isEqualTo(0)
//        assertThat(alexContexts[0].history[GamePhase.PRE_FLOP] ?: error("Should be at Pre-Flop")).isEmpty()
//        assertThat(alexContexts[0].table.button).isEqualTo(0)
//        assertThat(alexContexts[0].table.players).extracting({ it.name }, { it.stack }, { it.status })
//                .containsOnly(Triple("Alex", 2000, NONE),
//                        Triple("Jane", 2000, NONE))
//
//        assertThat(janeContexts).hasSize(1)
//        assertThat(janeContexts[0].board.phase()).isEqualTo(GamePhase.PRE_FLOP)
//        assertThat(janeContexts[0].hero.name).isEqualTo("Jane")
//        assertThat(janeContexts[0].hero.stack).isEqualTo(2000)
//        assertThat(janeContexts[0].payments).isEqualTo(payments)
//        assertThat(janeContexts[0].potAmount).isEqualTo(0)
//        assertThat(janeContexts[0].history[GamePhase.PRE_FLOP] ?: error("Should be at Pre-Flop"))
//                .containsOnly( alex.player does call() )
//        assertThat(janeContexts[0].table.button).isEqualTo(0)
//        assertThat(janeContexts[0].table.players).extracting({ it.name }, { it.stack }, { it.status })
//                .containsOnly(Triple("Alex", 2000, CALL),
//                        Triple("Jane", 2000, NONE))
//
//        assertThat(alexContexts[1].board.phase()).isEqualTo(GamePhase.PRE_FLOP)
//        assertThat(alexContexts[1].hero.name).isEqualTo("Alex")
//        assertThat(alexContexts[1].hero.stack).isEqualTo(2000)
//        assertThat(alexContexts[1].payments).isEqualTo(payments)
//        assertThat(alexContexts[1].potAmount).isEqualTo(100)
//        assertThat(alexContexts[1].history[GamePhase.PRE_FLOP] ?: error("Should be at Pre-Flop"))
//                .containsOnly( alex.player does call(), jane.player does raise(100) )
//        assertThat(alexContexts[1].table.button).isEqualTo(0)
//        assertThat(alexContexts[1].table.players).extracting({ it.name }, { it.stack }, { it.status })
//                .containsOnly(Triple("Alex", 2000, CALL),
//                        Triple("Jane", 1900, RAISE))
//    }

    @Test
    @DisplayName("collectPot(): Alex raises 70, Jane folds, Dave calls -> Alex paid 70, Jane 10 and Dave 70")
    fun collectPotTest010() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(raise(70)))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(fold()))
        val dave = anInGamePlayer("Dave", 2000, strategyMock(call()))
        val table = Table(listOf(alex, jane, dave), 0)
        val context = aContext(table, blinds(10, 20))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(70)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(10)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(70)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1930)
        assertThat(jane.stack).isEqualTo(1990)
        assertThat(dave.stack).isEqualTo(1930)
    }

    @Test
    @DisplayName("collectPot(): button re-raise utg -> utg had to act again")
    fun collectPotTest009() {
        val table: Table<InGamePlayer> = buildTestTable {
            underTheGun(stack = 1000, strategy = strategyMock(raise(30), call()) )
            button(stack = 1000, strategy = strategyMock(raise(90)) )
            smallBlind(stack = 1000) { fold() }
            bigBlind(stack = 1000) { fold() }
        }
        val context = aContext(table, blinds(5, 10))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PreFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.playerName }, { it.action })
            .containsExactly(
                UNDER_THE_GUN to raise(30),
                BUTTON to raise(90),
                SMALL_BLIND to fold(),
                BIG_BLIND to fold(),
                UNDER_THE_GUN to call()
            )
        assertThat(context.getGlobalPot().payedBy(table.underTheGun())).isEqualTo(90)
        assertThat(context.getGlobalPot().payedBy(table.button())).isEqualTo(90)
        assertThat(table.underTheGun().status).isEqualTo(CALL)
        assertThat(table.button().status).isEqualTo(RAISE)
    }

    @Test
    @DisplayName("collectPot(): raising less tha requested previous bet -> raise will be treated as call")
    fun collectPotTest011() {
        val table: Table<InGamePlayer> = buildTestTable {
            underTheGun(stack = 2000)  { fold() }
            middle(stack = 2000) { raise(175) }
            late(stack = 2000) { raise(150) }
            button(stack = 2000) { fold() }
            smallBlind(stack = 2000) { call() }
            bigBlind(stack = 2000) { call() }
        }
        val context = aContext(table, blinds(25, 50))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PreFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.playerName }, { it.action })
            .containsExactly(
                UNDER_THE_GUN to fold(),
                MIDDLE to raise(175),
                LATE to call(),
                BUTTON to fold(),
                SMALL_BLIND to call(),
                BIG_BLIND to call()
            )
    }

    @Test
    @DisplayName("collectPot(): raising more than the stack -> raise will be treated as all-in")
    fun collectPotTest012() {
        val table: Table<InGamePlayer> = buildTestTable {
            smallBlind(stack = 2000, strategy = strategyMock(raise(175), raise(1000), raise(3000)))
            bigBlind(stack = 2000, strategy = strategyMock(call(), raise(2000)))
            button(stack = 200, strategy = strategyMock(raise(200)) )
        }
        val context = aContext(table, blinds(25, 50))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PostFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.playerName }, { it.action })
            .containsExactly(
                SMALL_BLIND to raise(175),
                BIG_BLIND to call(),
                BUTTON to raise(200),
                SMALL_BLIND to raise(1000),
                BIG_BLIND to raise(1825),
                SMALL_BLIND to call()
            )
        assertThat(context.getGlobalPot().payedBy(table.button())).isEqualTo(200)
        assertThat(context.getGlobalPot().payedBy(table.smallBlind())).isEqualTo(2000)
        assertThat(context.getGlobalPot().payedBy(table.bigBlind())).isEqualTo(2000)
        assertThat(table.button().status).isEqualTo(ALL_IN)
        assertThat(table.smallBlind().status).isEqualTo(ALL_IN)
        assertThat(table.bigBlind().status).isEqualTo(ALL_IN)
        assertThat(table.button().stack).isEqualTo(0)
        assertThat(table.smallBlind().stack).isEqualTo(0)
        assertThat(table.bigBlind().stack).isEqualTo(0)
    }

    @Test
    @DisplayName("collectPot(): raising less than requested to play -> raise will be treated as call")
    fun collectPotTest013() {
        val table: Table<InGamePlayer> = buildTestTable {
            underTheGun(stack = 2000)  { raise(1) }
            button(stack = 2000) { fold() }
            smallBlind(stack = 2000) { call() }
            bigBlind(stack = 2000) { call() }
        }
        val context = aContext(table, blinds(25, 50))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PreFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.playerName }, { it.action })
            .containsExactly(
                UNDER_THE_GUN to call(),
                BUTTON to fold(),
                SMALL_BLIND to call(),
                BIG_BLIND to call()
            )
    }

    @Test
    @DisplayName("collectPot(): raising as last raise -> raise will be treated as call")
    fun collectPotTest014() {
        val table: Table<InGamePlayer> = buildTestTable {
            underTheGun(stack = 2000)  { raise(150) }
            button(stack = 2000) { raise(150) }
            smallBlind(stack = 2000) { fold() }
            bigBlind(stack = 2000) { fold() }
        }
        val context = aContext(table, blinds(25, 50))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PreFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.playerName }, { it.action })
            .containsExactly(
                UNDER_THE_GUN to raise(150),
                BUTTON to call(),
                SMALL_BLIND to fold(),
                BIG_BLIND to fold()
            )
    }

    @Test
    @DisplayName("collectPot(): raising as last raise -> raise will be treated as call")
    fun collectPotTest015() {
        val table: Table<InGamePlayer> = buildTestTable {
            underTheGun(stack = 2000)  { raise(175) }
            button(stack = 2000) { call() }
            smallBlind(stack = 2000) { fold() }
            bigBlind(stack = 2000) { raise(150) }
        }
        val context = aContext(table, blinds(25, 50))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PreFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.playerName }, { it.action })
            .containsExactly(
                UNDER_THE_GUN to raise(175),
                BUTTON to call(),
                SMALL_BLIND to fold(),
                BIG_BLIND to call()
            )
    }

    @Test
    @DisplayName("collectPot(): tree players act before BigBlind -> BigBlind received the list of actions")
    fun collectPotTest016() {
        val strategyMock: PlayerStrategyInterface = mockk()
        val table: Table<InGamePlayer> = buildTestTable {
            underTheGun(stack = 2000)  { raise(175) }
            button(stack = 2000) { call() }
            smallBlind(stack = 2000) { fold() }
            bigBlind(stack = 2000, strategy = strategyMock)
        }
        val context = aContext(table, blinds(25, 50))
        val bigBlindContext: CapturingSlot<HeroGameContextImpl<OwnPlayer>> = slot()
        every { strategyMock.invoke(capture(bigBlindContext)) } answers { call() }
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        val captured = bigBlindContext.captured
        assertThat(captured.hero.name).isEqualTo(BIG_BLIND)
        val preFlopHistory = captured.history[GamePhase.PRE_FLOP]
        assertThat(preFlopHistory).isNotNull()
        assertThat(preFlopHistory!!).extracting { it.action }
            .containsExactly(raise(175), call(), fold())
    }

}
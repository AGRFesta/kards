package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext
import agrfesta.k.cards.texasholdem.playercontext.does
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.*
import assertk.assertThat
import assertk.assertions.*
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
        val dealer = PostFlopDealer(buildPot(), context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(0)
        assertThat(pot.payedBy(jane)).isEqualTo(100)
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
        val dealer = PostFlopDealer(buildPot(), context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(200)
        assertThat(pot.payedBy(jane)).isEqualTo(200)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1800)
        assertThat(jane.stack).isEqualTo(1800)
    }

    @Test
    @DisplayName("Post flop (10/20) story: Alex calls, Jane raises 1, Alex calls")
    fun postFlopStory002() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(call(), call()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(raise(1)))
        val table = Table(listOf(alex, jane), 0)
        val context = aContext(table, blinds(10, 20))
        val dealer = PostFlopDealer(buildPot(), context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(20)
        assertThat(pot.payedBy(jane)).isEqualTo(20)
        assertThat(alex.status).isEqualTo(CALL)
        assertThat(jane.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(1980)
        assertThat(jane.stack).isEqualTo(1980)
    }

    @Test
    @DisplayName("Post flop (10/20) story: Alex calls, Jane raises Int.MAX_VALUE, Alex calls")
    fun postFlopStory003() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(call(), call()))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(raise(Int.MAX_VALUE)))
        val table = Table(listOf(alex, jane), 0)
        val context = aContext(table, blinds(10, 20))
        val dealer = PostFlopDealer(buildPot(), context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(2000)
        assertThat(pot.payedBy(jane)).isEqualTo(2000)
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
        val dealer = PostFlopDealer(buildPot(), context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(200)
        assertThat(pot.payedBy(jane)).isEqualTo(200)
        assertThat(pot.payedBy(dave)).isEqualTo(200)
        assertThat(alex.status).isEqualTo(CALL)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(dave.status).isEqualTo(ALL_IN)
        assertThat(alex.stack).isEqualTo(1800)
        assertThat(jane.stack).isEqualTo(1800)
        assertThat(dave.stack).isEqualTo(0)
    }

    @Test
    @DisplayName("Post flop (25/50) story: Alex raises 175, Jane calls, Dave all-in, Alex raises 1000, Jane all-in")
    fun postFlopStory005() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(raise(175), raise(1000)))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(call(), raise(2000)))
        val dave = anInGamePlayer("Dave", 200, strategyMock(raise(200)))
        val table = Table(listOf(alex, jane, dave), 2)
        val context = aContext(table, blinds(25, 50))
        val dealer = PostFlopDealer(buildPot(), context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(200)
        assertThat(pot.payedBy(jane)).isEqualTo(200)
        assertThat(pot.payedBy(dave)).isEqualTo(200)
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
        val dealer = PostFlopDealer(buildPot(), context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(575)
        assertThat(pot.payedBy(jane)).isEqualTo(200)
        assertThat(pot.payedBy(dave)).isEqualTo(575)
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
        val dealer = PostFlopDealer(buildPot(), context)

        val pot = dealer.collectPot()

        assertThat(pot).isEmpty()
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

        val prevPot = buildPot()
        prevPot.receiveFrom(alex, 20)
        prevPot.receiveFrom(jane, 20)

        val context = aContext(table, blinds(10, 20))
        val dealer = PostFlopDealer(prevPot, context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(0)
        assertThat(pot.payedBy(jane)).isEqualTo(100)
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

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(100)
        assertThat(pot.payedBy(jane)).isEqualTo(100)
        assertThat(pot.payedBy(dave)).isEqualTo(100)
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

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(250)
        assertThat(pot.payedBy(jane)).isEqualTo(250)
        assertThat(pot.payedBy(dave)).isEqualTo(250)
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

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(300)
        assertThat(pot.payedBy(jane)).isEqualTo(100)
        assertThat(pot.payedBy(dave)).isEqualTo(300)
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

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(650)
        assertThat(pot.payedBy(jane)).isEqualTo(50)
        assertThat(pot.payedBy(dave)).isEqualTo(650)
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

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(100)
        assertThat(pot.payedBy(jane)).isEqualTo(400)
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

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(2000)
        assertThat(pot.payedBy(juno)).isEqualTo(100)
        assertThat(pot.payedBy(dave)).isEqualTo(0)
        assertThat(pot.payedBy(jane)).isEqualTo(2000)
        assertThat(pot.payedBy(eric)).isEqualTo(2000)
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

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(50)
        assertThat(pot.payedBy(jane)).isEqualTo(100)
        assertThat(pot.payedBy(dave)).isEqualTo(0)
        assertThat(alex.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(NONE)
        assertThat(dave.status).isEqualTo(FOLD)
        assertThat(alex.stack).isEqualTo(1950)
        assertThat(jane.stack).isEqualTo(1900)
        assertThat(dave.stack).isEqualTo(2000)
    }

    @Test
    @DisplayName("Pre flop (60/120) story: Dave calls, Alex folds, Jane folds")
    fun preFlopStory007() {
        val alex = anInGamePlayer("Alex", 50, strategyMock(fold()))
        val jane = anInGamePlayer("Jane", 140, strategyMock(fold()))
        val dave = anInGamePlayer("Dave", 9000, strategyMock(call()))
        val table = Table(listOf(alex, jane, dave), 2)
        val context = aContext(table, blinds(60, 120))
        val dealer = PreFlopDealer(context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(50)
        assertThat(pot.payedBy(jane)).isEqualTo(120)
        assertThat(pot.payedBy(dave)).isEqualTo(120)
        assertThat(alex.status).isEqualTo(ALL_IN)
        assertThat(jane.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(0)
        assertThat(jane.stack).isEqualTo(20)
        assertThat(dave.stack).isEqualTo(8880)
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

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(100)
        assertThat(pot.payedBy(jane)).isEqualTo(50)
        assertThat(pot.payedBy(dave)).isEqualTo(40)
        assertThat(alex.status).isEqualTo(NONE)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(dave.status).isEqualTo(ALL_IN)
        assertThat(alex.stack).isEqualTo(1900)
        assertThat(jane.stack).isEqualTo(0)
        assertThat(dave.stack).isEqualTo(0)
    }

    @Test
    @DisplayName("Players receive a context to help them to act")
    fun playersReceiveAContextToHelpThemToAct() {
        val payments = blinds(10, 20)
        val alexContexts = mutableListOf<PlayerGameContext>()
        val janeContexts = mutableListOf<PlayerGameContext>()
        val alexStrategy = strategyMock(alexContexts, call(), fold())
        val janeStrategy = strategyMock(janeContexts, raise(100))
        val alex = anInGamePlayer("Alex", 2000, alexStrategy)
        val jane = anInGamePlayer("Jane", 2000, janeStrategy)
        val table = Table(listOf(alex, jane), 0)
        val context = aContext(table, payments)
        val dealer = PostFlopDealer(buildPot(), context)

        dealer.collectPot()

        assertThat(alexContexts).hasSize(2)

        assertThat(alexContexts[0].board.phase).isEqualTo(GamePhase.PRE_FLOP)
        assertThat(alexContexts[0].me.name).isEqualTo("Alex")
        assertThat(alexContexts[0].me.stack).isEqualTo(2000)
        assertThat(alexContexts[0].payments).isEqualTo(payments)
        assertThat(alexContexts[0].potAmount).isEqualTo(0)
        assertThat(alexContexts[0].history[GamePhase.PRE_FLOP] ?: error("Should be at Pre-Flop")).isEmpty()
        assertThat(alexContexts[0].table.button).isEqualTo(0)
        assertThat(alexContexts[0].table.players).extracting({ it.name }, { it.stack }, { it.status })
                .containsOnly(Triple("Alex", 2000, NONE),
                        Triple("Jane", 2000, NONE))

        assertThat(janeContexts).hasSize(1)
        assertThat(janeContexts[0].board.phase).isEqualTo(GamePhase.PRE_FLOP)
        assertThat(janeContexts[0].me.name).isEqualTo("Jane")
        assertThat(janeContexts[0].me.stack).isEqualTo(2000)
        assertThat(janeContexts[0].payments).isEqualTo(payments)
        assertThat(janeContexts[0].potAmount).isEqualTo(0)
        assertThat(janeContexts[0].history[GamePhase.PRE_FLOP] ?: error("Should be at Pre-Flop"))
                .containsOnly( alex.player does call() )
        assertThat(janeContexts[0].table.button).isEqualTo(0)
        assertThat(janeContexts[0].table.players).extracting({ it.name }, { it.stack }, { it.status })
                .containsOnly(Triple("Alex", 2000, CALL),
                        Triple("Jane", 2000, NONE))

        assertThat(alexContexts[1].board.phase).isEqualTo(GamePhase.PRE_FLOP)
        assertThat(alexContexts[1].me.name).isEqualTo("Alex")
        assertThat(alexContexts[1].me.stack).isEqualTo(2000)
        assertThat(alexContexts[1].payments).isEqualTo(payments)
        assertThat(alexContexts[1].potAmount).isEqualTo(100)
        assertThat(alexContexts[1].history[GamePhase.PRE_FLOP] ?: error("Should be at Pre-Flop"))
                .containsOnly( alex.player does call(), jane.player does raise(100) )
        assertThat(alexContexts[1].table.button).isEqualTo(0)
        assertThat(alexContexts[1].table.players).extracting({ it.name }, { it.stack }, { it.status })
                .containsOnly(Triple("Alex", 2000, CALL),
                        Triple("Jane", 1900, RAISE))
    }

    @Test
    @DisplayName("collectPot(): Alex raises 70, Jane folds, Dave calls -> Alex paid 70, Jane 10 and Dave 70")
    fun collectPotTest010() {
        val alex = anInGamePlayer("Alex", 2000, strategyMock(raise(70)))
        val jane = anInGamePlayer("Jane", 2000, strategyMock(fold()))
        val dave = anInGamePlayer("Dave", 2000, strategyMock(call()))
        val table = Table(listOf(alex, jane, dave), 0)
        val context = aContext(table, blinds(10, 20))
        val dealer = PreFlopDealer(context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(70)
        assertThat(pot.payedBy(jane)).isEqualTo(10)
        assertThat(pot.payedBy(dave)).isEqualTo(70)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1930)
        assertThat(jane.stack).isEqualTo(1990)
        assertThat(dave.stack).isEqualTo(1930)
    }

}

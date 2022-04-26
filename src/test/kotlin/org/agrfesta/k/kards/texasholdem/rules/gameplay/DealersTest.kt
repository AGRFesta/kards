package org.agrfesta.k.kards.texasholdem.rules.gameplay

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
import org.agrfesta.k.kards.texasholdem.observers.DealerObserver
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.ALL_IN
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.CALL
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.NONE
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.RAISE
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.BIG_BLIND
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.BUTTON
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.LATE
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.MIDDLE
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.SMALL_BLIND
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.UNDER_THE_GUN
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.bigBlind
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.buildTestTable
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.button
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.smallBlind
import org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers.underTheGun
import org.agrfesta.k.kards.texasholdem.utils.DistinctList.Companion.distinctListOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Dealers tests")
class DealersTest {

    @Test
    @DisplayName("Post flop (10/20) story: Alex checks, Jane raises 100, Alex folds")
    fun postFlopStory000() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(check(), fold()))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(raise(100u)))
        val table = TableImpl(distinctListOf(alex, jane), 0u)
        val context = aContext(table, blinds(10u, 20u))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(0u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(100u)
        assertThat(alex.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(2000u)
        assertThat(jane.stack).isEqualTo(1900u)
    }

    @Test
    @DisplayName("""collectPot(): Pre flop (10/20) Alex calls, Jane raises 100, Alex raises 200, Jane calls -> 
        Alex paid 200 and Jane 200
    """)
    fun collectPotTest004() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(call(), raise(200u)))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(raise(100u), call()))
        val table = TableImpl(distinctListOf(alex, jane), 0u)
        val context = aContext(table, blinds(10u, 20u))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(200u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(200u)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1800u)
        assertThat(jane.stack).isEqualTo(1800u)
    }

    @Test
    @DisplayName("Post flop (10/20) story: Alex checks, Jane raises Int.MAX_VALUE, Alex calls")
    fun postFlopStory003() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(check(), call()))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(raise(UInt.MAX_VALUE)))
        val table = TableImpl(distinctListOf(alex, jane), 0u)
        val context = aContext(table, blinds(10u, 20u))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(2000u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(2000u)
        assertThat(alex.status).isEqualTo(ALL_IN)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(alex.stack).isEqualTo(0u)
        assertThat(jane.stack).isEqualTo(0u)
    }

    @Test
    @DisplayName("Post flop (10/20) story: Alex raises 175, Jane calls, Dave all-in, Alex calls, Jane calls")
    fun postFlopStory004() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(raise(175u), call()))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(call(), call()))
        val dave = anInGamePlayer(name = "Dave", stack = 200u, strategy = strategyMock(raise(200u)))
        val table = TableImpl(distinctListOf(alex, jane, dave), 2u)
        val context = aContext(table, blinds(10u, 20u))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(200u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(200u)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(200u)
        assertThat(alex.status).isEqualTo(CALL)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(dave.status).isEqualTo(ALL_IN)
        assertThat(alex.stack).isEqualTo(1800u)
        assertThat(jane.stack).isEqualTo(1800u)
        assertThat(dave.stack).isEqualTo(0u)
    }

    @Test
    @DisplayName("""collectPot(): Pre flop (25/50) Alex raises 175, Jane all-in, Dave raises 200, Alex raises 200, 
        Dave calls -> Alex paid 200 and Jane 200
    """)
    fun collectPotTest005() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(raise(175u), raise(400u)))
        val jane = anInGamePlayer(name = "Jane", stack = 200u, strategy = strategyMock(raise(200u)))
        val dave = anInGamePlayer(name = "Dave", stack = 2000u, strategy = strategyMock(raise(300u), call()))
        val table = TableImpl(distinctListOf(alex, jane, dave), 2u)
        val context = aContext(table, blinds(25u, 50u))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(575u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(200u)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(575u)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1425u)
        assertThat(jane.stack).isEqualTo(0u)
        assertThat(dave.stack).isEqualTo(1425u)
    }

    @Test
    @DisplayName("Post flop (110/220) story: Dave folds")
    fun postFlopStory008() {
        val alex = anInGamePlayer(name = "Alex", stack = 0u, status = ALL_IN, strategy = strategyMock(call()))
        val juno = anInGamePlayer(name = "Juno", stack = 20u, status = FOLD, strategy = strategyMock(call()))
        val dave = anInGamePlayer(name = "Dave", stack = 2000u, status = CALL, strategy = strategyMock(fold()))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, status = CALL, strategy = strategyMock(fold()))
        val eric = anInGamePlayer(name = "Eric", stack = 0u, status = ALL_IN, strategy = strategyMock(call()))
        val sara = anInGamePlayer(name = "Sara", stack = 8880u, status = FOLD, strategy = strategyMock(call()))

        val table = TableImpl(distinctListOf(alex, juno, dave, jane, eric, sara), 5u)
        val context = aContext(table, blinds(110u, 220u))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot()).isEmpty()
        assertThat(alex.status).isEqualTo(ALL_IN)
        assertThat(juno.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(NONE)
        assertThat(eric.status).isEqualTo(ALL_IN)
        assertThat(sara.status).isEqualTo(FOLD)
        assertThat(alex.stack).isEqualTo(0u)
        assertThat(juno.stack).isEqualTo(20u)
        assertThat(dave.stack).isEqualTo(2000u)
        assertThat(jane.stack).isEqualTo(2000u)
        assertThat(eric.stack).isEqualTo(0u)
        assertThat(sara.stack).isEqualTo(8880u)
    }

    @Test
    @DisplayName("Post flop (10/20) story: In previous phase both Alex and Jane call 20. " +
            "Alex checks, Jane raises 100, Alex folds")
    fun postFlopStory009() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(check(), fold()))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(raise(100u)))
        val table = TableImpl(distinctListOf(alex, jane), 0u)

        val prevPot = buildMutablePot()
        prevPot.receiveFrom(alex, 20u)
        prevPot.receiveFrom(jane, 20u)

        val context = aContext(table, blinds(10u, 20u))
        val dealer = PostFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(0u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(100u)
        assertThat(alex.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(1980u)
        assertThat(jane.stack).isEqualTo(1880u)
    }

    @Test
    @DisplayName("Pre flop (50/100) story: Dave calls, Alex calls, Jane calls")
    fun preFlopStory000() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(call()))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(call()))
        val dave = anInGamePlayer(name = "Dave", stack = 2000u, strategy = strategyMock(call()))
        val table = TableImpl(distinctListOf(alex, jane, dave), 2u)
        val context = aContext(table, blinds(50u, 100u))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(100u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(100u)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(100u)
        assertThat(alex.status).isEqualTo(CALL)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1900u)
        assertThat(jane.stack).isEqualTo(1900u)
        assertThat(dave.stack).isEqualTo(1900u)
    }

    @Test
    @DisplayName("""collectPot(): Pre flop (50/100) Dave calls, Alex raises 200, Jane calls, Dave calls -> 
        Alex paid 250, Jane 250, Dave 250 
    """)
    fun collectPotTest001() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(raise(200u)))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(call()))
        val dave = anInGamePlayer(name = "Dave", stack = 2000u, strategy = strategyMock(call(), call()))
        val table = TableImpl(distinctListOf(alex, jane, dave), 2u)
        val context = aContext(table, blinds(50u, 100u))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(250u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(250u)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(250u)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1750u)
        assertThat(jane.stack).isEqualTo(1750u)
        assertThat(dave.stack).isEqualTo(1750u)
    }

    @Test
    @DisplayName("""collectPot(): Pre flop (50/100) Dave raises 300, Alex calls, Jane folds -> 
        Alex paid 300, Jane 100, Dave 300 
    """)
    fun collectPotTest002() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(call()))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(fold()))
        val dave = anInGamePlayer(name = "Dave", stack = 2000u, strategy = strategyMock(raise(300u)))
        val table = TableImpl(distinctListOf(alex, jane, dave), 2u)
        val context = aContext(table, blinds(50u, 100u))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(300u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(100u)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(300u)
        assertThat(alex.status).isEqualTo(CALL)
        assertThat(jane.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(1700u)
        assertThat(jane.stack).isEqualTo(1900u)
        assertThat(dave.stack).isEqualTo(1700u)
    }

    @Test
    @DisplayName("""collectPot(): Pre flop (50/100) Dave raises 300, Alex re-raises 600, Dave calls -> 
        Alex paid 650, Jane 50, Dave 650 
    """)
    fun collectPotTest003() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(raise(600u)))
        val jane = anInGamePlayer(name = "Jane", stack = 50u, strategy = strategyMock())
        val dave = anInGamePlayer(name = "Dave", stack = 2000u, strategy = strategyMock(raise(300u), call()))
        val table = TableImpl(distinctListOf(alex, jane, dave), 2u)
        val context = aContext(table, blinds(50u, 100u))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(650u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(50u)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(650u)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1350u)
        assertThat(jane.stack).isEqualTo(0u)
        assertThat(dave.stack).isEqualTo(1350u)
    }

    @Test
    @DisplayName("Pre flop (50/100) story: Alex calls, Jane raises 300, Alex folds")
    fun preFlopStory004() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(call(), fold()))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(raise(300u)))
        val table = TableImpl(distinctListOf(alex, jane), 0u)
        val context = aContext(table, blinds(50u, 100u))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(100u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(400u)
        assertThat(alex.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(1900u)
        assertThat(jane.stack).isEqualTo(1600u)
    }

    @Test
    @DisplayName("Pre flop (50/100) story: Dave folds, Jane calls, Eric all-in, Alex calls, Juno folds, Jane calls")
    fun preFlopStory005() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(call()))
        val juno = anInGamePlayer(name = "Juno", stack = 2000u, strategy = strategyMock(fold()))
        val dave = anInGamePlayer(name = "Dave", stack = 2000u, strategy = strategyMock(fold()))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(call(), call()))
        val eric = anInGamePlayer(name = "Eric", stack = 2000u, strategy = strategyMock(raise(2000u)))
        val table = TableImpl(distinctListOf(alex, juno, dave, jane, eric), 4u)
        val context = aContext(table, blinds(50u, 100u))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(2000u)
        assertThat(context.getGlobalPot().payedBy(juno)).isEqualTo(100u)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(0u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(2000u)
        assertThat(context.getGlobalPot().payedBy(eric)).isEqualTo(2000u)
        assertThat(alex.status).isEqualTo(ALL_IN)
        assertThat(juno.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(eric.status).isEqualTo(ALL_IN)
        assertThat(alex.stack).isEqualTo(0u)
        assertThat(juno.stack).isEqualTo(1900u)
        assertThat(dave.stack).isEqualTo(2000u)
        assertThat(jane.stack).isEqualTo(0u)
        assertThat(eric.stack).isEqualTo(0u)
    }

    @Test
    @DisplayName("Pre flop (50/100) story: Dave folds, Alex folds")
    fun preFlopStory006() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(fold()))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(fold()))
        val dave = anInGamePlayer(name = "Dave", stack = 2000u, strategy = strategyMock(fold()))
        val table = TableImpl(distinctListOf(alex, jane, dave), 2u)
        val context = aContext(table, blinds(50u, 100u))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(50u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(100u)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(0u)
        assertThat(alex.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(NONE)
        assertThat(dave.status).isEqualTo(FOLD)
        assertThat(alex.stack).isEqualTo(1950u)
        assertThat(jane.stack).isEqualTo(1900u)
        assertThat(dave.stack).isEqualTo(2000u)
    }

    @Test
    @DisplayName("Pre flop (60/120) story: Button calls, Small Blind folds, Big Blind folds")
    fun preFlopStory007() {
        val table: Table<InGamePlayer> = buildTestTable {
            button(stack = 9000u, strategy = limper() )
            smallBlind(stack = 140u, strategy = folder() )
            bigBlind(stack = 50u, strategy = folder() )
        }
        val context = aContext(table, blinds(60u, 120u))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(table.button())).isEqualTo(120u)
        assertThat(context.getGlobalPot().payedBy(table.smallBlind())).isEqualTo(60u)
        assertThat(context.getGlobalPot().payedBy(table.bigBlind())).isEqualTo(50u)
        assertThat(table.button().status).isEqualTo(CALL)
        assertThat(table.smallBlind().status).isEqualTo(FOLD)
        assertThat(table.bigBlind().status).isEqualTo(ALL_IN)
        assertThat(table.button().stack).isEqualTo(8880u)
        assertThat(table.smallBlind().stack).isEqualTo(80u)
        assertThat(table.bigBlind().stack).isEqualTo(0u)
    }

    @Test
    @DisplayName("Pre flop (100/200) story: Dave calls, Alex folds")
    fun preFlopStory008() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(fold()))
        val jane = anInGamePlayer(name = "Jane", stack = 50u, strategy = strategyMock(fold()))
        val dave = anInGamePlayer(name = "Dave", stack = 40u, strategy = strategyMock(call()))
        val table = TableImpl(distinctListOf(alex, jane, dave), 2u)
        val context = aContext(table, blinds(100u, 200u))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(100u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(50u)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(40u)
        assertThat(alex.status).isEqualTo(NONE)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(dave.status).isEqualTo(ALL_IN)
        assertThat(alex.stack).isEqualTo(1900u)
        assertThat(jane.stack).isEqualTo(0u)
        assertThat(dave.stack).isEqualTo(0u)
    }

    @Test
    @DisplayName("collectPot(): Alex raises 70, Jane folds, Dave calls -> Alex paid 70, Jane 10 and Dave 70")
    fun collectPotTest010() {
        val alex = anInGamePlayer(name = "Alex", stack = 2000u, strategy = strategyMock(raise(70u)))
        val jane = anInGamePlayer(name = "Jane", stack = 2000u, strategy = strategyMock(fold()))
        val dave = anInGamePlayer(name = "Dave", stack = 2000u, strategy = strategyMock(call()))
        val table = TableImpl(distinctListOf(alex, jane, dave), 0u)
        val context = aContext(table, blinds(10u, 20u))
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        assertThat(context.getGlobalPot().payedBy(alex)).isEqualTo(70u)
        assertThat(context.getGlobalPot().payedBy(jane)).isEqualTo(10u)
        assertThat(context.getGlobalPot().payedBy(dave)).isEqualTo(70u)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1930u)
        assertThat(jane.stack).isEqualTo(1990u)
        assertThat(dave.stack).isEqualTo(1930u)
    }

    @Test
    @DisplayName("collectPot(): button re-raise utg -> utg had to act again")
    fun collectPotTest009() {
        val table: Table<InGamePlayer> = buildTestTable {
            underTheGun(stack = 1000u, strategy = strategyMock(raise(30u), call()) )
            button(stack = 1000u, strategy = strategyMock(raise(90u)) )
            smallBlind(stack = 1000u) { _,_ -> fold() }
            bigBlind(stack = 1000u) { _,_ -> fold() }
        }
        val context = aContext(table, blinds(5u, 10u))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PreFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.player.name }, { it.action })
            .containsExactly(
                UNDER_THE_GUN to raise(30u),
                BUTTON to raise(90u),
                SMALL_BLIND to fold(),
                BIG_BLIND to fold(),
                UNDER_THE_GUN to call()
            )
        assertThat(context.getGlobalPot().payedBy(table.underTheGun())).isEqualTo(90u)
        assertThat(context.getGlobalPot().payedBy(table.button())).isEqualTo(90u)
        assertThat(table.underTheGun().status).isEqualTo(CALL)
        assertThat(table.button().status).isEqualTo(RAISE)
    }

    @Test
    @DisplayName("collectPot(): raising less tha requested previous bet -> raise will be treated as call")
    fun collectPotTest011() {
        val table: Table<InGamePlayer> = buildTestTable {
            underTheGun(stack = 2000u)  { _,_ -> fold() }
            middle(stack = 2000u) { _,_ -> raise(175u) }
            late(stack = 2000u) { _,_ -> raise(150u) }
            button(stack = 2000u) { _,_ -> fold() }
            smallBlind(stack = 2000u) { _,_ -> call() }
            bigBlind(stack = 2000u) { _,_ -> call() }
        }
        val context = aContext(table, blinds(25u, 50u))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PreFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.player.name }, { it.action })
            .containsExactly(
                UNDER_THE_GUN to fold(),
                MIDDLE to raise(175u),
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
            smallBlind(stack = 2000u, strategy = strategyMock(raise(175u), raise(1000u), raise(3000u)))
            bigBlind(stack = 2000u, strategy = strategyMock(call(), raise(2000u)))
            button(stack = 200u, strategy = strategyMock(raise(200u)) )
        }
        val context = aContext(table, blinds(25u, 50u))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PostFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.player.name }, { it.action })
            .containsExactly(
                SMALL_BLIND to raise(175u),
                BIG_BLIND to call(),
                BUTTON to raise(200u),
                SMALL_BLIND to raise(1000u),
                BIG_BLIND to raise(1825u),
                SMALL_BLIND to call()
            )
        assertThat(context.getGlobalPot().payedBy(table.button())).isEqualTo(200u)
        assertThat(context.getGlobalPot().payedBy(table.smallBlind())).isEqualTo(2000u)
        assertThat(context.getGlobalPot().payedBy(table.bigBlind())).isEqualTo(2000u)
        assertThat(table.button().status).isEqualTo(ALL_IN)
        assertThat(table.smallBlind().status).isEqualTo(ALL_IN)
        assertThat(table.bigBlind().status).isEqualTo(ALL_IN)
        assertThat(table.button().stack).isEqualTo(0u)
        assertThat(table.smallBlind().stack).isEqualTo(0u)
        assertThat(table.bigBlind().stack).isEqualTo(0u)
    }

    @Test
    @DisplayName("collectPot(): raising less than requested to play -> raise will be treated as call")
    fun collectPotTest013() {
        val table: Table<InGamePlayer> = buildTestTable {
            underTheGun(stack = 2000u)  { _,_ -> raise(1u) }
            button(stack = 2000u) { _,_ -> fold() }
            smallBlind(stack = 2000u) { _,_ -> call() }
            bigBlind(stack = 2000u) { _,_ -> call() }
        }
        val context = aContext(table, blinds(25u, 50u))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PreFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.player.name }, { it.action })
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
            underTheGun(stack = 2000u)  { _,_ -> raise(150u) }
            button(stack = 2000u) { _,_ -> raise(150u) }
            smallBlind(stack = 2000u) { _,_ -> fold() }
            bigBlind(stack = 2000u) { _,_ -> fold() }
        }
        val context = aContext(table, blinds(25u, 50u))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PreFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.player.name }, { it.action })
            .containsExactly(
                UNDER_THE_GUN to raise(150u),
                BUTTON to call(),
                SMALL_BLIND to fold(),
                BIG_BLIND to fold()
            )
    }

    @Test
    @DisplayName("collectPot(): raising as last raise -> raise will be treated as call")
    fun collectPotTest015() {
        val table: Table<InGamePlayer> = buildTestTable {
            underTheGun(stack = 2000u)  { _,_ -> raise(175u) }
            button(stack = 2000u) { _,_ -> call() }
            smallBlind(stack = 2000u) { _,_ -> fold() }
            bigBlind(stack = 2000u) { _,_ -> raise(150u) }
        }
        val context = aContext(table, blinds(25u, 50u))
        val observer: DealerObserver = mockk(relaxed = true)
        val actions: MutableList<PlayerAction> = mutableListOf()
        every { observer.notifyAction(any(), capture(actions)) } just Runs
        val dealer = PreFlopDealer(context, observer)

        dealer.collectPot()

        assertThat(actions).extracting({ it.player.name }, { it.action })
            .containsExactly(
                UNDER_THE_GUN to raise(175u),
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
            underTheGun(stack = 2000u)  { _,_ -> raise(175u) }
            button(stack = 2000u) { _,_ -> call() }
            smallBlind(stack = 2000u) { _,_ -> fold() }
            bigBlind(stack = 2000u, strategy = strategyMock)
        }
        val context = aContext(table, blinds(25u, 50u))
        val ownPlayer: CapturingSlot<OwnPlayer> = slot()
        val bigBlindContext: CapturingSlot<GameContext> = slot()
        every { strategyMock.invoke(capture(ownPlayer),capture(bigBlindContext)) } answers { call() }
        val dealer = PreFlopDealer(context)

        dealer.collectPot()

        val captured = bigBlindContext.captured
        assertThat(ownPlayer.captured.name).isEqualTo(BIG_BLIND)
        val preFlopHistory = captured.history[GamePhase.PRE_FLOP]
        assertThat(preFlopHistory).isNotNull()
        assertThat(preFlopHistory!!).extracting { it.action }
            .containsExactly(raise(175u), call(), fold())
    }

}

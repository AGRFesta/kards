package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext
import assertk.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.*
import assertk.assertions.*

@DisplayName("Dealers tests")
class DealersTest {

    @Test
    @DisplayName("Post flop (10/20) story: Alex calls, Jane raises 100, Alex folds")
    fun postFlopStory000() {
        val alex = aPlayer("Alex", 2000, strategyMock(call(),    fold() ))
        val jane = aPlayer("Jane", 2000, strategyMock(raise(100)        ))
        val table = Table(listOf(alex,jane),0)
        val context = aContext(table, blinds(10,20))
        val dealer = PostFlopDealer(buildPot(),context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(0)
        assertThat(pot.payedBy(jane)).isEqualTo(100)
        assertThat(alex.status).isEqualTo(FOLD)
        assertThat(jane.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(2000)
        assertThat(jane.stack).isEqualTo(1900)
    }
    @Test
    @DisplayName("Post flop (10/20) story: Alex calls, Jane raises 100, Alex raises 200, Jane calls")
    fun postFlopStory001() {
        val alex = aPlayer("Alex", 2000, strategyMock(call(),     raise(200) ))
        val jane = aPlayer("Jane", 2000, strategyMock(raise(100), call()     ))
        val table = Table(listOf(alex,jane),0)
        val context = aContext(table, blinds(10,20))
        val dealer = PostFlopDealer(buildPot(),context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(300)
        assertThat(pot.payedBy(jane)).isEqualTo(300)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1700)
        assertThat(jane.stack).isEqualTo(1700)
    }
    @Test
    @DisplayName("Post flop (10/20) story: Alex calls, Jane raises 1, Alex calls")
    fun postFlopStory002() {
        val alex = aPlayer("Alex", 2000, strategyMock(call(),   call() ))
        val jane = aPlayer("Jane", 2000, strategyMock(raise(1)         ))
        val table = Table(listOf(alex,jane),0)
        val context = aContext(table, blinds(10,20))
        val dealer = PostFlopDealer(buildPot(),context)

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
        val alex = aPlayer("Alex", 2000, strategyMock(call(),               call() ))
        val jane = aPlayer("Jane", 2000, strategyMock(raise(Int.MAX_VALUE)         ))
        val table = Table(listOf(alex,jane),0)
        val context = aContext(table, blinds(10,20))
        val dealer = PostFlopDealer(buildPot(),context)

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
        val alex = aPlayer("Alex", 2000, strategyMock(raise(175), call() ))
        val jane = aPlayer("Jane", 2000, strategyMock(call(),     call() ))
        val dave = aPlayer("Dave", 200,  strategyMock(raise(200)         ))
        val table = Table(listOf(alex,jane,dave),2)
        val context = aContext(table, blinds(10,20))
        val dealer = PostFlopDealer(buildPot(),context)

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
        val alex = aPlayer("Alex", 2000, strategyMock(raise(175), raise(1000) ))
        val jane = aPlayer("Jane", 2000, strategyMock(call(),     raise(2000) ))
        val dave = aPlayer("Dave", 200,  strategyMock(raise(200)              ))
        val table = Table(listOf(alex,jane,dave),2)
        val context = aContext(table, blinds(25,50))
        val dealer = PostFlopDealer(buildPot(),context)

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
    @DisplayName("Post flop (25/50) story: Alex raises 175, Jane all-in, Dave raises 200, Alex raises 200, Dave calls")
    fun postFlopStory007() {
        val alex = aPlayer("Alex", 2000, strategyMock(raise(175), raise(200) ))
        val jane = aPlayer("Jane", 200,  strategyMock(raise(200)             ))
        val dave = aPlayer("Dave", 2000, strategyMock(raise(200), call()     ))
        val table = Table(listOf(alex,jane,dave),2)
        val context = aContext(table, blinds(25,50))
        val dealer = PostFlopDealer(buildPot(),context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(600)
        assertThat(pot.payedBy(jane)).isEqualTo(200)
        assertThat(pot.payedBy(dave)).isEqualTo(600)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1400)
        assertThat(jane.stack).isEqualTo(0)
        assertThat(dave.stack).isEqualTo(1400)
    }
    @Test
    @DisplayName("Post flop (110/220) story: Dave folds")
    fun postFlopStory008() {
        val alex = aPlayer("Alex", 0,    ALL_IN,    strategyMock( call() ))
        val juno = aPlayer("Juno", 20,   FOLD,      strategyMock( call() ))
        val dave = aPlayer("Dave", 2000, CALL,      strategyMock( fold() ))
        val jane = aPlayer("Jane", 2000, CALL,      strategyMock( fold() ))
        val eric = aPlayer("Eric", 0,    ALL_IN,    strategyMock( call() ))
        val sara = aPlayer("Sara", 8880, FOLD,      strategyMock( call() ))

        val table = Table(listOf(alex,juno,dave,jane,eric,sara),5)
        val context = aContext(table, blinds(110,220))
        val dealer = PostFlopDealer(buildPot(),context)

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
    @DisplayName("Pre flop (50/100) story: Dave calls, Alex calls, Jane calls")
    fun preFlopStory000() {
        val alex = aPlayer("Alex", 2000, strategyMock( call() ))
        val jane = aPlayer("Jane", 2000, strategyMock( call() ))
        val dave = aPlayer("Dave", 2000, strategyMock( call() ))
        val table = Table(listOf(alex,jane,dave),2)
        val context = aContext(table, blinds(50,100))
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
    @DisplayName("Pre flop (50/100) story: Dave calls, Alex raises 200, Jane calls, Dave calls")
    fun preFlopStory001() {
        val alex = aPlayer("Alex", 2000, strategyMock( raise(200)         ))
        val jane = aPlayer("Jane", 2000, strategyMock( call()             ))
        val dave = aPlayer("Dave", 2000, strategyMock( call(),     call() ))
        val table = Table(listOf(alex,jane,dave),2)
        val context = aContext(table, blinds(50,100))
        val dealer = PreFlopDealer(context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(300)
        assertThat(pot.payedBy(jane)).isEqualTo(300)
        assertThat(pot.payedBy(dave)).isEqualTo(300)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(CALL)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1700)
        assertThat(jane.stack).isEqualTo(1700)
        assertThat(dave.stack).isEqualTo(1700)
    }
    @Test
    @DisplayName("Pre flop (50/100) story: Dave raises 300, Alex calls, Jane folds")
    fun preFlopStory002() {
        val alex = aPlayer("Alex", 2000, strategyMock( call() ))
        val jane = aPlayer("Jane", 2000, strategyMock( fold() ))
        val dave = aPlayer("Dave", 2000, strategyMock( raise(300) ))
        val table = Table(listOf(alex,jane,dave),2)
        val context = aContext(table, blinds(50,100))
        val dealer = PreFlopDealer(context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(400)
        assertThat(pot.payedBy(jane)).isEqualTo(100)
        assertThat(pot.payedBy(dave)).isEqualTo(400)
        assertThat(alex.status).isEqualTo(CALL)
        assertThat(jane.status).isEqualTo(FOLD)
        assertThat(dave.status).isEqualTo(RAISE)
        assertThat(alex.stack).isEqualTo(1600)
        assertThat(jane.stack).isEqualTo(1900)
        assertThat(dave.stack).isEqualTo(1600)
    }
    @Test
    @DisplayName("Pre flop (50/100) story: Dave raises 300, Alex raises 300, Jane folds, Dave calls")
    fun preFlopStory003() {
        val alex = aPlayer("Alex", 2000, strategyMock( raise(300)         ))
        val jane = aPlayer("Jane", 50,   strategyMock( fold()             ))
        val dave = aPlayer("Dave", 2000, strategyMock( raise(300), call() ))
        val table = Table(listOf(alex,jane,dave),2)
        val context = aContext(table, blinds(50,100))
        val dealer = PreFlopDealer(context)

        val pot = dealer.collectPot()

        assertThat(pot.payedBy(alex)).isEqualTo(700)
        assertThat(pot.payedBy(jane)).isEqualTo(50)
        assertThat(pot.payedBy(dave)).isEqualTo(700)
        assertThat(alex.status).isEqualTo(RAISE)
        assertThat(jane.status).isEqualTo(ALL_IN)
        assertThat(dave.status).isEqualTo(CALL)
        assertThat(alex.stack).isEqualTo(1300)
        assertThat(jane.stack).isEqualTo(0)
        assertThat(dave.stack).isEqualTo(1300)
    }
    @Test
    @DisplayName("Pre flop (50/100) story: Alex calls, Jane raises 300, Alex folds")
    fun preFlopStory004() {
        val alex = aPlayer("Alex", 2000, strategyMock( call(),     fold() ))
        val jane = aPlayer("Jane", 2000, strategyMock( raise(300)         ))
        val table = Table(listOf(alex,jane),0)
        val context = aContext(table, blinds(50,100))
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
        val alex = aPlayer("Alex", 2000, strategyMock( call()             ))
        val juno = aPlayer("Juno", 2000, strategyMock( fold()             ))
        val dave = aPlayer("Dave", 2000, strategyMock( fold()             ))
        val jane = aPlayer("Jane", 2000, strategyMock( call(),     call() ))
        val eric = aPlayer("Eric", 2000, strategyMock( raise(2000)        ))
        val table = Table(listOf(alex,juno,dave,jane,eric),4)
        val context = aContext(table, blinds(50,100))
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
        val alex = aPlayer("Alex", 2000, strategyMock( fold() ))
        val jane = aPlayer("Jane", 2000, strategyMock( fold() ))
        val dave = aPlayer("Dave", 2000, strategyMock( fold() ))
        val table = Table(listOf(alex,jane,dave),2)
        val context = aContext(table, blinds(50,100))
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
        val alex = aPlayer("Alex", 50,   strategyMock( fold() ))
        val jane = aPlayer("Jane", 140,  strategyMock( fold() ))
        val dave = aPlayer("Dave", 9000, strategyMock( call() ))
        val table = Table(listOf(alex,jane,dave),2)
        val context = aContext(table, blinds(60,120))
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
        val alex = aPlayer("Alex", 2000, strategyMock( fold() ))
        val jane = aPlayer("Jane", 50,   strategyMock( fold() ))
        val dave = aPlayer("Dave", 40,   strategyMock( call() ))
        val table = Table(listOf(alex,jane,dave),2)
        val context = aContext(table, blinds(100,200))
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
        val payments = blinds(10,20)
        val alexContexts = mutableListOf<PlayerGameContext>()
        val janeContexts = mutableListOf<PlayerGameContext>()
        val alexStrategy = strategyMock(alexContexts,   call(),    fold() )
        val janeStrategy = strategyMock(janeContexts,   raise(100)        )
        val alex = aPlayer("Alex", 2000, alexStrategy)
        val jane = aPlayer("Jane", 2000, janeStrategy)
        val table = Table(listOf(alex,jane),0)
        val context = aContext(table, payments)
        val dealer = PostFlopDealer(buildPot(),context)

        dealer.collectPot()

        assertThat(alexContexts).hasSize(2)

        assertThat(alexContexts[0].board).isInstanceOf(EmptyBoard::class)
        assertThat(alexContexts[0].hero).isEqualTo(alex)
        assertThat(alexContexts[0].payments).isEqualTo(payments)
        assertThat(alexContexts[0].potAmount).isEqualTo(0)
        assertThat(alexContexts[0].history).isEmpty()
        assertThat(alexContexts[0].table.button).isEqualTo(0)
        assertThat(alexContexts[0].table.players).extracting({ it.player },{ it.stack },{ it.state })
                .containsOnly(Triple(alex.player,2000,NONE),
                              Triple(jane.player,2000,NONE))

        assertThat(janeContexts).hasSize(1)
        assertThat(janeContexts[0].board).isInstanceOf(EmptyBoard::class)
        assertThat(janeContexts[0].hero).isEqualTo(jane)
        assertThat(janeContexts[0].payments).isEqualTo(payments)
        assertThat(janeContexts[0].potAmount).isEqualTo(0)
        assertThat(janeContexts[0].history).extracting({ it.player },{ it.action::class },{ it.action.getAmount() } )
                .containsOnly(Triple(alex.player,CallAction::class,null))
        assertThat(janeContexts[0].table.button).isEqualTo(0)
        assertThat(janeContexts[0].table.players).extracting({ it.player },{ it.stack },{ it.state })
                .containsOnly(Triple(alex.player,2000,CALL),
                              Triple(jane.player,2000,NONE))

        assertThat(alexContexts[1].board).isInstanceOf(EmptyBoard::class)
        assertThat(alexContexts[1].hero).isEqualTo(alex)
        assertThat(alexContexts[1].payments).isEqualTo(payments)
        assertThat(alexContexts[1].potAmount).isEqualTo(100)
        assertThat(alexContexts[1].history).extracting({ it.player },{ it.action::class },{ it.action.getAmount() } )
                .containsOnly(Triple(alex.player,CallAction::class,null),
                              Triple(jane.player,RaiseAction::class,100))
        assertThat(alexContexts[1].table.button).isEqualTo(0)
        assertThat(alexContexts[1].table.players).extracting({ it.player },{ it.stack },{ it.state })
                .containsOnly(Triple(alex.player,2000,CALL),
                              Triple(jane.player,1900,RAISE))


    }

}
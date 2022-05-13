package org.agrfesta.k.kards.texasholdem.observers

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BroadcasterImplTest {

    @Test
    @DisplayName("""constructor(): instantiating a new broadcaster -> the broadcaster has no subscribers""")
    fun Test() {
        val broadcaster = BroadcasterImpl<Int>()

        broadcaster.notify {
            assert(false) { "There should be no subscriber to notify!" }
        }
    }

    @Test
    @DisplayName("""notify(): executing a broadcast notify -> all subscribers are notified""")
    fun Test0() {
        val broadcaster = BroadcasterImpl<DealerObserver>()
        val subscriber = mockk<DealerObserver>(relaxed = true)
        broadcaster.subscribe(subscriber)

        broadcaster.notify { it.notifyAction(GAME_CONTEXT, playerAction) }

        verify(exactly = 1) { subscriber.notifyAction(GAME_CONTEXT, playerAction) }
    }

    @Test
    @DisplayName("""notify(): executing a broadcast notify -> only actual subscribers are notified""")
    fun Test1() {
        val broadcaster = BroadcasterImpl<DealerObserver>()
        val subscriberA = mockk<DealerObserver>(relaxed = true)
        val subscriberB = mockk<DealerObserver>(relaxed = true)
        broadcaster.subscribe(subscriberA)
        broadcaster.notify { it.notifyAction(GAME_CONTEXT, playerAction) }

        broadcaster.subscribe(subscriberB)
        broadcaster.notify { it.notifyAction(GAME_CONTEXT, playerAction) }

        verify(exactly = 2) { subscriberA.notifyAction(GAME_CONTEXT, playerAction) }
        verify(exactly = 1) { subscriberB.notifyAction(GAME_CONTEXT, playerAction) }
    }

}

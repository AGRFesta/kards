package org.agrfesta.k.kards.texasholdem.observers

import io.mockk.mockk
import io.mockk.verify
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aShowdownPlayerResult
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ShowdownBroadcasterTest {
    private val subscriberA = mockk<GameObserver>(relaxed = true)
    private val subscriberB = mockk<GameObserver>(relaxed = true)
    private val subscriberC = mockk<GameObserver>(relaxed = true)

    @Test
    @DisplayName("""notifyResult(): executing broadcast notifyResult -> all subscribers are notified""")
    fun notifyResult_allSubscribersAreNotified() {
        val result: Collection<ShowdownPlayerResult> = listOf(aShowdownPlayerResult())
        val broadcaster = ShowdownBroadcaster()
        broadcaster.subscribe(subscriberA)
        broadcaster.subscribe(subscriberB)
        broadcaster.subscribe(subscriberC)

        broadcaster.notifyResult(result)

        verify(exactly = 1) { subscriberA.notifyResult(result) }
        verify(exactly = 1) { subscriberB.notifyResult(result) }
        verify(exactly = 1) { subscriberC.notifyResult(result) }
    }

}

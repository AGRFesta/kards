package agrfesta.k.cards.texasholdem.draws

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.suits.ACE
import agrfesta.k.cards.playingcards.suits.FIVE
import agrfesta.k.cards.playingcards.suits.TEN
import agrfesta.k.cards.texasholdem.utils.FOUR_OFF
import agrfesta.k.cards.texasholdem.utils.ONE_OFF
import agrfesta.k.cards.texasholdem.utils.THREE_OFF
import agrfesta.k.cards.texasholdem.utils.TWO_OFF

/*
    An inside straight draw, or gutshot draw or belly buster draw, is a hand with four of the five cards needed
    for a straight, but missing one in the middle. For example, 9-x-7-6-5. An inside straight draw has four outs
    (four cards to fill the missing internal rank). Because straight draws including an ace only have four outs,
    they are also considered inside straight draws. For example, A-K-Q-J-x or A-2-3-4-x. The probability of catching
    an out for an inside straight draw is half that of catching an out for an outside straight draw.
 */

data class InsideStraightDraw(val top: Rank, val missing: Rank) : Draw {

  init {
    if (top < FIVE) {
      throw IllegalArgumentException("The minimum Inside Straight Draw top is FIVE, top: $top")
    }
    if (top == FIVE) {
      if (missing > top) {
        throw IllegalArgumentException("The missing Rank can't be greater than top: missing=$missing, top=$top")
      }
    } else if (missing >= top) {
      throw IllegalArgumentException(
          "The missing Rank can't be greater than or equal to top: missing=$missing, top=$top")
    }
    val tail = if (top == FIVE) ACE else top - FOUR_OFF
    if (top == ACE) {
      if (missing < TEN) {
        throw IllegalArgumentException(
            "The missing Rank can't be lesser than tail: missing=$missing, tail=$tail")
      }
    } else if (tail != ACE) {
      if (missing <= tail) {
        throw IllegalArgumentException(
            "The missing Rank can't be lesser than or equal to tail: missing=$missing, tail=$tail")
      }
    }
  }

  private fun stringIfMissing(rank: Rank) = if (rank == missing) "*" else rank.symbol().toString()
  override fun toString(): String = StringBuilder("[${top.symbol()}")
      .append(" ${stringIfMissing(top - ONE_OFF)}")
      .append(" ${stringIfMissing(top - TWO_OFF)}")
      .append(" ${stringIfMissing(top - THREE_OFF)}")
      .append(" ${stringIfMissing(top - FOUR_OFF)}]")
      .toString()
}



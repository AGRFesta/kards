package agrfesta.k.cards.playingcards.utils

class SimpleRandomGenerator: RandomGenerator {

    override fun nextInt(bound: Int) = (0 until bound).shuffled().first()

}
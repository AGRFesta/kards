package agrfesta.k.cards.texasholdem.rules

import agrfesta.kcards.playingcards.cards.Rank

val RANK_COMPARATOR: Comparator<Rank> = compareBy(Rank::ord).reversed()

//TODO test
//TODO doc
class OrderedRankListComparator: Comparator<List<Rank>> {
    override fun compare(o1: List<Rank>?, o2: List<Rank>?): Int {
        if (o1==null || o2==null) {
            throw IllegalArgumentException("Unable to compare null list of Rank")
        }
        if (o1.size != o2.size) {
            throw IllegalArgumentException("Unable to compare list of Rank with different size")
        }
        return (0 .. o1.size)
                .map { i -> RANK_COMPARATOR.compare(o1[i],o2[i]) }
                .find { c -> c != 0 } ?: 0
    }

}
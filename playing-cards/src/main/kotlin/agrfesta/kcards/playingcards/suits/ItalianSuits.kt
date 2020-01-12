package agrfesta.kcards.playingcards.suits

import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.cards.Seed

enum class ItalianRank(private val symbol: Char) : Rank {

    ASSO('A'),
    RE('K'),
    CAVALLO('H'),
    FANTE('J'),
    SETTE('7'),
    SEI('6'),
    CINQUE('5'),
    QUATTRO('4'),
    TRE('3'),
    DUE('2');

    override fun symbol() = symbol
    override fun ord() = ordinal
}

enum class ItalianSeed(private val symbol: Char) : Seed {

    SPADE('s'),
    COPPE('c'),
    DENARI('d'),
    BASTONI('b');

    override fun symbol() = symbol
    override fun ord() = ordinal
}
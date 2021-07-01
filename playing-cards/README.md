## Playing Kards 
[![Kotlin](https://img.shields.io/badge/Kotlin-1.3.61-orange.svg?style=flat&logo=kotlin&logoColor=orange)](http://kotlinlang.org)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

### Build your deck
The easiest way to create your deck is choosing the card suit.
Cards will be shuffled with MutableList<T>.shuffle().
```kotlin
val deck = buildDeck {
    withCardsFromSuit(FRENCH)
}
```
If you need a specific shuffler you can use the fluent builder:
```kotlin
val deck = buildDeck {
    withCardsFromSuit(FRENCH)
    shuffleWith(aShufflingService)
}
```
or if you want use your own cards:
```kotlin
val deck = buildDeck {
    withCards(cards)
}
```

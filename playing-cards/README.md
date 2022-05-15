## playing-cards

---
### How to build your deck
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

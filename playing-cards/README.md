## K-PlayingCards [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

### Build your deck
The easiest way to create your deck is choosing the card suit.
A deck will be created with a default configuration.

```kotlin
val deck = buildDeck(FRENCH)
```
If you need a specific configuration you can use the fluent builder:
```kotlin
val deck = deckBuilder()
            .shuffleWith(aShufflingService)
            .build(FRENCH)
```
or if you want use your own cards:
```kotlin
val deck = deckBuilder()
            .shuffleWith(aShufflingService)
            .build(cards)
```
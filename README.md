## K-TexasHoldem [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

### How to create a Player
A Player is defined by it's name and strategy.

```kotlin
val player = Player("name", strategy)
```

### How to create a Table
You can create a Table just providing the players, in this case the button will be in a random position.
Each Player's name must be unique.

```kotlin
val table = buildingTable()
        .withPlayer("Poly", 2000, polyStrategy)
        .withPlayer("Alex", 2000, alexStrategy)
        .withPlayer("Jane", 2000, janeStrategy)
        .build()
```

### How to create a Game
You can create a Game using the fluent-step builder.

```kotlin
val game = buildingAGame()
        .withPayments(100, 200)
        .withTable(aTable)
        .build()
```

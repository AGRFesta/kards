## K-TexasHoldem [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.5.31-violet.svg?style=flat&logo=kotlin&logoColor=violet)](http://kotlinlang.org)

### How to create a Player
A Player is defined by its name and strategy.

```kotlin
val player = Player("name", strategy)
```

### How to create a Table
You can create a Table just providing the players, in this case the button will be in a random position.
Each Player's name must be unique.

```kotlin
val table = buildTable {
    withPlayers(poly, alex, jane)
}
```

### How to create a Game
The only implementation of Game interface is GameImpl.

```kotlin
val game = GameImpl( 
    payments = blinds(100, 200), 
    table = aTable)
```

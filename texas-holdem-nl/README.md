## texas-holdem-nl

---
### How to play a single table tournament
First you need to define the tournament through the *TournamentDescriptor* specifying the players initial stack and the
"increasing" payments structure as follows:
```kotlin
val paymentsStructure = listOf(
    LevelPayments(sb = 20u, bb = 40u),
    LevelPayments(sb = 100u, bb = 200u),
    LevelPayments(sb = 100u, bb = 200u, ante = 10u)
)
val paymentsDefinition = IncreasingGamePaymentsDefinition(structure = paymentsStructure, gamesPerLevel = 10u)
val tournamentDescriptor = TournamentDescriptorImpl(initialStack = 2000u, paymentsDefinition = paymentsDefinition)
```
Surprisingly you'll need at least two players to instantiate your tournament:
```kotlin
val players = setOf(maria, alex)
val tournament = TournamentImpl(descriptor = tournamentDescriptor, subscriptions = players)
```
That's all, now you can start the tournament invoking the method *play()*, when the tournament is over will return the
final result.
```kotlin
val result = tournament.play()
```
---
### How to set up a player
There are multiples way to represent a player depending on the context but in all cases is identified by a UUID.  
So you first need to define the identity of player choosing a UUID (the default is a random UUID) and a name:
```kotlin
val playerIdentity = PlayerIdentityImpl(name = "PlayerName") // the UUID has a random value
```
During a game the dealer will interact with players querying them for an action, when is their turn, using the *Player* 
function:  
```kotlin
fun act(hero: OwnPlayer, context: GameContext): Action
```
So this function's implementation represents the player's strategy.
You can finally instantiate a *Player* simply providing the strategy:  
```kotlin
val strategy: (OwnPlayer, GameContext) -> Action // player's strategy definition
val player = playerIdentity playingAs strategy
```

---

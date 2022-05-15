## Kards 
[![Kotlin](https://img.shields.io/badge/Kotlin-1.6.10-violet.svg?style=flat&logo=kotlin&logoColor=violet)](http://kotlinlang.org)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-lightblue.svg)](https://www.gnu.org/licenses/gpl-3.0)
---
### Introduction
Texas hold 'em no limit project and more.  
This is a 'toy' project created in early 2020 when I started learning Kotlin, the focus was on experiment Kotlin 
features, now that is a bit more mature can be used in small applications.  
Initially I created different repositories 
that I finally decided to merge as modules in a single one.

---
### Modules
More information are available in module's readme file.
* #### playing-cards
    Contains all you need to represent a deck of playing cards (at moment French or Italian), do not depend on any specific 
cards game so can be used to implement any.

* #### texas-holdem-nl
    Can be used to create a game or a full single table tournament of Texas hold 'em no limit.

* #### texas-holdem-nl-testing
    Contains a collection of builders and objects mothers for objects from the other modules.

* #### cli-evaluation-report
    Simple CLI to generate a report on the Evaluator, it will simply test the Evaluator against any possible combination of 
5, 6 or 7 cards.
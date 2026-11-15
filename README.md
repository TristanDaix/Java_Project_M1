# Java Project M1 - ISEN Holy Grail War

A turn-based, console RPG inspired by the *Fate* universe (Fate/Stay Night, Fate/Extra, Fate/Grand Order, ...).
Built in Java 24 with Maven for the JUNIA M1 Object-Oriented Programming final project.

## 1. Purpose of the application

You pick one Servant (a Heroic Spirit) and fight your way through a roster of **6 enemy Servants**,
each of a different class from yours. Every fight is a turn-based duel where both sides choose an action simultaneously
and the engine resolves how the two actions interact (an attack can be blocked and countered, a break beats a defense,
etc. ([see rules bellow](#combat-rules))).

Learn your opponent pattern (*<small>or cheat by looking at the data files</small>*) and win all 6 fights to gain the
Holy Grail. If your Servant falls, the game is over.

## 2. How to run it

### Requirements

- JDK 24 (configured in `pom.xml`)
- Maven 3.9+

### Run

Run `game.fate.Main` directly from your IDE.

### Run the tests

Right click on `src/test/java` and click `Run 'Tests in 'java''` directly from your IDE.

## 3. Main features

- Main menu (start / quit) and a Servant selection menu with a confirmation step.
- 7 Servant classes with different base statistic: Saber, Archer, Lancer, Rider, Caster, Assassin, Berserker.
- Every Servant has HP, MP, ATK, a critical rate and a critical multiplier, all loaded from CSV files.
- Basic actions for everyone: **Attack**, **Defense**, **Break** (all cost 0 MP).
- One unique **skill** and **Noble Phantasm (NP)** per Servant.
- Action interaction rules resolved by a combat engine.
- Enemy AI that follows a predefined action pattern loaded from a CSV file, with a fallback when the desired action is
  unaffordable.
- Experience and level-up: each victory gives 100 EXP, and every 100 EXP raises the level, which boosts ATK, max HP and
  max MP by 20%.
- Full HP/MP restoration between fights.
- Enemy difficulty scaling: the n-th enemy in the roster is levelled up n-1 times before the fight.
- Colored console output (ANSI) with damage, healing, MP, critical-hit and action reporting.

### Combat rules

| Player action                                    | Enemy action | Result                                             |
|--------------------------------------------------|--------------|----------------------------------------------------|
| Attack                                           | Break        | Attack succeeds, Break does nothing                |
| Break                                            | Defense      | Break succeeds, Defense fails                      |
| Defense                                          | Attack       | Attack is blocked and the defender counter-attacks |
| Anything else (NP, Skill, Attack vs Attack, ...) |              | Both actions execute independently                 |

### Class skills and Noble Phantasms

| Class     | Skill                                    | MP |
|-----------|------------------------------------------|----|
| Saber     | Mana Burst (big damage)                  | 20 |
| Archer    | Clairvoyance (guaranteed critical hit)   | 15 |
| Lancer    | Battle Continuation (heals)              | 15 |
| Rider     | Voyager of the Storm (big damage)        | 20 |
| Caster    | High-Speed Divine Words (restores MP)    | 0  |
| Assassin  | Presence Concealment (fixed damage)      | 10 |
| Berserker | Valor (big damage at the cost of 100 HP) | 15 |

| Servant ID | Noble Phantasm                                           | MP |
|------------|----------------------------------------------------------|----|
| 1          | Excalibur (very big damage)                              | 50 |
| 2          | Unlimited Blade Works (1000 hit with small damage)       | 40 |
| 3          | Gae Bolg (instant kill under 30% HP)                     | 30 |
| 4          | Bellerophon (damage + lifesteal)                         | 40 |
| 5          | Rule Breaker (damage + steals MP)                        | 40 |
| 6          | Tsubame Gaeshi (triple hit with guaranteed critical hit) | 50 |
| 7          | Nine Lives (very big damage)                             | 50 |

## 4. Project structure and main classes

```
src/main/java/game/fate/
├── Main.java                         Entry point, main menu loop
├── exceptions/                       Custom exceptions
├── models/
│   ├── MasterServantPair.java        Binds a Master to a Servant
│   ├── actions/                      Action interface + ActionType enum
│   │   ├── basics/                   AttackAction, DefenseAction, BreakAction
│   │   ├── skills/                   One class skill per Servant class
│   │   └── noblephantasms/           One NP per Servant ID
│   ├── dto/                          ActionResult, LevelUpResult (immutable records)
│   ├── interactionRules/             InteractionRule interface + AttackVsBreak / BreakVsDefense / DefenseVsAttack
│   ├── master/                       Master interface + Human / Pattern AI / Random AI
│   └── servant/                      Servant, ServantClass enum
├── services/
│   ├── GameManager.java              Game lifecycle, enemy roster, fight loop
│   ├── CombatEngine.java             Resolves one turn using the interaction rules
│   ├── ConsoleManager.java           All console input/output
│   └── factories/ServantFactory.java Builds a fully equipped Servant
└── utils/CsvReader.java              Loads classes, servants and patterns from CSV

src/main/resources/                   classes.csv, servants.csv, patterns.csv
src/test/java/                        JUnit tests (mirrors src/main/java)
```

| Class                   | Role                                                                                                                            |
|-------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| `Servant`               | Core entity: stats, HP/MP management, leveling, list of available actions                                                       |
| `ServantClass`          | Enum holding class modifiers (sort order, ATK multiplier, crit multiplier, crit rate), overridden at startup from `classes.csv` |
| `Action` / `ActionType` | Command interface for everything a Servant can do in combat                                                                     |
| `Master`                | Decision-making abstraction (human or AI)                                                                                       |
| `MasterServantPair`     | Combines a Master and a Servant, delegates command selection                                                                    |
| `InteractionRule`       | Strategy-like abstraction describing how two action types interact                                                              |
| `CombatEngine`          | Applies the matching rule, or executes both actions independently                                                               |
| `GameManager`           | Orchestrates loading, roster generation, fights and progression                                                                 |
| `ConsoleManager`        | Presentation layer (menus, colored output, safe integer input)                                                                  |
| `ServantFactory`        | Creates Servants and attaches basic actions, the class skill and the NP                                                         |
| `CsvReader`             | Parses the CSV files into game objects                                                                                          |

## 5. Technical decisions

1. **Actions as a command interface (`Action`).** Attacks, skills and NPs all implement one interface returning an
   `ActionResult`. Adding a new move means adding a class, with no change to the engine or the menus.
2. **Interaction rules as separate classes (`InteractionRule`).** Each interaction (Attack vs Break, Break vs Defense,
   Defense vs Attack) is its own rule object registered in `CombatEngine`. This replaces a large `if/else` and lets us
   add a new rule without editing the existing ones.
3. **DTO records for results (`ActionResult`, `LevelUpResult`).** Game logic returns immutable data and never prints.
   `ConsoleManager` is the only class that formats output, so the logic stays testable and the UI can be replaced.
4. **Data-driven game balance.** Servant stats, class modifiers and enemy behavior live in CSV files, so rebalancing or
   adding a Servant does not require touching the logic (a new NP or skill still needs a class and a line in
   `ServantFactory`).
5. **Master abstraction for human and AI.** The fight loop does not know whether a side is a person or an AI: it calls
   `issueCommand()` on a `MasterServantPair`.

## 6. Where inheritance is used

- The 4 custom exceptions extend `RuntimeException`.
- The type hierarchy of the game itself is built mostly with **interfaces** (`Action`, `Master`, `InteractionRule`), and
  composition was more relevant than inheritance: a Servant *has* a list of actions, and a pair *has* a Master and a
  Servant for example.

## 7. Where polymorphism is used

- `Servant.getAvailableActions()` returns a `List<Action>` mixing `AttackAction`, `DefenseAction`, `BreakAction`, class
  skills and NPs. The engine and menus call `getName()`, `getMpCost()`, `getType()` and `execute(...)` without knowing
  the concrete class, and each action behaves differently.
- `MasterServantPair.issueCommand()` calls `Master.selectAction(...)`, which behaves differently for `HumanMaster` (
  console prompt), `PatternAIMaster` (CSV pattern) and `RandomAIMaster` (random).
- `CombatEngine` iterates over a `List<InteractionRule>` and calls `matches(...)` / `resolve(...)` on each.

## 8. Where interfaces and abstract types are used

| Type              | Kind      | Why                                                                                                                     |
|-------------------|-----------|-------------------------------------------------------------------------------------------------------------------------|
| `Action`          | Interface | A contract ("something a Servant can do in combat") shared by very different behaviors, with no shared state to inherit |
| `Master`          | Interface | Separates the decision-making strategy (human / AI) from the Servant                                                    |
| `InteractionRule` | Interface | Lets the engine treat all interaction rules uniformly (open for extension)                                              |

Interfaces was chosen rather than an abstract class because the implementations share a capability and not state or an
identity.

## 9. Custom exceptions

All are unchecked (`RuntimeException`) because they signal invalid data or an invalid game setup, and are caught at the
application boundary.

| Exception                     | Thrown when                                                                                    |
|-------------------------------|------------------------------------------------------------------------------------------------|
| `UnknowServantClassException` | A CSV row references a class that is not in `ServantClass`                                     |
| `UnknowActionException`       | A pattern CSV row contains an action that is not in `ActionType`                               |
| `UnavailablePatternException` | A pattern is empty, or no pattern exists for an enemy Servant                                  |
| `NotEnoughServantsException`  | There is no Servant to choose from, or fewer than 7 distinct enemy classes to build the roster |

`CsvReader` also catches `IOException`, `NumberFormatException` and `ArrayIndexOutOfBoundsException` to report malformed
or missing files without crashing.

## 10. How the application loads its data

When the player starts a game, `GameManager.start()` calls `CsvReader` on three files in `src/main/resources/`:

| File           | Content                                                                  | Used for                                                           |
|----------------|--------------------------------------------------------------------------|--------------------------------------------------------------------|
| `classes.csv`  | className, sortOrder, attackMultiplier, criticalMultiplier, criticalRate | `ServantClass.setStats(...)`: class modifiers and display order    |
| `servants.csv` | id, name, servantClass, hp, mp, atk, criticalMultiplier, criticalRate    | Builds Servants through `ServantFactory`                           |
| `patterns.csv` | servantId, action1 ... action10                                          | Maps a Servant ID to its looping `ActionType[]` pattern for the AI |

The first line of each file is a header and is skipped, and blank lines are ignored.

## 11. How SOLID principles are applied

- **Single Responsibility:** `CsvReader` parses files, `ServantFactory` builds Servants, `CombatEngine` resolves turns,
  `ConsoleManager` handles all I/O, `GameManager` orchestrates the game flow, and the DTO records only carry data.
- **Open/Closed:** new moves (`Action`), new interaction behaviors (`InteractionRule`) and new AI types (`Master`) are
  added by creating a new class, without modifying the engine.
- **Liskov Substitution:** any `Action`, `Master` or `InteractionRule` implementation can replace another without the
  caller needing to know the concrete type.
- **Interface Segregation:** `Action`, `Master` and `InteractionRule` are small, focused interfaces; no implementation
  has to provide an unsupported method.
- **Dependency Inversion:** `MasterServantPair` depends on `Master`, `CombatEngine` on `InteractionRule`, and `Servant`
  on `Action`, all abstractions.

## 12. Known limitations

- `DefenseAction` does not yet reduce damage from skills or NPs (should add a defense statistic and status effect with
  cooldown and reduce the damage in `Servant.takeDamage()`).
- CSV parsing is a simple `split(",")` and does not support quoted fields.
- `RandomAIMaster` is kept but deprecated in favor of `PatternAIMaster`.
- Nothing happen when the player win, the main menu just reappear (should add the possibility to make a wish after
  winning the Holy Grail).

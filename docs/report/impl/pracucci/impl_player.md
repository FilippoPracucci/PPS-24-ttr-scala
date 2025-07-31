---

title: Player
nav_order: 2
parent: Implementazione Pracucci Filippo

---

# Implementazione giocatore

```mermaid
---
config:
  class:
    hideEmptyMembersBox: true
---
classDiagram
    TrainCars "*" --* "1"  Player
    PlayerColor "1" --* "1" Player: identifica
    ObjectiveCompletion "1" --* "1" Player
    ObjectiveCompletion --|> Objective
    BasicObjective ..|> Objective
    ObjectiveWithCompletion --|> BasicObjective
    ObjectiveWithCompletion ..|> ObjectiveCompletion
    class Player {
        <<trait>>
        + id: PlayerId
        + objective: ObjectiveCompletion
        + hand: Hand
        + trains: Trains
        + score: Points
        + drawCards(n: Int) Either[GameError, Unit]
        + canPlayCards(color: Color, n: Int) Boolean
        + playCards(color: Color, n: Int) Either[GameError, Unit]
        + placeTrains(n: Int) Either[GameError, Unit]
        + addPoints(points: Points) Unit
    }
    class PlayerColor {
        <<enumeration>>
        RED
        BLUE
        YELLOW
        GREEN
    }
    class TrainCars {
        <<trait>>
        + trainCars: Int
        + placeTrainCars(n: Int) Unit
    }
    class Objective {
        <<trait>>
        + citiesToConnect: (City, City)
        + points: Points
    }
    class ObjectiveCompletion {
        <<trait>>
        + completed: Boolean
        + markAsComplete() Unit
    }
```

## Player

Il `Player` viene identificato tramite un `PlayerId` che corrisponde ad un `PlayerColor`, ovvero uno dei colori
riguardanti i giocatori. Inoltre gestisce le azioni che potrebbero fallire a causa di requisiti non soddisfatti (come
quantità di vagoni non sufficienti, etc.), quindi i metodi `drawCards`, `playCards` e `placeTrains`, utilizzando come
tipo di ritorno `Either[GameError, Unit]`. In questo modo nel caso in cui non sia possibile portare a termine il metodo
verrà restituito un `GameError`, nello specifico uno degli oggetti che estende il trait `GameError` e che rappresenta
una tipologia di errore. Il metodo `drawCards` affida la responsabilità di pescare le carte dal `Deck`, ovvero di
chiamare il relativo metodo `draw`, al giocatore, in quanto è l'unico ad avere l'istanza del mazzo di gioco;
successivamente le carte pescate vengono aggiunte alla mano del giocatore, sempre tramite la sua chiamata del metodo
`addCards` di `Hand`. La scelta di affidare al `Player` l'istanza del `Deck` deriva dalla volontà di simulare le azioni
reali, dove è il giocatore a pescare fisicamente le carte dal mazzo di gioco per aggiungerle alla sua mano.

## Objective

Il trait `Objective` viene esteso dal trait `ObjectiveCompletion` con il concetto di completamento.
Si realizza un'implementazione base di `Objective`, ovvero `BasicObjective`; questa viene poi estesa
dall'implementazione `ObjectiveWithCompletion` con l'aggiunta di `ObjectiveCompletion` come **mixin**. Dunque la classe
`ObjectiveWithCompletion` consiste in una decorazione dell'implementazione base dell'obiettivo con il trait del
completamento. Il giocatore utilizzerà un obiettivo del tipo `ObjectiveCompletion`, per poter sfruttare tutte le
funzionalità e semplificare la gestione del completamento.

---

title: Controller
nav_order: 1
parent: Design di dettaglio

---

# Design Controller

```mermaid
---
config:
  class:
    hideEmptyMembersBox: true
---
classDiagram
    DrawCardsController <|-- GameController
    ClaimRouteController <|-- GameController
    TurnManager --o GameController
    GameState --o TurnManager
    class GameController {
        <<trait>>
        +showRules() Unit
    }
    class DrawCardsController {
        <<trait>>
        +drawCards() Unit
    }
    class ClaimRouteController {
        <<trait>>
        +claimRoute(connectedCities: (City, City)) Unit
    }
    class TurnManager {
        <<trait>>
        +currentPlayer: Player
        +switchTurn() Unit
        +gameState: GameState
    }
    class GameState {
        <<enumeration>>
    }
```

## GameController

Il trait `GameController` rappresenta il *controller* del gioco ed è quindi l'entità centrale con cui tutti comunicano.
Estende `DrawCardsController` e `ClaimRouteController` per includere le azioni di pescaggio ed occupazione di una
tratta. Inoltre contiene un `TurnManager`, il quale mantiene lo stato della partita tramite un `GameState` che viene
rappresentato tramite un'enumerazione dei possibili stati. Espone quindi il `GameState` corrente, oltre al giocatore di
turno e al metodo `switchTurn` che si occupa di cambiare il turno (come da
[requisito di sistema 2](../requirement_specification.md#requisiti-di-sistema)).
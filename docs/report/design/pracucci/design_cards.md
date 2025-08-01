---

title: Cards
nav_order: 1
parent: Design Pracucci Filippo

---

# Design carte

```mermaid
---
config:
  class:
    hideEmptyMembersBox: true
---
classDiagram
    Hand --|> Cards
    Color "1" --o "*" Card
    Card "1..*" --o "0..1" Cards
    Deck --|> Cards
    Hand "*" ..> "1" HandGenerator: «use»
    Deck "*" ..> "1" DeckGenerator: «use»
    Cards "*" ..> "1" CardsGenerator: «use»
    HandGenerator ..|> Generator~T~
    CardsGenerator ..|> Generator~T~
    DeckGenerator ..|> Generator~T~
    class Color {
        <<Enumeration>>
    }
    class Card {
        <<trait>>
        + color: Color
    }
    class Generator~T~ {
        <<trait>>
        + generateCards() List[Card]
        + generate() T
    }
    class CardsGenerator {
        <<abstract>>
    }
    class DeckGenerator {
        <<abstract>>
    }
    class HandGenerator {
        <<abstract>>
    }
    class Cards {
        + cards: List[Card]
    }
    class Deck {
        <<trait>>
        + shuffle() Unit
        + draw(n: Int) List[Card]
        + reinsertAtTheBottom(card: Card) Unit
    }
    class Hand {
        <<trait>>
        + playCards(color: Color, n: Int) List[Card]
        + addCards(cardsToAdd: List[Card]) Unit
    }
```

## Color

Consiste in un'enumerazione dei possibili colori delle carte vagone.

## Card

L'entità `Card`, intesa come carta vagone, è caratterizzata da un colore e viene modellata tramite un trait in modo da
consentire facilmente una sua estensione e/o decorazione per aggiungere caratteristiche e funzionalità. Dato che le
entità che ne necessitano, ovvero `Deck` e `Hand`, la utilizzano sotto forma di lista, si crea un'entità `Cards`, la
quale consiste appunto in una lista di carte. Dunque sia `Deck` che `Hand` sono delle estensioni di `Cards`.

## Generator

Si tratta di un generatore generico per `Cards` e chiunque aderisca al suo contratto. Le generazioni avvengono tramite:
- `generate`: per generare un'istanza del tipo passato come **type parameter**;
- `generateCards`: per generare una lista di carte.

La genericità consente di realizzare tre implementazioni diverse per `Cards`, per il `Deck` e per l'`Hand` del
giocatore.

## Deck

Il concetto di `Deck` rappresenta il mazzo di carte vagone, con il quale è possibile effettuare tre azioni:
- `shuffle`: mischiare le carte;
- `draw`: pescare un qualsiasi numero positivo di carte;
- `reinsertAtTheBottom`: reinserire una carta in fondo.

Il mazzo di carte viene creato per mezzo di un'istanza del `DeckGenerator`, ovvero l'implementazione di `Generator` per
il `Deck`.

## Hand

Il concetto di `Hand` rappresenta la mano del giocatore, la quale è composta da un insieme di carte vagone. Le azioni
che la riguardano consistono in:
- `playCards`: giocare una lista di carte presenti nella mano specificando il numero di carte di un determinato colore.
  Questo metodo viene utilizzato per occupare una tratta sulla mappa di gioco;
- `addCards`: aggiungere una lista di carte. Questo metodo viene usato per aggiungere alla mano del giocatore la lista
  di carte pescate dal mazzo.

La mano del giocatore viene creata sfruttando un'istanza del `HandGenerator`, ovvero l'implementazione di `Generator`
per `Hand`.

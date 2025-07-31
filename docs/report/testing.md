---

title: Testing
nav_order: 6
parent: Report

---

# Testing

## TDD

Come metodologia di sviluppo del software abbiamo adottato il **TDD** (Test-Driven-Development) per quanto riguarda il
*model*, per cui ogni funzionalità è stata implementata dopo aver creato un test che ne mostrasse l'utilizzo. Il
*controller* non è stato realizzato completamente in TDD, dato che alcune parti interagiscono con la *view*. Infatti, i
componenti di *view* creavano problemi nell'effettuare i test tramite l'action di GitHub. Per questa ragione i
componenti di *view*, qualora possibile, sono stati verificati singolarmente tramite l'avvio di una GUI esemplificativa.

## ScalaTest

Per effettuare i test del *model* è stata utilizzata la libreria **ScalaTest**, in particolare utilizzando `AnyFlatSpec`
e `Matchers` per rendere i test più leggibili. Un esempio di test è:
```scala 3
"A Player" should "be able to draw cards from the deck" in:
  val initialHandCards = player.hand
  player.drawCards(StandardNumberOfCardsToDraw) should be(Right(()))
  player.hand should be(Deck().cards.take(StandardNumberOfCardsToDraw) ++: initialHandCards)
  player.drawCards(Deck().cards.size + 1) should be(Left(NotEnoughCardsInTheDeck))
```

## Scoverage

Per misurare la copertura dei test abbiamo utilizzato il tool `Scoverage`, integrabile con SBT, tenendo conto
esclusivamente dei risultati ottenuti sul *model* ed escludendo casi limite derivanti dall'interazione con i file JSON
utilizzati. L'obiettivo minimo del 90% di coverage, indicato nella Definition of Done, è stato raggiunto.

[Scoverage Report](scoverage-report/index.html){: .btn .btn-blue}
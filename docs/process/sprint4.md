---

title: Sprint 4
nav_order: 6
parent: Process

---

# Sprint 4

Lo sprint ha una durata di una settimana: 12/07/2025 - 18/07/2025

## Obiettivo

L'obiettivo dello sprint 4 consiste nel rilascio della seconda release (`v0.2.0`), includendo la gestione dei turni dei
giocatori. Inoltre si gestiscono le carte obiettivo ed i punteggi dei giocatori; infine si implementano i controlli
relativi la condizione di fine partita e il completamento degli obiettivi.

## Sprint Backlog

| Feature                              | Sprint Task                                                             | Volontario | Stima iniziale | Iniziato | Completato |
|--------------------------------------|-------------------------------------------------------------------------|------------|:--------------:|:--------:|:----------:|
| /                                    | Fix errore nella GUI quando si esegue il jar su Linux                   | Entrambi   |       25       |    X     |     X      |
| /                                    | Rilascio release `v0.2.0`                                               | Entrambi   |       5        |    X     |     X      |
| Gestione delle carte obiettivo       | Modellazione delle carte obiettivo                                      | Pracucci   |       15       |    X     |     X      |
|                                      | Creazione e gestione file di configurazione per le carte obiettivo      | Bedeschi   |       10       |    X     |     X      |
|                                      | Visualizzazione obiettivo del giocatore di turno                        | Pracucci   |       5        |    X     |     X      |
| Controllo condizione di fine partita | Implementazione del controllo di fine partita ad ogni turno             | Pracucci   |       5        |    X     |     X      |
|                                      | Visualizzazione inizio ultimo round e schermata di fine partita         | Pracucci   |       10       |    X     |     X      |
| Gestione punteggio dei giocatori     | Implementazione calcolo punteggio relativo all'occupazione delle tratte | Bedeschi   |       10       |    X     |     X      |
|                                      | Visualizzazione punteggi attuali                                        | Bedeschi   |       5        |    X     |     X      |
| Controllo completamento obiettivi    | Implementazione logica del controllo completamento obiettivi            | Bedeschi   |       10       |    X     |     X      |

## Sprint Review

Tutti i task dello sprint sono stati completati. Nei prossimi sprint verrà considerata la possibilità di migliorare
alcune implementazioni. All'interno dello sprint sono state effettuate le release:
- `v0.1.1`: fix bug di visualizzazione su linux;
- `v0.2.0`: gestione dei turni dei giocatori;
- `v0.3.0`: aggiunta degli obiettivi dei giocatori;
- `v0.4.0`: aggiunta dell'assegnamento dei punti in seguito all'occupazione di una tratta e gestione del termine della
partita.

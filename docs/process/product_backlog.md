---

title: Product Backlog
nav_order: 1
parent: Process

---

# Product Backlog
Consideriamo ordinate per priorità le funzionalità di base del gioco, assegnando ad ognuna una stima iniziale, che
consiste in un valore compreso tra 1 e 100 che ne indica il peso specifico. Al termine di ogni sprint le stime verranno
rivalutate.

| Priorità | Feature                                                       | Stima iniziale | 1 |
|----------|---------------------------------------------------------------|----------------|---|
| 1        | Gestione della mappa di gioco                                 | 100            |   |
| 2        | Gestione del mazzo di carte e delle mani dei giocatori        | 75             |   |
| 3        | Come giocatore, poter pescare due carte                       | 15             |   |
| 4        | Come giocatore, poter utilizzare carte per posizionare vagoni | 50             |   |
| 5        | Gestione dei turni dei giocatori                              | 35             |   |
| 6        | Gestione delle carte obiettivo                                | 35             |   |
| 7        | Controllo condizione di fine partita                          | 10             |   |

[Decidere se spostare Definition of done nel processo di sviluppo e specificare la % di coverage]: #

## Definition of done
Dichiariamo una feature riguardante il model conclusa quando ha un % di coverage e tutti i test hanno successo, mentre
per le feature della view esse devono risultare utilizzabili e non ambigue. In entrambi i casi deve inoltre essere
presente anche la Scaladoc dove necessaria.


---

title: Retrospettiva
nav_order: 7
parent: Report

---

# Retrospettiva

## Processo di sviluppo

Il processo di sviluppo che ci eravamo preposti è stato rispettato, in quanto sono stati fatti sprint settimanali con
relativo **backlog** e review finale. Il [Product Backlog](../process/product_backlog.md) è stato mantenuto aggiornato
ad ogni sprint. Gli sprint sono stati così suddivisi:
1. [Sprint 1](../process/sprint1.md) 
2. [Sprint 2](../process/sprint2.md) 
3. [Sprint 3](../process/sprint3.md) 
4. [Sprint 4](../process/sprint4.md) 
5. [Sprint 5](../process/sprint5.md)

Ogni sprint è stato concluso rispettando le scadenze ed effettuando un incontro per effettuare la review dello sprint,
per poi programmare il successivo. La quasi totalità dei task assegnati ad ogni sprint è stata portata a termine
all'interno dello sprint stesso.

Il lavoro è stato svolto principalmente in maniera individuale, ad eccezione di alcune parti comuni che sono state
progettate e realizzate in collaborazione; lo stesso vale per la stesura della documentazione.

## Git Workflow

Il flusso di lavoro adottato è **GitFlow**, dove i branch sono stati così utilizzati:
- un branch `feature` per ogni feature, utilizzato solo dallo sviluppatore che aveva preso in carico il task ed in
totale ne sono stati creati 13. Ognuno di essi, una volta terminato, veniva chiuso tramite **pull request**
sul branch `develop`, la quale doveva essere accettata dall'altro componente;
- un branch `hotfix` per risolvere un bug relativo la visualizzazione della GUI su linux che era presente nel branch
`main`;
- un branch `release` per ogni release, seguendo il **Semantic Versioning**, il quale veniva chiuso sia su `main` che
`develop` tramite due **pull request** separate;
- un branch `refactor` in cui sono stati eseguiti i refactor finali.

Per quanto riguarda le **GitHub Actions**, sono state realizzate prima di iniziare lo sviluppo e questo ha aiutato
notevolmente l'integrità continua del software, grazie soprattutto al controllo della correttezza dei test ad ogni
**push** e alla coerenza di stile garantita dall'utilizzo di **Scalafmt**. Inoltre ha permesso anche la
**Continuous Delivery (CD)** grazie alla release automatica in seguito a **push** sul branch `main` con tag semantico
`v*.*.*`.

## Release incrementali

Seguendo un approccio in stile **Agile** sono state effettuate release incrementali, ognuna delle quali aggiungeva
funzionalità e/o fix di bug. Le release effettuate sono:
- `v0.1.0`: release iniziale che comprendeva la modalità _single player_ con possibilità di effettuare le azioni di
pescaggio e occupazione di una tratta;
- `v0.1.1`: fix del bug della GUI su linux (branch `hotfix/linux-view`);
- `v0.2.0`: aggiunta della modalita _multiplayer_, con conseguente gestione dei turni;
- `v0.3.0`: aggiunta degli obiettivi dei giocatori;
- `v0.4.0`: assegnamento dei punti ai giocatori quando occupano una tratta, gestione punti dei giocatori e gestione
dell'inizio dell'ultimo round e quindi di fine partita;
- `v1.0.0`: aggiunta del controllo di completamento degli obiettivi e conseguente assegnamento dei punti, classifica
finale dei giocatori e pulsante per visualizzare il regolamento. Inoltre sono stati fatti i fix relativi
all'assegnamento di obiettivi duplicati e crash in caso di pescaggio dal mazzo con carte insufficienti;
- `v1.1.0`: release finale che comprende tutti i piccoli aggiustamenti finali (branch `refactor`) e la documentazione
completa.

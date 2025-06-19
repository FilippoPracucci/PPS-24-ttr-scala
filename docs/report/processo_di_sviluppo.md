---

title: Processo di sviluppo
nav_order: 1
parent: Report

---

# Processo di sviluppo

Il processo di sviluppo adottato si ispira a Scrum, quindi è basato su sprint e task da realizzare.
Il team viene suddiviso in due ruoli: committente e product owner. Viene utilizzato un product backlog, aggiornato in
ogni sprint e scomposto negli sprint backlog. La durata degli sprint è settimanale, in cui vengono svolti i task
specificati nello sprint backlog, suddivisi tra i componenti, e al termine viene organizzato lo sprint successivo.

## Meeting iniziale

## Definition of done

## Sprint planning

## Versioning
Utilizziamo Git adottando il flusso di lavoro GitFlow e la seguente strategia:
- un branch main che contiene le release;
- un branch develop che rappresenta la linea principale di sviluppo;
- un branch feature per ogni funzionalità.

Adottiamo come Semantic Versioning il formato MAJOR.MINOR.PATCH, partendo dalla versione 0.1.0, dopo l'initial commit.
Ogni versione è taggata tramite Git e ne attiva il rilascio automatico tramite GitHub Actions.

## Workflow

La gestione e il deployment del progetto sfruttano tecniche di Continuous Integration/Delivery, nello specifico le 
GitHub Actions tramite la creazione di workflows.

- **Continuous Integration (CI)**: il workflow `test.yml` si occupa di avviare automaticamente i test (Scalatest, Scoverage
e Scalafmt) ad ogni push e pull request. In questo modo si assicura l'integrità del progetto durante tutto il suo
processo di sviluppo.
- **Continuous Delivery (CD)**: il workflow `release.yml` ha lo scopo di effettuare il rilascio automatico del progetto,
solo in caso tutti i test abbiano successo. Si attiva in caso di push sul branch main con tag semantico "v*.\*.\*"
e produce un JAR eseguibile (ttr-scala.jar) utilizzando sbt assembly, il quale viene caricato come release su GitHub.

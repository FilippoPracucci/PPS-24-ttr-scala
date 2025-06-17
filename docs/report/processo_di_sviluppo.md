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
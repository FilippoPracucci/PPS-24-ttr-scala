---

title: Processo di sviluppo
nav_order: 1
parent: Report

---

# Processo di sviluppo

Il processo di sviluppo adottato si ispira a Scrum, quindi è basato su sprint e task da realizzare.
Viene utilizzato un **Product Backlog**, aggiornato in ogni sprint e scomposto negli **Sprint Backlog**.

## Ruoli

Il team viene suddiviso in due ruoli:

- **Committente e sviluppatore** (Federica Bedeschi): garantisce la qualità e usabilità del prodotto finale.  
- **Product Owner e sviluppatore** (Filippo Pracucci): gestisce la lista delle priorità e il coordinamento del flusso di lavoro.

## Sprint planning

Gli **sprint** avranno durata settimanale. In ognuno saranno definiti gli obiettivi da
raggiungere e tramite uno **Sprint Backlog** i task da svolgere, assegnati ai vari componenti. Al termine dello sprint
viene realizzata una revisione e si imposta quello successivo.

## Definition of done

[Inserire % di coverage]: #
Dichiariamo una feature riguardante il model conclusa quando ha un % di coverage e tutti i test hanno successo, mentre
per le feature della view esse devono risultare utilizzabili e non ambigue. In entrambi i casi deve inoltre essere
presente anche la Scaladoc dove necessaria.

## Documentazione

La documentazione viene realizzata in formato **Markdown**, contenuta nella directory `docs`, e pubblicata sotto forma di
**GitHub Pages**.

## Versioning

Utilizziamo Git adottando il flusso di lavoro **GitFlow** e la seguente strategia:
- un branch `main` che contiene le release;
- un branch `develop` che rappresenta la linea principale di sviluppo;
- un branch `feature` per ogni funzionalità.

Ogni funzionalità realizzata deve essere integrata nel branch `develop` tramite una **pull request**, la quale deve
essere controllata e approvata dall'altro membro del gruppo.

Adottiamo come Semantic Versioning il formato MAJOR.MINOR.PATCH, partendo dalla versione 0.1.0, dopo l'`initial commit`.
Ogni versione è taggata tramite Git e ne attiva il rilascio automatico tramite **GitHub Actions**.

## Workflow

La gestione e il deployment del progetto sfruttano tecniche di Continuous Integration/Delivery, nello specifico le 
GitHub Actions tramite la creazione di workflows.

- **Continuous Integration (CI)**: il workflow `test.yml` si occupa di avviare automaticamente i test (Scalatest, Scoverage
e Scalafmt) ad ogni push e pull request. In questo modo si assicura l'integrità del progetto durante tutto il suo
processo di sviluppo.
- **Continuous Delivery (CD)**: il workflow `release.yml` ha lo scopo di effettuare il rilascio automatico del progetto,
solo in caso tutti i test abbiano successo. Si attiva in caso di push sul branch main con tag semantico `v*.*.*`
e produce un JAR eseguibile (ttr-scala.jar) utilizzando sbt assembly, il quale viene caricato come release su GitHub.

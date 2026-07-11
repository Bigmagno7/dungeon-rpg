# 📌 Dungeon Wizard 

Gioco di ruolo a turni bidimensionale sviluppato in **Java** come progetto d'esame per il corso di **Metodologie di Programmazione** dell'Università degli Studi di Camerino.

Il giocatore veste i panni di un mago che esplora i livelli di un dungeon fantasy, affronta creature ostili con un sistema di combattimento tattico, raccoglie medikit e gestisce i salvataggi persistenti su file.

---

# 🚀 Come eseguire il progetto

## Prerequisiti

- Java 17 o superiore
- Gradle (incluso tramite Wrapper, non richiede installazione)

## Clonare il repository

```bash
git clone https://github.com/Bigmagno7/dungeon-rpg.git
cd dungeon-rpg
```

## Build del progetto

### Linux / macOS

```bash
./gradlew build
```

### Windows (PowerShell o Prompt)

```powershell
.\gradlew build
```

## Avviare il gioco

### Linux / macOS

```bash
./gradlew run
```

### Windows (PowerShell o Prompt)

```powershell
.\gradlew run
```

---

# 🎮 Meccaniche di Gioco

## ⚔️ Bump Combat (Attacco Direzionale)

Il sistema di combattimento è integrato direttamente nel movimento.

Non esistono pulsanti o menu dedicati all'attacco: è sufficiente muoversi verso la casella occupata dall'Orco per colpirlo. Se il mostro sopravvive, effettua immediatamente un contrattacco.

---

## 🤖 IA con Pathfinding BFS

Il mostro non si muove casualmente.

Ad ogni turno calcola il percorso minimo verso l'eroe utilizzando l'algoritmo **Breadth First Search (BFS)**, aggirando muri e ostacoli.

---

## 🌫️ Nebbia di Guerra (Fog of War)

Dal **Livello 3** la visibilità viene drasticamente ridotta.

Sono visibili solamente le caselle adiacenti all'eroe, aumentando la difficoltà dell'esplorazione.

---

## 📈 Progressione dei Livelli

Il gioco è composto da **5 livelli**.

Con l'avanzare dei livelli aumentano progressivamente:

- HP massimi del mostro
- Danni inflitti
- Difficoltà generale

---

## 💾 Persistenza

È possibile salvare e ricaricare la partita.

Il salvataggio utilizza:

- file binari `.dat`
- serializzazione Java (`Serializable`)

Vengono memorizzati:

- mappa
- posizioni
- HP
- stato della partita
- log dei combattimenti

---


# 🤖 Utilizzo dell'Intelligenza Artificiale

Durante lo sviluppo sono stati utilizzati strumenti di Intelligenza Artificiale esclusivamente come supporto allo sviluppo e non come sostituzione del lavoro personale.

L'AI è stata impiegata per:

- supporto al refactoring secondo il pattern MVC;
- applicazione dei principi SOLID;
- risoluzione di problemi relativi a Gradle e JavaFX;
- debugging dell'interfaccia grafica;
- generazione di bozze logiche successivamente comprese, modificate e validate manualmente.

Tutto il codice presente nel progetto è stato studiato, verificato, testato e integrato personalmente prima del commit sulla repository.

---

# 🌿 Versionamento (Git & GitHub)

Lo sviluppo del progetto è stato documentato tramite **61 commit**, seguendo un'evoluzione incrementale.

Sono stati utilizzati:

## Messaggi descrittivi

Per documentare:

- correzione bug
- miglioramenti della logica
- rifiniture grafiche

Esempi:

- correzione calcolo danni
- fix BFS
- attivazione della Fog of War

---

## Commit Semantici

Nella fase finale sono stati adottati prefissi standard:

- **fix:** correzione al codice
- **feat:** nuove funzionalità
- **refactor:** modifiche strutturali senza cambiamenti funzionali
- **style:** miglioramenti grafici e pulizia del codice

---

# 👤 Sviluppatore

**Lorenzo Magnoni**

Studente del Corso di Laurea in Informatica presso l'Università degli Studi di Camerino.

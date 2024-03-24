# Caso di studio - Metodi Avanzati di Programmazione 22-23
***NURJA Luan Abdurahman, REGGIO Francesco Maria, SANTOVITO Leonardo***

### Introduzione
Il caso di studio consiste nella realizzazione di una avventura testuale nel linguaggio Java, con l'obiettivo di applicare i concetti di programmazione orientata agli oggetti e di utilizzare gli argomenti spiegati durante il corso di Metodi Avanzati di Programmazione.

Il programma risultante è un gioco testuale, con interazione da linea di comando, in cui il giocatore impersona un personaggio che si trova in un mondo virtuale e deve interagire con esso per risolvere enigmi e proseguire nella storia.

## Descrizione generale del caso di studio

### Introduzione al gioco Starship Exodus

*Terra, anno 2030, la crisi energetica è la priorità di tutti i governi mondiali. Le discordie tra i paesi produttori di petrolio e le guerre rendono la situazione sempre più fuori controllo. L'Unione Europea si sta mobilitando per cercare una soluzione a questa crisi, allocando ingenti risorse nella ricerca e sviluppo di nuove fonti di energia rinnovabili. 
L'Agenzia Spaziale Europea (ESA) ricerca nello spazio nuove forme di energia e ha scoperto un pianeta potenzialmente abitabile, denominato 'Eden', situato ad una distanza incredibilmente vicina alla Terra. Viene organizzata una spedizione di due astronauti per esplorare il pianeta e cercare di capire se è possibile trasferirvi una cerchia di eletti.
Dieci anni dopo, lo shuttle è quasi giunto a destinazione...*

In **Starship Exodus**, il giocatore dovrà impersonare uno dei due astronauti della spedizione spaziale in arrivo verso 'Eden'. Sul pianeta, però, non ci metterà mai piede. Il giocatore si ritroverà improvvisamente in una astronave aliena, e utilizzare tutti i mezzi a propria disposizione per sopravvivere, con la speranza di tornare a casa.

La mappa del gioco è strutturata in stanze comunicanti, ognuna delle quali rappresenta un luogo all'interno dell'astronave aliena. All'interno delle stanze possono esserci degli oggetti che il giocatore potrebbe raccogliere e utilizzare per risolvere enigmi e sfide. Alcune stanze potrebbero essere bloccate e richiedere l'utilizzo di oggetti specifici per poter essere aperte.
Il giocatore può muoversi tra le stanze, esaminare oggetti, raccoglierli, usarli per risolvere enigmi e sfide e quindi per poter proseguire nella storia. L'obiettivo finale del gioco è trovare un modo di fuggire dall'astronave aliena e tornare a casa.

![Mappa astronave](./resources/images/Map.png)

*Figura 1. Mappa che mostra la configurazione delle stanze del gioco.*

### Struttura del progetto

Il progetto è strutturato in package, ognuno dei quali contiene classi che implementano funzionalità specifiche del gioco. Di seguito è riportata la struttura del progetto, inclusa la sezione `resources` che contiene le risorse necessarie per il funzionamento del gioco.
    
    ```
    ├───resources
    │   ├───dialogs
    │   ├───files
    │   ├───images
    │   └───sounds
    ├───src
    │   ├───main
    │   │   └───java
    │   │       └───org
    │   │           └───example
    │   │               ├───api
    │   │               ├───database
    │   │               ├───gui
    │   │               ├───parser
    │   │               └───type
    │   └───test
    │       └───java
    ```
*Figura 2. Struttura del progetto 'Starship Exodus'.*

### Funzionalità del gioco
Il gioco è strutturato in modo da permettere al giocatore di interagire con l'ambiente circostante, con comandi appositi che garantiscono una interazione intuitiva e naturale.
Di seguito sono riportate le funzionalità principali del gioco:

- **Menu iniziale**: mediante un menu grafico iniziale, il giocatore può scegliere se creare una nuova partita, caricare una delle partite salvate, visualizzare informazioni sul gioco, oppure uscire. 
- **Movimento**: il giocatore può muoversi tra le stanze dell'astronave aliena usando i comandi `nord`, `sud`, `est` e `ovest`.
- **Esplorazione**: il giocatore può esaminare le stanze usando `osserva`, raccogliere eventuali oggetti presenti nelle stanze usando `prendi`, aprire oggetti contenitori con `apri`, con `usa` può usarli per risolvere enigmi e sfide.
- **Combattimento**: quando diventa necessario, il giocatore sarà costretto ad usare il comando `attacca` per combattere contro nemici presenti nell'astronave aliena.
- **Inventario**: utilizzando il comando `inventario` il giocatore può visualizzare la lista degli oggetti raccolti e la relativa descrizione.
- **Tempo**: il giocatore può tener traccia del tempo di gioco usando il comando `tempo`.
- **Aiuto**: con il comando `aiuto`, si può visualizzare una schermata con informazioni utili al giocatore.
- **Uscita**: il giocatore può uscire dal gioco in qualsiasi momento con `esci`, e gli verrà chiesto se vuole salvare o meno la partita corrente.
- **Salvataggio**: il giocatore può salvare la partita in qualsiasi momento e riprenderla successivamente con il comando `salva`.
- **Caricamento**: dal menu iniziale il giocatore può caricare una partita salvata in precedenza e riprenderla da dove l'aveva interrotta.
- **Vittoria**: il giocatore vince il gioco se trova il modo di fuggire dall'astronave aliena.

## Applicazione degli argomenti del corso nel progetto

Nel caso di studio 'Starship Exodus' sono stati impiegati i concetti di programmazione orientata agli oggetti e le conoscenze acquisite durante il corso di Metodi Avanzati di Programmazione.
Di seguito sono riportati gli argomenti trattati durante il corso e il relativo utilizzo nel progetto.

### Files per Input/Output

### Java Database Connectivity

I database sono integrati nel gioco per memorizzare i progressi del giocatore e permettere il salvataggio e il caricamento delle partite.
Il database è implementato utilizzando la tecnologia Java Database Connectivity (JDBC) per la connessione al database.
Si fa uso di H2 come database embedded permamentemente salvato su file, per garantire la persistenza dei dati anche dopo la chiusura del gioco.


In particolare, all'interno della classe `Database` sono implementati i metodi per la connessione al database, il salvataggio e il caricamento delle partite.
Della partita vengono salvate in `resources/files/savedgames` le seguenti informazioni:
- `id`: chiave primaria generata con un contatore `auto_increment` per identificare univocamente la partita.
- `gamedescription`: stringa di tipo BLOB (Binary Large Object) che contiene lo stato della partita.
- `creationdate`: timestamp di creazione del record nel database.
- `playername`: nome del giocatore che viene richiesto alla creazione di una nuova partita.

Quando viene creata una nuova partita mediante il metodo `newGame` nella classe `Engine`, viene creata una nuova riga nella tabella `games` del database con i dati relativi alla partita corrente, mediante il metodo `insertGame` della classe `Database`.
Il campo `gameId` all'interno della `GameDescription` viene inizialmente impostato a `-1`, e solamente aver inserito il record in tabella viene aggiornato con la chiave primaria `id` della riga corrispondente nel database.
In questo modo, se non viene salvata la partita, si potranno semplicemente eliminare le righe con il campo `gameId` uguale a `-1` all'interno della `GameDescription` per liberare spazio nel database.

Quando il giocatore salva una partita, viene invocato il metodo `saveGame` in `Engine`, che aggiorna il tempo di gioco salvato all'interno della `GameDescription`, e passa la `GameDescription` al metodo `updateGame` di `Database`. Quest'ultimo, a partire dal campo `gameId` dello stato del gioco, esegue una query di aggiornamento del record corrispondente nella tabella `games`, salvando su memoria permanente lo stato della partita.

Se il giocatore seleziona dal menu principale di caricare una partita salvata, viene mostrata una lista di tutte le partite salvate nella tabella `games` all'interno del database. Viene utilizzata la classe `GameRecord` che include solo le informazioni essenziali dei salvataggi, al fine di semplificare controlli e stampe in quanto non viene salvato in memoria l'intero `BLOB` della `GameDescription`.
Mediante la chiave primaria `id` della partita scelta, il giocatore seleziona la partita che desidera caricare, viene invocato il metodo `loadGame` nella classe `Database` che esegue una query sulla tabella `games` e restituisce la `GameDescription` corrispondente.

### Programmazione concorrente

Per quanto riguarda la programmazione concorrente, il caso di studio utilizza la classe `Thread` per gestire il timer di gioco all'interno della classe `GameTimer`. Questa classe estende la classe `Thread` e implementa un meccanismo per aggiornare il tempo di gioco ogni secondo.

La variabile `secondsElapsed` tiene traccia del tempo trascorso di gioco in secondi, mentre il flag `running` viene utilizzato per controllare l'esecuzione del thread del timer.

Quando viene creata una nuova partita, `secondsElapsed` viene inizializzato a 0 e il thread del timer viene avviato. Al momento del salvataggio, si aggiornerà la variabile `timeElapsed` nella `GameDescription` della partita, in modo da poter riprendere il timer al momento del caricamento.

Il metodo `run` viene eseguito all'avvio del thread e continua ad eseguire un loop finché il gioco è in esecuzione e il thread non è interrotto. Ogni secondo, il tempo di gioco viene aggiornato e, se trascorrono 300 secondi (5 minuti), viene visualizzato un messaggio di avviso.

I metodi `stopTimer`, `resumeTimer`, `resetTimer` e `setTime` permettono rispettivamente di fermare temporaneamente il thread, riprenderlo, azzerare il timer e impostare il tempo di gioco a un valore specifico, nel caso dovessero essere necessarie queste funzionalità.

### Programmazione in rete

### Framework Swing

### Espressioni Lambda


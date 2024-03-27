# Caso di studio - Metodi Avanzati di Programmazione 22-23
***NURJA Luan Abdurahman, REGGIO Francesco Maria, SANTOVITO Leonardo***

## Introduzione
Il caso di studio consiste nella realizzazione di una avventura testuale nel linguaggio Java, con l'obiettivo di applicare i concetti di programmazione orientata agli oggetti e di utilizzare gli argomenti spiegati durante il corso di Metodi Avanzati di Programmazione.

Il programma risultante è un gioco testuale, con interazione da linea di comando, in cui il giocatore impersona un personaggio che si trova in un mondo virtuale e deve interagire con esso per risolvere enigmi e proseguire nella storia.

## Indice

1. [Descrizione generale del caso di studio](#descrizione-generale-del-caso-di-studio)
    - [Introduzione al gioco Starship Exodus](#introduzione-al-gioco-starship-exodus)
    - [Struttura del progetto](#struttura-del-progetto)
    - [Funzionalità del gioco](#funzionalità-del-gioco)
2. [Applicazione degli argomenti del corso nel progetto](#applicazione-degli-argomenti-del-corso-nel-progetto)
    - [Files](#files)
    - [Java Database Connectivity](#java-database-connectivity)
    - [Programmazione concorrente](#programmazione-concorrente)
    - [Programmazione in rete](#programmazione-in-rete)
    - [Framework Swing](#framework-swing)
    - [Espressioni Lambda](#espressioni-lambda)
3. [Diagramma delle classi](#diagramma-delle-classi)
4. [Specifica algebrica](#specifica-algebrica)

## Descrizione generale del caso di studio

### Introduzione al gioco Starship Exodus

*Terra, anno 2030, la crisi energetica è la priorità di tutti i governi mondiali. Le discordie tra i paesi produttori di petrolio e le guerre rendono la situazione sempre più fuori controllo. L'Unione Europea si sta mobilitando per cercare una soluzione a questa crisi, allocando ingenti risorse nella ricerca e sviluppo di nuove fonti di energia rinnovabili. 
L'Agenzia Spaziale Europea (ESA) ricerca nello spazio nuove forme di energia e ha scoperto un pianeta potenzialmente abitabile, denominato 'Eden', situato ad una distanza incredibilmente vicina alla Terra. Viene organizzata una spedizione di due astronauti per esplorare il pianeta e cercare di capire se è possibile trasferirvi una cerchia di eletti.
Dieci anni dopo, lo shuttle è quasi giunto a destinazione...*

In **Starship Exodus**, il giocatore dovrà impersonare uno dei due astronauti della spedizione spaziale in arrivo verso 'Eden'. Sul pianeta, però, non ci metterà mai piede. Il giocatore si ritroverà improvvisamente in una astronave aliena, e dovrà utilizzare tutti i mezzi a propria disposizione per sopravvivere, con la speranza di tornare a casa.

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
- **Mappa**: il giocatore può visualizzare la mappa dell'astronave aliena con il comando `mappa`, ammesso che abbia la mappa nell'inventario.
- **Tempo**: il giocatore può tener traccia del tempo di gioco usando il comando `tempo`.
- **Aiuto**: con il comando `aiuto`, si può visualizzare una schermata con informazioni utili al giocatore.
- **Uscita**: il giocatore può uscire dal gioco in qualsiasi momento con `esci`, e gli verrà chiesto se vuole salvare o meno la partita corrente.
- **Salvataggio**: il giocatore può salvare la partita in qualsiasi momento e riprenderla successivamente con il comando `salva`.
- **Caricamento**: dal menu iniziale il giocatore può caricare una partita salvata in precedenza e riprenderla da dove l'aveva interrotta.
- **Vittoria**: il giocatore vince il gioco se trova il modo di fuggire dall'astronave aliena.

## Applicazione degli argomenti del corso nel progetto

Nel caso di studio 'Starship Exodus' sono stati impiegati i concetti di programmazione orientata agli oggetti e le conoscenze acquisite durante il corso di Metodi Avanzati di Programmazione.
Di seguito sono riportati gli argomenti trattati durante il corso e il relativo utilizzo nel progetto.

### Files

All'interno del caso di studio vengono utilizzati diversi tipi di file per memorizzare in modo permanente una serie di informazioni necessarie al funzionamento del gioco, in modo tale da separarle dall'implementazione del codice e consentire una facile modifica e personalizzazione del gioco.

#### File Testuali Semplici

Nel progetto sono presenti diversi file testuali semplici, utilizzati principalmente per memorizzare dialoghi, descrizioni e altri testi di supporto al gioco.

I file testuali che si trovano nella directory `resources/dialogs/` hanno una estensione `.txt` e contengono i testi utilizzati durante l'esecuzione del gioco. Questi file vengono caricati dai metodi `printFromFile` e `printFromFilePlaceholder` che prendono in input il path del file e stampano sul terminale i testi salvati.
Il secondo metodo prende in input anche una stringa che verrà sostituita all'eventuale `{placeholder}` presente nel testo. 
Questi file in generale vengono utilizzati per caricare l'introduzione del gioco, il testo della fine del gioco, il testo da visualizzare con il comando `aiuto` e per fornire descrizioni di risposte a determinate azioni compiute dal giocatore. Un esempio di utilizzo di questi file si trova all'interno della classe `CommandsExecution`, dove vengono caricati i dialoghi relativi alle risposte del gioco all'uso di oggetti.

La directory `resources/files/` contiene anche un file `stopwords`, ovvero elenchi di parole da ignorare durante l'analisi del testo.
Le stopwords vengono caricate dal file all'interno della classe `Engine` quando viene inizializzata dal costruttore. Le stopwords vengono utilizzate nell'analisi dei comandi immessi dal giocatore per eliminare parole non rilevanti alla classe `Parser` che si occupa di interpretare i comandi.

#### File Strutturati

Oltre ai file testuali semplici, il progetto fa ampio uso di file strutturati di tipo JSON, situati nella directory `resources/files/`, per memorizzare informazioni più complesse, come la configurazione delle stanze, con i relativi oggetti, e i campi dei comandi del gioco.

Il file `rooms.json` contiene la definizione delle stanze presenti nell'astronave aliena del gioco, insieme alle loro caratteristiche e agli oggetti al loro interno.
Le stanze sono rappresentate come oggetti JSON, ciascuno con attributi quali `id`, `name`, `description`,`look` (testo che viene visualizzato quando il giocatore invoca il comando `osserva` mentre è nella stanza relativa), `intro` (testo che viene visualizzato solo la prima volta che l'utente entra in una stanza, ai fini della storia), i collegamenti alle stanze adiacenti identificate tramite il rispettivo `id`, variabili che vengono impiegate quando una stanza è inaccessibile, e gli oggetti all'interno della stanza.

Gli oggetti, pertanto, sebbene siano delle entità separate come definiti dalla classe `AdvObject` nel package `org.example.type`, vengono memorizzati all'interno delle stanze, in modo da semplificare la gestione degli oggetti e delle stanze all'interno del gioco. Degli oggetti vengono salvati `id`, `name`, `description`, la lista degli `alias`, e variabili booleane come `openable`, `pickupble`, `open`.
La gestione degli oggetti containers è stata implementata in modo tale che ogni oggetto ha una lista `objectsList` che contiene gli oggetti al suo interno, un attributo `open` che indica se l'oggetto è aperto e quindi gli oggetti al suo interno sono raccoglibili dal giocatore.
Sono presenti anche le variabili `containerId`, che definisce l'`id` dell'oggetto contenitore, e la variabile boolena `container` settata a `true` se l'oggetto è un contenitore.

Analogamente al file delle stanze, il file `commands.json` contiene la definizione dei comandi disponibili nel gioco, insieme alle relative azioni associate. Ogni comando è rappresentato come un oggetto JSON con attributi come `type` che indica il tipo di comando salvato nella classe enumerativa `CommandType` nel package `org.example.type`, `name`, e la lista degli `alias` utilizzati dal parser.

L'inizializzazione del gioco è attuata dal metodo `init` all'interno della classe `StarshipExodus` che implementa la classe astratta `GameDescription`.
In `init` le informazioni vengono caricate dai rispettivi file JSON mediante il metodo `loadObjectsFromFile` nella classe `Utils` e caricate in memoria nelle rispettive liste di stanze e comandi della `GameDescription`. 
Tutto ciò ha permesso una facile modifica e personalizzazione delle stanze e dei comandi senza dover modificare direttamente il codice sorgente.

I file JSON sono stati gestiti mediante la libreria `Gson`, inclusa nel file `pom.xml` come dipendenza, che permette di convertire oggetti Java in formato JSON e viceversa.

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


![dbSchema.jpg](resources%2Fimages%2FdbSchema.jpg)

*Figura 3. Schema della tabella games nel database.*

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

Nel contesto della programmazione in rete, sono state impiegate le API con architettura REST per ottenere informazioni meteorologiche in tempo reale. Per fare ciò, è stata implementata la classe `WeatherApi`, all'interno del package `org/example/api/`, che si occupa di effettuare richieste HTTP all'API di OpenWeatherMap per ottenere i dati meteorologici di una determinata città.

La classe `WeatherApi` contiene un metodo `getWeatherData` che accetta il nome di una città come parametro e restituisce informazioni sulle condizioni meteorologiche attuali di quella città.
Si utilizza la libreria `Gson`, includendo la relativa dipendenza nel file `pom.xml`, per il parsing dei dati JSON ricevuti dall'API, con la quale vengono estratte le informazioni necessarie quali la descrizione del meteo, la temperatura, l'umidità e le coordinate geografiche della città.

La classe `WeatherApi` viene utilizzata all'interno del metodo `useItem` della classe `CommandsExecution`, durante l'esecuzione del gioco.
In particolare, quando il giocatore utilizza l'oggetto "trasmettitore di messaggi intergalattico", crea un tunnel quantico con un posto specifico della Terra, verso il quale vuole inviare un messaggio di richiesta di aiuto, da trasmettere come onda elettromagnetica.
Il dispositivo risponderà con informazioni relative al meteo del posto desiderato, oltre che ad altre informazioni come "Pericolosità dei locali" o "Adattabilità alla nostra forma di vita".

Per fare ciò, viene istanziato un oggetto `WeatherApi` e invocato il metodo `getWeatherData`, passando come parametro la città scelta dal giocatore. Una volta ottenute le informazioni, vengono visualizzate sullo schermo insieme al messaggio inviato, caricato dal file testuale `use_object_8.txt`, salvato in `resources/dialogs/`.

### Framework Swing

Nel progetto, sono state impiegate le Swing Java per creare interfacce grafiche utente (GUI) interattive. Le Swing sono una libreria di classi e componenti grafici forniti da Java che permettono di sviluppare GUI, rendendo l'esperienza utente più intuitiva e coinvolgente. Queste classi sono contenute nel package `javax.swing`.

Un componente fondamentale delle Swing è JPanel, che agisce come un contenitore leggero per organizzare altri componenti Swing. Nel progetto, è stata estesa la classe JPanel per creare sia il pannello di gioco `AlienBossGame` che il tastierino numerico `NumericKeypadUnlocker`. Questi pannelli forniscono l'ambiente visivo per l'interazione dell'utente con il gioco e per l'inserimento di una combinazione numerica.

È stata utilizzata ampiamente la classe JButton per creare pulsanti cliccabili all'interno della GUI. Questi pulsanti, presenti sia nel tastierino numerico che nel pannello di gioco, permettono agli utenti di eseguire azioni come inserire numeri, cancellare l'input o confermare una combinazione.

Per l'inserimento dell'input da parte dell'utente, è stato impiegato il componente JTextField, che offre una semplice interfaccia per l'inserimento di testo singolo. Nel nostro caso, il JTextField è stato utilizzato nel tastierino numerico per visualizzare le cifre inserite dall'utente.

È stata anche sfruttata la classe JDialog per creare finestre di dialogo modali (mentre il dialog è aperto non si può interagire con altro) o non modali, utilizzate per mostrare la mappa del gioco, l'oggetto `note`, il testo di aiuto (al quale si può accedere sia dal menu iniziale che durante il gioco con il comando `aiuto`) e per creare una finestra modale per il combattimento con il boss alieno.
Queste finestre vengono utilizzate, quindi, per fornire informazioni aggiuntive all'utente o per creare delle sfide per proseguire nel gioco.

![Notes.png](resources%2Fimages%2FNotes.png)

*Figura 4. Utilizzo di JDialog per visualizzare l'oggetto "Note" durante il gioco.*

Per gestire gli eventi generati dall'interazione dell'utente con i componenti Swing, sono state implementate le interfacce `MouseListener` e `ActionListener`. Queste interfacce permettono di definire comportamenti specifici da eseguire quando un utente interagisce con pulsanti, aree di disegno e altri componenti GUI.

Infine, è stato personalizzato l'aspetto della GUI sovrascrivendo il metodo `paintComponent`, che ha permesso di disegnare grafica personalizzata nei pannelli di gioco. Questo ci ha fornito il controllo completo sull'aspetto visivo del gioco, permettendoci di rendere l'esperienza utente più accattivante e coinvolgente.

L'utilizzo delle Swing nel progetto ha consentito di creare diverse interfacce grafiche per migliorare l'esperienza di gioco rispetto alla semplice interazione da linea di comando, per determinate azioni.

#### Menu iniziale

La classe `MenuSwing` fornisce un'interfaccia utente per avviare e gestire il gioco attraverso pulsanti grafici. Pertanto, gestisce il frame principale visualizzato all'avvio. Essenzialmente, crea una finestra di dimensioni fisse (400x500 pixel) che ospita i pulsanti principali del gioco e fornisce un'interfaccia utente per gestire diverse azioni.

Per prima cosa, all'interno del metodo `startMenu`, viene istanziato un oggetto JFrame con il titolo "Starship Exodus", dimensioni fisse e posizionamento al centro dello schermo. Questo frame rappresenta il contenitore principale della nostra interfaccia.

Successivamente, viene creato un pannello personalizzato chiamato `Background`, che funge da contenitore per il layout grafico.
All'interno di questo pannello vengono aggiunti anche altri componenti grafici, come il logo del gioco.
Questo pannello è fondamentale perché permette di inserire uno sfondo personalizzato, e viene utilizzato sia per il menu iniziale, che per altri pannelli grafici del gioco, come visualizzare gli oggetti `mappa` e `note`, e il comando `help`.

I pulsanti vengono creati come istanze di JButton e configurati con testo, icone e azioni associate. I pulsanti vengono posizionati manualmente all'interno del pannello grafico e vengono aggiunti al pannello stesso.
Sono presenti i seguenti quattro pulsanti.

* *Nuova Partita:* Avvia una nuova partita quando premuto.

* *Carica Partita:* Carica una partita salvata nel database.

* *Help:* Mostra i comandi del gioco quando premuto.

* *Esci:* Chiude il gioco quando premuto.

Infine, vengono gestiti gli eventi associati ai pulsanti tramite ActionListener. Quando un pulsante viene premuto, viene eseguita un'azione specifica, come avviare una nuova partita o chiudere il gioco.

#### Alien Boss Game

La classe `AlienBossGame` è una sottoclasse di JPanel che rappresenta il pannello di gioco per un semplice gioco in cui il giocatore deve colpire il capo dell'astronave aliena, nella stanza "Ponte di comando".
Questo pannello fornisce un'interfaccia grafica per il gioco, disegnando il boss, la salute e il punteggio, e gestendo i click del mouse per colpire il boss.

Il pannello di gioco contiene vari attributi per gestire lo stato del gioco, come la salute del boss, il punteggio del giocatore e le coordinate del boss.
Inoltre, vengono utilizzate immagini per disegnare lo sfondo, il boss e lo sfondo di vittoria.
In particolare, troviamo le seguenti variabili di stato.

- `bossHealth`: Rappresenta la salute del boss alieno.
- `score`: Punteggio del giocatore.
- `bossHit`: Indica se il boss è stato colpito nell'ultimo click.
- `bossX`, `bossY`: Coordinate x e y del boss alieno.
- `killed`: Indica se il boss è stato sconfitto.
- `gameWon`: Indica se il gioco è stato vinto.

Le immagini impiegate nel gioco sono caricate all'avvio del pannello di gioco e vengono disegnate all'interno del metodo `paintComponent`.
In particolare, vengono utilizzate le seguenti immagini.

- `background`: Immagine dello sfondo del gioco.
- `winBackground`: Immagine dello sfondo della schermata di vittoria.
- `bossImage`: Immagine del boss alieno.

Il costruttore `AlienBossGame` imposta le dimensioni del pannello di gioco,
carica le immagini dello sfondo, del boss e dello sfondo di vittoria,
avvia un timer per cambiare l'area del boss ogni secondo e uno per far avanzare il gioco.
Aggiunge un MouseListener per rilevare i click del mouse e controlla la sconfitta del boss.

I metodi hanno due livelli di protezione, privati e pubblici.
I metodi privati sono utilizzati per disegnare il gioco, controllare la fine del gioco e disegnare lo sfondo in base allo stato del gioco, e sono i seguenti.
- `paintComponent`: Disegna il gioco sul pannello.
- `drawGame`: Disegna il boss, la salute e il punteggio.
- `endGame`: Mostra un messaggio di vittoria e termina il gioco.
- `drawBackground`: Disegna lo sfondo in base allo stato del gioco.

I metodi pubblici sono utilizzati per accedere e modificare lo stato di `gameWon` e sono `isGameWon`, che restituisce il valore booleano, e `setGameWon`, che viene usato per impostarlo.

Il pannello di gioco disegna il background, il boss, la salute e il punteggio, il timer cambia l'area del boss ogni secondo, rendendolo "scorrevole", il mouse listener rileva i click del mouse e controlla se il boss viene colpito, il timer del gioco controlla se il boss è stato sconfitto e se il boss viene sconfitto, viene mostrato un messaggio di vittoria e il gioco termina.

La classe `AlienBossGame` fornisce, pertanto, un'interfaccia grafica per un semplice gioco in cui il giocatore deve colpire il boss alieno finché la sua salute non raggiunge lo zero. Una volta sconfitto il boss, il giocatore vince il mini gioco e potrà raccogliere la chiave necessaria a proseguire l'avventura.

#### Numeric Keypad Unlocker

La classe `NumericKeypadUnlocker` rappresenta un pannello per un tastierino numerico virtuale utilizzato per sbloccare una combinazione di cifre.
Viene visualizzato quando il giocatore esegue `usa visore` nella stanza iniziale "Laboratorio". Segue una descrizione dei principali componenti e funzionalità della classe.

Nel costruttore, vengono inizializzati e configurati i componenti del pannello, tra cui il campo di testo per l'input e i pulsanti numerici del tastierino.

Il campo `JTextField` nominato `inputField` rappresenta il campo di testo in cui vengono visualizzate le cifre inserite dall'utente.

Il tastierino numerico è costituito da pulsanti numerici da 0 a 9, un pulsante "Cancella" e un pulsante "OK". Ogni pulsante numerico viene configurato con un listener per gestire l'aggiunta della cifra corrispondente all'input e la riproduzione di un suono di clic.
Il pulsante "Cancella" elimina l'ultima cifra inserita dall'utente, mentre il pulsante "OK" controlla se sono state inserite esattamente tre cifre e visualizza un messaggio di conferma o errore di conseguenza.

Per quanto riguarda lo sblocco della combinazione, la soluzione corretta per sbloccare il tastierino è fissata a "531".
Quando l'utente inserisce tre cifre, il programma controlla se la sequenza corrisponde alla combinazione di sblocco.
Se la combinazione è corretta, viene visualizzato un messaggio di sblocco e il pannello viene chiuso.
Invece, se la combinazione è errata, viene visualizzato un messaggio di errore e l'input viene resettato.

Quando l'utente preme un pulsante numerico, viene riprodotto un suono di clic per fornire un feedback all'utente. 
Il campo statico `padUnlocked` tiene traccia dello stato di sblocco del tastierino numerico. 

La classe fornisce l'interfaccia utente interattiva per l'inserimento della combinazione che permette di sbloccare la porta che permette di accedere alla stanza "Sala delle Armi" dal "Laboratorio", e di conseguenza continuare il gioco.

Entrambe le classi `AlienBossGame` e `NumericKeypadUnlocker` vengono instanziate all'interno di un JDialog nella classe `CommandExecution` rispettivamente in corrispondenza dei comandi attacca e usa visore.


### Espressioni Lambda
Le espressioni lambda sono state usate in più parti del progetto per instanziare oggetti di interfacce funzionali, come `Runnable` e `ActionListener`, in modo conciso e leggibile, senza la necessità di creare classi anonime. Ecco le parti del progetto in cui sono state utilizzate:
- **NumericKeypadUnlocker**: qui le espressioni lambda sono state usate per gestire gli eventi dei pulsanti del tastierino numerico e per gestire l'evento di chiusura del pannello

![lambdaKeypad3.png](resources%2Fimages%2FlambdaKeypad3.png)

![lambdaKeypad2.png](resources%2Fimages%2FlambdaKeypad2.png)

![lambdaKeypad1.png](resources%2Fimages%2FlambdaKeypad1.png)

- **Parser**: qui le espressioni lambda sono state usate per filtrare gli indici nella lista `item` in base alle condizioni specificate. 

![lambdaParser.png](resources%2Fimages/lambdaParser.png)

- **AlienBossGame**: qui le espressioni lambda sono state usate per gestire gli eventi del mouse per colpire il boss alieno e per gestire il timer del gioco.

![lambdaBoss.png](resources%2Fimages/lambdaBoss.png)

![lambdaBoss2.png](resources%2Fimages/lambdaBoss2.png)

- **MenuSwing**: qui le espressioni lambda sono state usate per gestire gli eventi dei pulsanti del menu iniziale.

![lambdaMenu.png](resources%2Fimages/lambdaMenu.png)



## Diagramma delle classi
**Diagramma della classe `Engine`**

![Engine_structure.svg](resources%2Fdiagrams%2FEngine_structure.svg)

**Diagramma della classe `StarshipExodus`**

![StarshipExodus_structure.svg](resources%2Fdiagrams%2FStarshipExodus_structure.svg)

**Diagramma della classe `GameTimer`**

![GameTimer_structure.svg](resources%2Fdiagrams%2FGameTimer_structure.svg)


## Specifica algebrica
package org.example;

/* Classe che gestisce il thread della partita. Si occupa di gestire la variabile del tempo di gioco. */
public class GameTimer extends Thread{
    private volatile int secondsElapsed;
    private boolean running;

    public GameTimer(int secondsElapsed){
        this.secondsElapsed = secondsElapsed;
        this.running = true;
    }

    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                /* Aggiorna il tempo di gioco ogni secondo. */
                Thread.sleep(1000);
                secondsElapsed++;
                if ((secondsElapsed) % 300 == 0) {
                    System.out.println("Hai già giocato per " + (secondsElapsed)/60 + " minuti! Ti vuoi muovere a finire il gioco?");
                    System.out.println();
                }
            } catch (InterruptedException ex) {
                //System.out.println("DEBUG = Timer thread interrotto");

                /* Preserva l'interruzione del thread. */
                Thread.currentThread().interrupt();
            }
        }
    }

    /* Restituisce il tempo di gioco trascorso in secondi. */
    public int getSecondsElapsed(){
        return secondsElapsed;
    }

    /* Ferma temporaneamente il thread del timer. */
    public void stopTimer(){
        running = false;
    }

    /* Riprende il thread del timer. */
    public void resumeTimer(){
        running = true;
    }

    /* Resetta il timer a 0. */
    public void resetTimer(){
        secondsElapsed = 0;
    }

    /* Imposta il timer a un tempo in input misurato in secondi. */
    public void setTime(int secondsElapsed){
        this.secondsElapsed = secondsElapsed;
    }
}

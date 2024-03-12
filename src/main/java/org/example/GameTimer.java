package org.example;

/* Classe che gestisce il thread della partita. Si occupa di gestire la variabile del tempo di gioco. */
public class GameTimer extends Thread{
    private volatile int secondsElapsed;
    private boolean running;

    public GameTimer(int secondsElapsed){
        this.secondsElapsed = secondsElapsed;
        this.running = true;
    }

    public void run(){
        while(running){
            try {
                /* Aggiorna il tempo di gioco ogni secondo. */
                Thread.sleep(1000);
                secondsElapsed++;
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }

    public int getSecondsElapsed(){
        return secondsElapsed;
    }

    public void stopTimer(){
        running = false;
    }

    public void resumeTimer(){
        running = true;
    }

    public void resetTimer(){
        secondsElapsed = 0;
    }

    public void setTime(int secondsElapsed){
        this.secondsElapsed = secondsElapsed;
    }
}

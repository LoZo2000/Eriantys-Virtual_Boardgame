package it.polimi.ingsw.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An object that implements the Callable interface: its purpose is reading the System.in buffer and returning a String
 */
public class ReadInputTask implements Callable<String> {

    private final AtomicBoolean finished;

    /**
     * Constructor of the class.
     * It receives the object that has the flag that tells if the game is finished or not.
     * It needs an object because this status can change while it's reading the console.
     * @param finished It's the flag that tells if the game is finished.
     */
    public ReadInputTask(AtomicBoolean finished) {
        this.finished = finished;
    }

    /**
     * Method called when the object is submitted to an Executor.
     * Using a BufferedReader this method continuously checks, thanks to the ready method of BufferedReader, if there's a carriage return
     * the call method will return the String in buffer. Otherwise, the thread will wait 50 milliseconds and retry.
     * This method will also stop if the finished flag is true, meaning that the game has already ended.
     * @return A String in buffer, if there is one. If finished is true this will return null.
     * @throws IOException if the stream is closed while trying to read.
     */
    public String call() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        //System.out.println("ConsoleInputReadTask run() called.");
        String input;
        do {
            try {
                // Wait until the buffer is ready to complete a readLine without blocking, so when there is a '\n' in System.in stream
                while (!br.ready() && !finished.get()) {
                    Thread.sleep(50);
                }
                if(!finished.get())
                    input = br.readLine();
                else
                    input = null;
            } catch (InterruptedException e) {
                return null;
            }
        } while (!finished.get() && "".equals(input));
        return input;
    }
}
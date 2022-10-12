package ru.rsreu.labs.additional_tasks;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class FirstAdditionalTaskRunner {

    private final Collection<Thread> threads = new ArrayList<>();
    private int count = 0;

    public static void main(String[] args) throws InterruptedException {
        new FirstAdditionalTaskRunner().start(args);
    }

    public void start(String[] filenames) throws InterruptedException {
        for (int i = 0; i < filenames.length; i++) {
            String filename = filenames[i];
            int finalI = i;
            Thread thread = new Thread(() -> {
                try {
                    readFile(filename);
                } catch (InterruptedException e) {
                    System.out.printf("interrupted %d thread\n", finalI);
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.printf("Count: %d\n", count);
    }

    private void readFile(String filename) throws InterruptedException {
        try {
            FileReader fileReader = new FileReader(filename);
            int symbolCode = fileReader.read();
            while (symbolCode != -1) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                if (symbolCode == (int) 'a') {
                    incrementCount();
                }
                symbolCode = fileReader.read();
            }
            fileReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void incrementCount() {
        if (count >= 10) return;
        count++;
        if (count >= 10) {
            System.out.println("Found 10 symbols");
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }
    }
}
package ru.rsreu.labs.additional_tasks;

import java.io.FileReader;
import java.io.IOException;

public class SecondAdditionalTaskRunner {
    private final Object lock = new Object();
    private int lastThread;

    public static void main(String[] args) {
        new SecondAdditionalTaskRunner().start(args);
    }

    public void setLastThread(int lastThread) {
        synchronized (lock) {
            this.lastThread = lastThread;
            lock.notify();
        }
    }

    private void start(String[] filenames) {
        for (int i = 0; i < filenames.length; i++) {
            String filename = filenames[i];
            int threadNumber = i + 1;

            new Thread(() -> readFile(filename, threadNumber)).start();
        }

        Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    while (true) {
                        lock.wait();
                        System.out.print(this.lastThread + " ");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void readFile(String filename, int threadNumber) {
        try {
            FileReader fileReader = new FileReader(filename);
            int symbolCode = fileReader.read();
            while (symbolCode != -1) {
                if ((char) symbolCode == 'a') {
                    setLastThread(threadNumber);
                }
                symbolCode = fileReader.read();
            }
            fileReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
package ru.rsreu.labs.additional_tasks;

import java.io.FileReader;
import java.io.IOException;

public class SecondAdditionalTaskRunner {
    private int lastThread;

    public static void main(String[] args) {
        new SecondAdditionalTaskRunner().start(args);
    }

    private void start(String[] filenames) {
        for (int i = 0; i < filenames.length; i++) {
            String filename = filenames[i];
            int threadNumber = i + 1;

            new Thread(() -> readFile(filename, threadNumber)).start();
        }

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.print(lastThread + " ");
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
                Thread.sleep(1000);
                if ((char) symbolCode == 'a') {
                    lastThread = threadNumber;
                }
                symbolCode = fileReader.read();
            }
            fileReader.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
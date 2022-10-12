package ru.rsreu.labs.additional_tasks;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ThirdAdditionalTaskRunner {
    private final Collection<Thread> threads = new ArrayList<>();
    private volatile Collection<Integer> positions;

    public static void main(String[] args) throws InterruptedException {
        new ThirdAdditionalTaskRunner().start(args);
    }

    private Collection<Integer> getPositions() {
        if (positions == null) {
            synchronized (this) {
                if (positions == null) {
                    positions = new ArrayList<>();
                }
            }
        }
        return positions;
    }

    private void start(String[] filenames) throws InterruptedException {
        for (String filename : filenames) {
            Thread thread = new Thread(() -> readFile(filename));
            threads.add(thread);
            thread.start();
        }
        for(Thread thread: threads){
            thread.join();
        }
        System.out.println(positions);
    }

    private void readFile(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            int i = 0;
            int symbolCode = fileReader.read();
            while (symbolCode != -1) {
                if (symbolCode == (int) ' ') {
                    addPosition(i);
                }
                symbolCode = fileReader.read();
                i++;
            }
            fileReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void addPosition(int position) {
        getPositions().add(position);
    }
}
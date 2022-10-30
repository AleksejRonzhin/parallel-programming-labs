package ru.rsreu.labs.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountDownLatchTest {

    private static <T> Collection<T> mergeCollections(Collection<T> firstPart, Collection<T> secondPart) {
        return Stream.concat(firstPart.stream(), secondPart.stream())
                .collect(Collectors.toList());
    }

    @Test
    public void threadsIsTerminatedTest() throws InterruptedException {
        int threadCount = 50;
        CountDownLatch latch = new CountDownLatch(threadCount);
        Collection<Thread> threads = createCountDownLatchThreads(threadCount, latch);
        startThreads(threads);
        Thread.sleep(1000);
        Assertions.assertTrue(allThreadsIsState(threads, Thread.State.TERMINATED));
    }

    private Collection<Thread> createCountDownLatchThreads(int threadCount, CountDownLatch latch) {
        Collection<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            threads.add(new Thread(() -> countDownLatchTarget(latch)));
        }
        return threads;
    }

    private void countDownLatchTarget(CountDownLatch latch) {
        latch.countDown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void threadsIsWaitingTest() throws InterruptedException {
        int threadCount = 50;
        CountDownLatch latch = new CountDownLatch(threadCount + 1);
        Collection<Thread> threads = createCountDownLatchThreads(threadCount, latch);
        startThreads(threads);
        Thread.sleep(1000);
        Assertions.assertTrue(allThreadsIsState(threads, Thread.State.WAITING));
    }

    @Test
    public void threadsTerminateAtSameTime() throws InterruptedException, ExecutionException {
        int taskCount = 20;
        CountDownLatch latch = new CountDownLatch(taskCount);
        List<Callable<Long>> tasks = createGetFinishedTimeDownLatchTasks(taskCount, latch);
        ExecutorService service = Executors.newFixedThreadPool(taskCount);
        Collection<Future<Long>> futuresFirstPart = startTasks(tasks.subList(0, taskCount / 2), service);

        Thread.sleep(3000);

        Collection<Future<Long>> futuresSecondPart = startTasks(tasks.subList(taskCount / 2, taskCount), service);
        Collection<Future<Long>> futures = mergeCollections(futuresFirstPart, futuresSecondPart);
        Collection<Long> finishedTimes = getFinishedTimes(futures);
        long eps = 10;
        Assertions.assertTrue(allTimesIsSame(finishedTimes, eps));
    }

    private boolean allTimesIsSame(Collection<Long> finishedTimes, long eps) {
        LongSummaryStatistics statistics = finishedTimes.stream().mapToLong(Long::longValue).summaryStatistics();
        long min = statistics.getMin();
        long max = statistics.getMax();
        return max - min < eps;
    }

    private Collection<Long> getFinishedTimes(Collection<Future<Long>> futures) throws ExecutionException, InterruptedException {
        Collection<Long> times = new ArrayList<>();
        for (Future<Long> future : futures) {
            times.add(future.get());
        }
        return times;
    }

    private List<Future<Long>> startTasks(List<Callable<Long>> tasks, ExecutorService service) {
        List<Future<Long>> futures = new ArrayList<>();
        tasks.forEach(task -> futures.add(service.submit(task)));
        return futures;
    }

    private List<Callable<Long>> createGetFinishedTimeDownLatchTasks(int taskCount, CountDownLatch latch) {
        List<Callable<Long>> tasks = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            tasks.add(getFinishedTimeCountDownLatchTarget(latch));
        }
        return tasks;
    }


    private Callable<Long> getFinishedTimeCountDownLatchTarget(CountDownLatch latch) {
        return () -> {
            countDownLatchTarget(latch);
            return System.currentTimeMillis();
        };
    }

    private void startThreads(Collection<Thread> threads) {
        threads.forEach(Thread::start);
    }

    private boolean allThreadsIsState(Collection<Thread> threads, Thread.State state) {
        for (Thread thread : threads) {
            if (thread.getState() != state) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void decrementCountTest() {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        countDownLatch.countDown();
        Assertions.assertEquals(4, countDownLatch.getCount());
    }

    @Test
    public void decrementCountToMuchTest() {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        for (int i = 0; i < 10; i++) {
            countDownLatch.countDown();
        }
        Assertions.assertEquals(0, countDownLatch.getCount());
    }

    @Test
    public void awaitTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
        Thread.sleep(1000);

        Assertions.assertEquals(Thread.State.WAITING, thread.getState());
    }

    @Test
    public void awaitWithTimeoutTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                countDownLatch.await(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
        Thread.sleep(1000);

        Assertions.assertEquals(Thread.State.TERMINATED, thread.getState());
    }

    @Test
    public void interruptAwaitingTest() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(() -> Assertions.assertThrowsExactly(InterruptedException.class, countDownLatch::await));
        thread.start();
        thread.interrupt();
    }
}

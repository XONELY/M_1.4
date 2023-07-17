package org.example;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Foo foo = new Foo();
        System.out.println(Runtime.getRuntime().availableProcessors());
       /* ExecutorService es = Executors.newFixedThreadPool(3);
        es.execute(() -> foo.first(new Thread()));
        es.execute(() -> foo.third(new Thread()));
        es.execute(() -> foo.second(new Thread()));*/

        CompletableFuture.runAsync(() -> foo.first(new Thread()));
        CompletableFuture.runAsync(() -> foo.third(new Thread()));
        CompletableFuture.runAsync(() -> foo.second(new Thread()));

    }
}

class Foo {
    private Semaphore semaphoreFirst = new Semaphore(0);
    private Semaphore semaphoreSecond = new Semaphore(0);

    public void first(Runnable r) {
        System.out.print("first");
        semaphoreFirst.release();
    }

    public void second(Runnable r) {
        try {
            semaphoreFirst.acquire();
            System.out.print("second");
            semaphoreSecond.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void third(Runnable r) {
        try {
            semaphoreSecond.acquire();
            System.out.print("third");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
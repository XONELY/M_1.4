package org.example;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Foo foo = new Foo();
        ExecutorService es = Executors.newFixedThreadPool(3);
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                foo.first(this);
            }
        };
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                foo.second(this);
            }
        };
        Runnable r3 = new Runnable() {
            @Override
            public void run() {
                foo.third(this);
            }
        };

        es.execute(r2);
        es.execute(r3);
        es.execute(r1);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("second");
        semaphoreSecond.release();
    }
    public void third(Runnable r) {
        try {
            semaphoreSecond.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("third");
    }
}
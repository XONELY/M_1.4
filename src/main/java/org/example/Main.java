package org.example;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Foo foo = new Foo();
        ExecutorService es = Executors.newFixedThreadPool(3);
        //region Note

        // Runnable r1 = new Runnable() {
        //   @Override
        //    public void run() {
        //        foo.first(this);
        //    }
        // };
        // es.submit(r1);
        // Если нужно было сохранить параметры методов Foo принимающие (Runnable r) и потом через wait/notify их синхронизировать

        //endregion
        es.execute(() -> {
            foo.first();
        });
        es.execute(() -> {
            try {
                foo.third();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        es.execute(() -> {
            try {
                foo.second();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

class Foo {
    private Semaphore semaphoreFirst = new Semaphore(0);
    private Semaphore semaphoreSecond = new Semaphore(0);

    public void first() {
        System.out.print("first");
        semaphoreFirst.release();
    }

    public void second() throws InterruptedException {
        semaphoreFirst.acquire();
        System.out.print("second");
        semaphoreSecond.release();
    }

    public void third() throws InterruptedException {
        semaphoreSecond.acquire();
        System.out.print("third");
    }
}
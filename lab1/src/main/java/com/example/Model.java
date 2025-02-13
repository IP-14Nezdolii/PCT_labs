package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.elems.Ball;
import com.example.elems.Pocket;

public class Model {
    private final ArrayList<Ball> balls = new ArrayList<>();
    private Pocket pocket = null;

    public void add(Ball ball, long sleepTime) {
        balls.add(ball);

        Thread thread = new Thread(runBall(ball));
        thread.setPriority(ball.getPriority());
        thread.start();

        System.out.println("Thread name = " + thread.getName());
    }

    public void add(Ball ball) {
        add(ball, 5);
    }

    public void add(Pocket pocket) {
        if (this.pocket == null) 
            this.pocket = pocket;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public Pocket getPocket() {
        return this.pocket;
    }

    public void clearBalls() {
        for (Ball ball : balls) ball.setFallen(true);
        balls.clear();
    }

    public void clearPocket() {
        pocket = null;
    }

    Runnable runBall(Ball ball) {
        return () -> {
            try {
                while (ball.isFallen() == false) {   

                    if (isBallInPocket(ball))
                        ball.setFallen(true);

                    ball.move();

                    System.out.println("Thread name = " + Thread.currentThread().getName());
                    Thread.sleep(4);
                }
            } catch (InterruptedException ex) {

            }
        };
    }

    boolean isBallInPocket(Ball ball) {
        if (ball.isFallen()) 
            return true;

        if (pocket == null) 
            return false;

        if (pocket.contains(ball))
            return true;

        return false;
    }

    public void doTask4(Ball ball1, Ball ball2) {
       Thread thread = new Thread(()->{
            balls.add(ball1);
            balls.add(ball2);

            Thread thread1 = new Thread(runBall(ball1));
            thread1.setPriority(ball1.getPriority());
            thread1.start();

            try {
                thread1.join();
            } catch (Exception e) {

            }
            
            Thread thread2 = new Thread(runBall(ball2));
            thread2.setPriority(ball2.getPriority());
            thread2.start();
       });

       thread.start();
       System.out.println("Thread name = " + thread.getName());
    }

    public void doTask5() {
        Counter counter = new Counter();

        int numb = 100_000;

        Thread thread1 = new Thread(()->{
            for (int i = 0; i < numb; i++) {
                counter.syncInc();
            }
        });
        Thread thread2 = new Thread(()->{
            for (int i = 0; i < numb; i++) {
                counter.syncDec();
            }
        });
        
        try {
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        } catch (Exception e) {

        }
        int methCounter = counter.getCount();

        Thread thread3 = new Thread(()->{
            for (int i = 0; i < numb; i++) {
                counter.syncBlockInc();
            }
        });
        Thread thread4 = new Thread(()->{
            for (int i = 0; i < numb; i++) {
                counter.syncBlockDec();
            }
        });

        try {
            thread3.start();
            thread4.start();
            thread3.join();
            thread4.join();
        } catch (Exception e) {

        }
        int blockCounter = counter.getCount();

        Thread thread5 = new Thread(()->{
            for (int i = 0; i < numb; i++) {
                counter.lockInc();
            }
        });

        Thread thread6 = new Thread(()->{
            for (int i = 0; i < numb; i++) {
                counter.lockDec();
            }
        });
        try {
            thread5.start();
            thread6.start();
            thread5.join();
            thread6.join();
        } catch (Exception e) {

        }
        int lockCounter = counter.getCount();

        Thread thread7 = new Thread(()->{
            for (int i = 0; i < numb; i++) {
                counter.syncBlockLockInc();
            }
        });
        Thread thread8 = new Thread(()->{
            for (int i = 0; i < numb; i++) {
                counter.syncBlockLockDec();
            }
        });
        try {
            thread7.start();
            thread8.start();
            thread7.join();
            thread8.join();
        } catch (Exception e) {

        }
        int syncBlockLockCounter = counter.getCount();

        System.out.println("Sync meth: " + methCounter);
        System.out.println("Sync block: " + blockCounter);
        System.out.println("Sync block lock: " + syncBlockLockCounter);
        System.out.println("Lock: " + lockCounter);
    }
}

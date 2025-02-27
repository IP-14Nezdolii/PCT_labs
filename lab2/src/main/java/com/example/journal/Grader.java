package com.example.journal;

import java.util.Random;
import java.util.concurrent.locks.LockSupport;

public class Grader implements Runnable {
    private static long WAITING = 100_000;

    private static int CURRENT_WEEK = 0;

    private static int N_WEEKS = 10;
    private static int N_MARKS = 6;

    private static Object outputLock = new Object();

    private int LAST_WEEK = 0;
    private final Journal journal;
    private final Random random;
    


    public Grader(Journal journal, Random random) {
        this.journal = journal;
        this.random = random;
    }

    public static void setNWeeks(int nWeeks) {
        N_WEEKS = nWeeks;
    }

    public static void setNMarks(int nMarks) {
        N_MARKS = nMarks;
    }

    public static void newWeek() {
        CURRENT_WEEK++;
    }

    @Override
    public void run() {
        while (LAST_WEEK < N_WEEKS) {
            while (LAST_WEEK == CURRENT_WEEK)
                LockSupport.parkNanos(WAITING); //Thread.sleep(500);
                
            addMarks();
            this.LAST_WEEK++;

            System.out.println(Thread.currentThread().getName() +
                " Week: " + LAST_WEEK);
        }
    }

    void addMarks() {
        int groupNumb = journal.groupNumb();
        int studentNumb = journal.studentsNumb();

        int markNumb = random.nextInt(studentNumb / groupNumb);

        for (int i = 0; i < markNumb; i++) {
            int group = random.nextInt(groupNumb);
            int student = random.nextInt(studentNumb/groupNumb);

            var studentMarks = journal.getGroup(group).get(student);
            synchronized (studentMarks) {
                if (studentMarks.size() == N_MARKS)
                    continue;

                studentMarks.add(random.nextInt(41) + 60);
            }
        }
    }
}

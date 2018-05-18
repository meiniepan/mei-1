package com.wuyou.user;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    private void print15() {
        Thread1 thread1 = new Thread1();
        Thread1 thread2 = new Thread1();
        Thread1 thread3 = new Thread1();

        while (n<76){
            int i1 = thread1.run(n);
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int i2 = thread2.run(i1);
            try {
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            n = thread3.run(i2);
            try {
                thread3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int n = 1;

    class ThreadLetter extends Thread {
        @Override
        public void run() {
            for (char c = 'A'; c <= 'Z'; c++) {
                printer.print(c);
            }
        }
    }
    class ThreadNumber extends Thread {
        @Override
        public void run() {
            for (int j = 1; j < 53; j++) {
                printer.print(j);
            }
        }
    }

    Printer printer = new Printer();
    int index = 1;

    class Printer {
        private synchronized void print(int i) {
            while (index % 3 == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            index++;
            notifyAll();
            System.out.println("---" + i);
        }


        private synchronized void print(char c) {
            while (index % 3 != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            index++;
            notifyAll();
            System.out.println("____" + c);
        }
    }

    class Thread1 extends Thread {
        public int run(int i) {
            for (int j = i; j < i + 5; j++) {
                System.out.println(Thread.currentThread() + ",,,,," + j);
            }
            return i + 5;
        }


    }
}
package com.wuyou.user;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals("haha", "haha");
        int[][] arr ={{0,0,1,1},{1,2,1,1}};
        System.out.println(arr[1][1]);
    }

    public long count(int... n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n.length; i++) {
            sb.append(n[i]);
        }

        char[] arr = sb.toString().toCharArray();
        for (int i = 0; i < arr.length-1 ; i++) {
            for (int j = 0; j < arr.length-1-i ; j++) {
                if (arr[j] < arr[j+1]) {
                    char temp;
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        sb = new StringBuilder();
        for (char c : arr) {
            sb.append(c);
        }
        System.out.println(sb.toString());
        return Long.parseLong(sb.toString());
    }

    public void ccc() {
        char[] arr = {'1', '3', '8', '2', '9', '0'};
        System.out.println("排序前数组为：");
        for (char num : arr) {
            System.out.print(num + " ");
        }
        for (int i = 0; i < arr.length - 1; i++) {//外层循环控制排序趟数
            for (int j = 0; j < arr.length - 1 - i; j++) {//内层循环控制每一趟排序多少次
                if (arr[j] > arr[j + 1]) {
                    char temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        System.out.println();
        System.out.println("排序后的数组为：");
        for (char num : arr) {
            System.out.print(num + " ");
        }
    }

    private void print15() {
        Thread1 thread1 = new Thread1();
        Thread1 thread2 = new Thread1();
        Thread1 thread3 = new Thread1();

        while (n < 76) {
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
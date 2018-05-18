package com.wuyou.user;

import org.junit.Test;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by DELL on 2018/5/16.
 */

public class TestDemo2 {
    @Test
    public void test1() {
        MyThread a = new MyThread("a", 1);
        MyThread b = new MyThread("b", 0);
        a.start();
        b.start();

        BlockingDeque blockingDeque = new LinkedBlockingDeque();
    }

    private int i = 1;

    class MyThread extends Thread {
        String name;
        int flag;

        public MyThread(String name, int flag) {
            super(name);
            this.name = name;
            this.flag = flag;
        }

        @Override
        public void run() {
            while (i < 21) {
                print();
            }
        }

        private void print() {
            synchronized (TestDemo2.this) {
                while (i % 2 == flag) {
                    try {
                        TestDemo2.this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("thread " + name + "------" + i);
                i++;
                TestDemo2.this.notifyAll();
            }
        }
    }
}

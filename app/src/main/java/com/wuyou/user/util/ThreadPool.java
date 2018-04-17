package com.wuyou.user.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by DELL on 2018/4/16.
 */

public class ThreadPool {
    private final ExecutorService executorService;

    private static ThreadPool threadPool;

    private ThreadPool() {
        final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        final int KEEP_ALIVE_TIME = 1;
        final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
        executorService = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES * 2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, taskQueue);
    }

    public static ThreadPool getIns() {
        if (null == threadPool) {
            threadPool = new ThreadPool();
        }
        return threadPool;
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    public void shutDown() {
        executorService.shutdownNow();
    }

    public boolean isShutDown() {
        return executorService.isShutdown();
    }
}

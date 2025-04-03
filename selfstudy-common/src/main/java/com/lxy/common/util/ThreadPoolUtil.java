package com.lxy.common.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/10 18:17
 * @version 1.0
 */

@Slf4j
public class ThreadPoolUtil {

    private static volatile ThreadPoolExecutor threadPool;

    private static final int CORE_POOL_SIZE;
    private static final int MAXIMUM_POOL_SIZE;
    private static final long KEEP_ALIVE_TIME = 60L;
    private static final int QUEUE_CAPACITY = 100;

    // 静态代码块初始化线程池的核心线程数和最大线程数
    static {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        log.info("CPU核心数: {}", availableProcessors);
        // 根据系统的 CPU 核心数来设置线程池大小
        // 至少4个线程
        CORE_POOL_SIZE = Math.max(4, availableProcessors);
        // 最大线程数不超过CPU核心数的两倍
        MAXIMUM_POOL_SIZE = Math.max(8, availableProcessors * 2);
    }

    /**
     * corePoolSize线程池的核心线程数
     * maximumPoolSize能容纳的最大线程数
     * keepAliveTime空闲线程存活时间
     * unit 存活的时间单位
     * workQueue 存放提交但未执行任务的队列
     * threadFactory 创建线程的工厂类
     * handler 等待队列满后的拒绝策略
     */
    public static ThreadPoolExecutor getInstance() {
        if (threadPool == null) {
            synchronized (ThreadPoolUtil.class) {
                if (threadPool == null) {
                    threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                            KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                            new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }
        return threadPool;
    }

    //执行任务
    public static void execute(Runnable command){
        getInstance().execute(command);
    }

    //有返回值
    public static <T> Future<T> submit(Callable<T> task){
        return getInstance().submit(task);
    }

    //关闭线程池
    public static void shutdown(){
        log.info("关闭线程池");
        if (threadPool != null) {
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow(); // 强制关闭
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void logStatus() {
        ThreadPoolExecutor executor = getInstance();
        log.info("正在主动执行任务的线程数: {}", executor.getActiveCount());
        log.info("已完成执行的任务的大致总数: {}", executor.getCompletedTaskCount());
        log.info("队列数量: {}", executor.getQueue().size());
    }


    public static void main(String[] args) {
        // 用法
        ThreadPoolUtil.execute(() ->{
            //执行具体逻辑
        });
        ThreadPoolUtil.submit(() -> {
            //执行具体逻辑，需要有返回值；
            return null;
        });

        // 用来存储所有 CompletableFuture
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 任务内容
        }, ThreadPoolUtil.getInstance());
        // 将每个 CompletableFuture 对象加入到 futures 列表中
        futures.add(future);

        // 任务完成后进行回调，不会阻塞主线程
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.thenRun(() -> {
            // 所有任务完成后执行的操作
        });
    }

}

package com.spring.college.myspringcollege.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.netflix.discovery.TimedSupervisorTask;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 芒草<xiongwen>
 * @DATE: 2021/1/15 5:37 下午
 */
@RestController
@Slf4j
public class TimerController {

    private static final ScheduledExecutorService scheduler;
    private static final AtomicInteger atomicInteger = new AtomicInteger();
    private static final ThreadPoolExecutor cacheRefreshExecutor;

    static {
        scheduler = Executors.newScheduledThreadPool(2,
                new ThreadFactoryBuilder()
                        .setNameFormat("DiscoveryClient-%d")
                        .setDaemon(true)
                        .build());
        cacheRefreshExecutor = new ThreadPoolExecutor(
                1, 2, 0, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadFactoryBuilder()
                        .setNameFormat("DiscoveryClient-CacheRefreshExecutor-%d")
                        .setDaemon(true)
                        .build()
        );  // use direct handoff
    }


    /**
     * 服务消费者 刷新缓存策略
     * TimedSupervisorTask -> Wrapped subtasks must be thread safe.
     */
    @RequestMapping("go")
    public void timedSupervisorTask() {
        int i = 0;
        // 延时一次性执行
        scheduler.schedule(
                new TimedSupervisorTask(
                        "cacheRefresh",
                        scheduler,
                        /** use build Future to do cancel when ex*/
                        cacheRefreshExecutor,
                        1,
                        TimeUnit.SECONDS,
                        10,
                        () -> {
                            System.out.println(atomicInteger.incrementAndGet());
                            try {
                                // 内部会因为future.cancel 而中断睡眠
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                ),
                1, TimeUnit.SECONDS);
    }


    @SneakyThrows
    public static void main(String[] args) {
        scheduler.schedule(() -> {
                    System.out.println(new Date());
                }, 1, TimeUnit.SECONDS
        );


    }

}

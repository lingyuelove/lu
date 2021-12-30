package com.luxuryadmin.common.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.*;

/**
 * 线程池的单例模式;静态内部类单例;<br/>
 * 只有第一次调用getInstance方法时，虚拟机才加载 Inner 并初始化instance ，
 * 只有一个线程可以获得对象的初始化锁，其他线程无法进行初始化，
 * 保证对象的唯一性。目前此方式是所有单例模式中最推荐的模式.
 *
 * @author monkey king
 * @Date 2019/12/27 2:33
 */
@Slf4j
public class ThreadUtils {

    public ScheduledExecutorService executorService;

    private boolean monitor = false;

    private boolean closeMonitor = false;

    private ThreadUtils() {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        ThreadFactory namedThreadFactory = threadFactoryBuilder.setNameFormat("ThreadUtils-%d").build();
        executorService = new ScheduledThreadPoolExecutor(100, namedThreadFactory);

    }

    private static class Inner {
        private static final ThreadUtils INSTANCE = new ThreadUtils();
    }

    /**
     * 获得线程池的实例; ScheduledExecutorService
     *
     * @return
     */
    public static ThreadUtils getInstance() {
        return Inner.INSTANCE;
    }

    public void setMonitor(boolean monitor) {
        this.monitor = monitor;
    }

    public void setCloseMonitor(boolean closeMonitor) {
        this.closeMonitor = closeMonitor;
    }

    /**
     * 启动一个线程去监控整个线程池运行情况;<br/>
     * 调用该方法之前,请ThreadUtils.isMonitor设置为true;
     */
    public void monitorThread() {
        if (monitor) {
            executorService.execute(this::showThreadInfo);
        }
    }

    private void showThreadInfo() {
        log.info("==============线程监控【已开启】==================");
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        ThreadPoolExecutor tpe = ((ThreadPoolExecutor) executorService);
        while (true) {
            System.out.println();

            int queueSize = tpe.getQueue().size();
            log.info("当前排队线程数：" + queueSize);

            int activeCount = tpe.getActiveCount();
            log.info("当前活动线程数：" + activeCount);

            long completedTaskCount = tpe.getCompletedTaskCount();
            log.info("执行完成线程数：" + completedTaskCount);

            long taskCount = tpe.getTaskCount();
            log.info("总线程数：" + taskCount);

            int corePoolSize = tpe.getCorePoolSize();
            log.info("corePoolSize: " + corePoolSize);

            log.info("线程总数为 = " + bean.getThreadCount());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (closeMonitor) {
                break;
            }
        }
        monitor = false;
        closeMonitor = false;
        log.info("==============线程监控【已关闭】==================");
    }


    public static void main(String[] args) {

        // Executors.newSingleThreadExecutor().execute(() -> System.out.println("hahah"));


        //demo();
        // demo2();
        ThreadUtils threadUtils = ThreadUtils.getInstance();
        threadUtils.demo3();

    }

    private int num = 1;
    private boolean ok = true;

    private void demo3() {
        ThreadUtils threadUtils = ThreadUtils.getInstance();
        ExecutorService executorService = threadUtils.executorService;
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        for (int i = 0; i < 10000; i++) {
            if (i == 0) {
                threadUtils.setMonitor(true);
                threadUtils.monitorThread();
            }

            executorService.execute(() -> {
                try {
                    num++;
                    Thread.sleep(1000);
                    if (num >= 1000 && ok) {
                        threadUtils.setCloseMonitor(true);
                        ok = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("===遍历完成====");
        threadUtils.setMonitor(true);
        showThreadInfo();


    }

    private void demo2() {
        //用于获取到本java进程，进而获取总线程数
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        String jvmName = runtimeBean.getName();
        System.out.println("JVM Name = " + jvmName);
        long pid = Long.valueOf(jvmName.split("@")[0]);
        System.out.println("JVM PID  = " + pid);
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        int n = 1;

        ExecutorService executorService = ThreadUtils.getInstance().executorService;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 10; j++) {
                executorService.execute(() -> {
                    num++;
                    System.out.println(num + "当前线程总数为：" + bean.getThreadCount());
                });
            }
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ThreadPoolExecutor tpe = ((ThreadPoolExecutor) executorService);

        while (true) {
            int queueSize = tpe.getQueue().size();
            System.out.println("当前排队线程数：" + queueSize);

            int activeCount = tpe.getActiveCount();
            System.out.println("当前活动线程数：" + activeCount);

            long completedTaskCount = tpe.getCompletedTaskCount();
            System.out.println("执行完成线程数：" + completedTaskCount);

            long taskCount = tpe.getTaskCount();
            System.out.println("总线程数：" + taskCount);
            System.out.println("线程总数为 = " + bean.getThreadCount());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void demo1() {
        long st = System.currentTimeMillis();
        int num = 1;
        for (int i = 0; i < 1000; i++) {
            System.out.println("====睡眠====");
            num++;
        }
        long et = System.currentTimeMillis();
        System.out.println(et - st);
    }

    private void demo() {
        System.out.println(Runtime.getRuntime().availableProcessors());
        ExecutorService executorService = ThreadUtils.getInstance().executorService;
        long st = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {

            final int index = i;
            executorService.execute(() -> {
                try {
                    num++;
                    try {
                        //Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " index = " + index);
                    System.out.println("et:" + System.currentTimeMillis());
                } catch (Exception e) {
                    System.out.println("ssss");
                }
            });
        }
        System.out.println(num + "on the main thread..." + st);
    }
}

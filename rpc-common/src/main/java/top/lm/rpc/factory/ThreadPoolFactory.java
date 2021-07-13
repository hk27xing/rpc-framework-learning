package top.lm.rpc.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author hk27xing
 * @Description 创建线程池的工具类
 * @createTime 2021/7/8 22:12
 */
public class ThreadPoolFactory {

    /* 线程池参数 */
    private static final int CORE_POOL_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private ThreadPoolFactory() {}

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix) {
        return createDefaultThreadPool(threadNamePrefix, false);
    }

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix, Boolean daemon) {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(CORE_POOL_SIZE,
                                      MAXIMUM_POOL_SIZE,
                                      KEEP_ALIVE_TIME,
                                      TimeUnit.SECONDS,
                                      workQueue,
                                      threadFactory);
    }

    /**
     * @Description 创建 ThreadFactory
     *
     * @param threadNamePrefix  作为创建线程名字的前缀
     * @param daemon            指定是否是 守护线程
     */
    private static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d")
                                                 .setDaemon(daemon)
                                                 .build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d")
                                                 .build();
            }
        }

        return Executors.defaultThreadFactory();
    }
}

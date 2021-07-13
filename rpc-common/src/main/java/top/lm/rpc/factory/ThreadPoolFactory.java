package top.lm.rpc.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
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

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolFactory.class);
    private static Map<String, ExecutorService> threadPoolsMap = new ConcurrentHashMap<>();

    private ThreadPoolFactory() {}

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix) {
        return createDefaultThreadPool(threadNamePrefix, false);
    }

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix, Boolean daemon) {
        ExecutorService pool = threadPoolsMap.computeIfAbsent(threadNamePrefix,
                                                              k -> createThreadPool(threadNamePrefix, daemon));
        if (pool.isShutdown() || pool.isTerminated()) {
            threadPoolsMap.remove(threadNamePrefix);
            pool = createThreadPool(threadNamePrefix, daemon);
            threadPoolsMap.put(threadNamePrefix, pool);
        }
        return pool;
    }

    public static void shutDownAll() {
        logger.info("关闭所有线程池...");
        threadPoolsMap.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            logger.info("关闭线程池 [{}] [{}]", entry.getKey(), executorService.isTerminated());

            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("关闭线程池失败!");
                executorService.shutdownNow();
            }
        });
    }

    private static ExecutorService createThreadPool(String threadNamePrefix, Boolean daemon) {
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

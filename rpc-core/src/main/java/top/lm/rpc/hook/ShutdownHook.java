package top.lm.rpc.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.factory.ThreadPoolFactory;
import top.lm.rpc.util.NacosUtil;

import java.util.concurrent.ExecutorService;

/**
 * @author hk27xing
 * @Description **
 * @createTime 2021/7/13 15:56
 */
public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);
    private static final String THREAD_NAME_PREFIX = "shutdown-hook";

    private ExecutorService threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            threadPool.shutdown();
        }));
    }

}

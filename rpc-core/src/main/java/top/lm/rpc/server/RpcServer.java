package top.lm.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.registry.ServiceRegistry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Description 远程方法调用的服务端
 * @author hk27xing
 * */
public class RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private static final int CORE_POOL_SIZE          = 5;
    private static final int MAXMUM_POOL_SIZE        = 50;
    private static final int KEEP_ALIVE_TIME         = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;

    public RpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry                 = serviceRegistry;

        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory          = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
                                            MAXMUM_POOL_SIZE,
                                            KEEP_ALIVE_TIME,
                                            TimeUnit.SECONDS,
                                            workingQueue,
                                            threadFactory);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器启动...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.info("客户端连接: {}:{}", socket.getInetAddress(), socket.getLocalPort());
                threadPool.execute(new RequestHanlerThread(socket, requestHandler, serviceRegistry));
            }
        } catch (IOException e) {
            logger.error("连接时有错误: ", e);
        }
    }

}

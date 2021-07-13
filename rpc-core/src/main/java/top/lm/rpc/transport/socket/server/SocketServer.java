package top.lm.rpc.transport.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.handler.RequestHandler;
import top.lm.rpc.hook.ShutdownHook;
import top.lm.rpc.provider.ServiceProvider;
import top.lm.rpc.provider.ServiceProviderImpl;
import top.lm.rpc.registry.NacosServiceRegistry;
import top.lm.rpc.transport.RpcServer;
import top.lm.rpc.enumeration.RpcError;
import top.lm.rpc.exception.RpcException;
import top.lm.rpc.registry.ServiceRegistry;
import top.lm.rpc.serializer.CommonSerializer;
import top.lm.rpc.factory.ThreadPoolFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Description Socket 传输方式 远程方法调用的服务端
 * @author hk27xing
 * */
public class SocketServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    private static final String THREAD_NAME_PREFIX = "socket-rpc-server";

    private final ExecutorService threadPool;
    private final String host;
    private final int port;
    private final RequestHandler requestHandler = new RequestHandler();
    private final CommonSerializer serializer;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    public <T> void publishService(T service, Class<T> serviceClass) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }

        serviceProvider.addServiceProvider(service, serviceClass);
        serviceRegistry.registry(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            logger.info("服务器启动...");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.info("客户端连接: {}:{}", socket.getInetAddress(), socket.getLocalPort());
                threadPool.execute(new SocketRequestHanlerThread(socket, requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("连接时有错误: ", e);
        }
    }

}

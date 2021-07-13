package top.lm.rpc.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.hook.ShutdownHook;
import top.lm.rpc.provider.ServiceProvider;
import top.lm.rpc.provider.ServiceProviderImpl;
import top.lm.rpc.registry.NacosServiceRegistry;
import top.lm.rpc.registry.ServiceRegistry;
import top.lm.rpc.transport.RpcServer;
import top.lm.rpc.codec.CommonDecoder;
import top.lm.rpc.codec.CommonEncoder;
import top.lm.rpc.enumeration.RpcError;
import top.lm.rpc.exception.RpcException;
import top.lm.rpc.serializer.CommonSerializer;

import java.net.InetSocketAddress;

/**
 * @author hk27xing
 * @Description 用 netty 实现的 NIO 方式的服务端类
 * @createTime 2021/7/6 17:03
 */
public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final int port;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    private final CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public NettyServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
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
        ShutdownHook.getShutdownHook().addClearAllHook();
        EventLoopGroup bossGroup   = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                           .channel(NioServerSocketChannel.class)
                           .handler(new LoggingHandler(LogLevel.INFO))
                           .option(ChannelOption.SO_BACKLOG, 256)
                           .option(ChannelOption.SO_KEEPALIVE, true)
                           .childOption(ChannelOption.TCP_NODELAY, true)
                           .childHandler(new ChannelInitializer<SocketChannel>() {
                               @Override
                               protected void initChannel(SocketChannel socketChannel) throws Exception {
                                   ChannelPipeline pipeline = socketChannel.pipeline();
                                   pipeline.addLast(new CommonEncoder(serializer))
                                           .addLast(new CommonDecoder())
                                           .addLast(new NettyServerHandler());
                               }
                           });

            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

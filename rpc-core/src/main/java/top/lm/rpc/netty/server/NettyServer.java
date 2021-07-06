package top.lm.rpc.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.RpcServer;
import top.lm.rpc.codec.CommonDecoder;
import top.lm.rpc.codec.CommonEncoder;
import top.lm.rpc.serializer.JsonSerializer;

/**
 * @author hk27xing
 * @Description 用 netty 实现的 NIO 方式的服务端类
 * @createTime 2021/7/6 17:03
 */
public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Override
    public void start(int port) {
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
                                   pipeline.addLast(new CommonEncoder(new JsonSerializer()))
                                           .addLast(new CommonDecoder())
                                           .addLast(new NettyServerHandler());
                               }
                           });

            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

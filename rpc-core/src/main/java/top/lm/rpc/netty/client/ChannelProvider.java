package top.lm.rpc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.codec.CommonDecoder;
import top.lm.rpc.codec.CommonEncoder;
import top.lm.rpc.enumeration.RpcError;
import top.lm.rpc.exception.RpcException;
import top.lm.rpc.serializer.CommonSerializer;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author hk27xing
 * @Description 用于获取 Channel 对象
 * @createTime 2021/7/10 10:10
 */
public class ChannelProvider {

    private static final Logger logger = LoggerFactory.getLogger(ChannelProvider.class);

    private static EventLoopGroup eventLoopGroup;
    private static final Bootstrap bootstrap = initializeBootstrap();
    private static final int MAX_RETRY_COUNT = 5;
    private static Channel channel           = null;
    private static int orderTime             = 1;

    private static Bootstrap initializeBootstrap() {
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                 .channel(NioSocketChannel.class)
                 /* 连接超时时间 */
                 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                 /* 开启 TCP 底层心跳机制 */
                 .option(ChannelOption.SO_KEEPALIVE, true)
                 /* 启用 Nagle 算法, 尽可能的发送大数据块，减少网络传输 */
                 .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }

    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer) {
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                /* 自定义序列化编解码器 */
                /* RpcResponse -> ByteBuf */
                socketChannel.pipeline().addLast(new CommonEncoder(serializer))
                                        .addLast(new CommonDecoder())
                                        .addLast(new NettyClientHandler());
            }
        });

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            connect(bootstrap, inetSocketAddress, countDownLatch);
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("获取 channel 时有错误发生: ", e);
        }
        return channel;
    }

    private static void connect(Bootstrap bootstrap,
                                InetSocketAddress inetSocketAddress,
                                CountDownLatch countDownLatch) {
        connect(bootstrap, inetSocketAddress, MAX_RETRY_COUNT, countDownLatch);
    }

    private static void connect(Bootstrap bootstrap,
                                InetSocketAddress inetSocketAddress,
                                int retryCount,
                                CountDownLatch countDownLatch) {
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                logger.info("客户端连接成功");
                channel = future.channel();
                countDownLatch.countDown();
                return;
            }

            if (retryCount == 0) {
                logger.error("客户端连接失败: 重试次数用完, 放弃连接!");
                countDownLatch.countDown();
                throw new RpcException(RpcError.CLIENT_CONNECTION_FAILURE);
            }

            /* 第几次重新连接 */
            int order = orderTime++;
            /* 本次重连的时间间隔, 每过一次就翻倍 */
            int delay = 1 << order;
            logger.error("{}: 连接失败, 第 {} 次重新连接...", new Date(), order);
            bootstrap.config()
                     .group()
                     .schedule(() -> connect(bootstrap,
                                             inetSocketAddress,
                                            retryCount - 1,
                                             countDownLatch), delay, TimeUnit.SECONDS);
        });
    }

}

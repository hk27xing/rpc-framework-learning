package top.lm.rpc.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.registry.NacosServiceDiscovery;
import top.lm.rpc.registry.NacosServiceRegistry;
import top.lm.rpc.registry.ServiceDiscovery;
import top.lm.rpc.registry.ServiceRegistry;
import top.lm.rpc.transport.RpcClient;
import top.lm.rpc.entity.RpcRequest;
import top.lm.rpc.entity.RpcResponse;
import top.lm.rpc.enumeration.RpcError;
import top.lm.rpc.exception.RpcException;
import top.lm.rpc.serializer.CommonSerializer;
import top.lm.rpc.util.RpcMessageChecker;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hk27xing
 * @Description 用 netty 实现的 NIO 方式的客户端类
 * @createTime 2021/7/6 20:08
 */
public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static final EventLoopGroup group;
    private static final Bootstrap bootstrap;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                 .channel(NioSocketChannel.class)
                 .option(ChannelOption.SO_KEEPALIVE, true);
    }

    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;

    public NettyClient() {
        this(DEFAULT_SERIALIZER);
    }

    public NettyClient(Integer serializer) {
        this.serviceDiscovery = new NacosServiceDiscovery();
        this.serializer = CommonSerializer.getByCode(serializer);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }

        AtomicReference<Object> result = new AtomicReference<>(null);
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);

            if (!channel.isActive()) {
                group.shutdownGracefully();
                return null;
            }

            channel.writeAndFlush(rpcRequest).addListener(f -> {
                if (f.isSuccess()) {
                    logger.info("客户端发送信息: {}", rpcRequest);
                } else {
                    logger.error("发送消息时有错误发生: ", f.cause());
                }
            });

            channel.closeFuture().sync();
            /* 通过 AttributeKey 的方式阻塞获得全局可见的返回结果 */
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
            RpcResponse rpcResponse = channel.attr(key).get();
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            result.set(rpcResponse.getData());
        } catch (InterruptedException e) {
            logger.error("发送消息时有错误发生: ", e);
            Thread.currentThread().interrupt();
        }
        return result.get();
    }

}

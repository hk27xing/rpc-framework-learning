package top.lm.rpc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.RpcClient;
import top.lm.rpc.codec.CommonDecoder;
import top.lm.rpc.codec.CommonEncoder;
import top.lm.rpc.entity.RpcRequest;
import top.lm.rpc.entity.RpcResponse;
import top.lm.rpc.enumeration.RpcError;
import top.lm.rpc.exception.RpcException;
import top.lm.rpc.serializer.CommonSerializer;
import top.lm.rpc.serializer.JsonSerializer;
import top.lm.rpc.serializer.KryoSerializer;

/**
 * @author hk27xing
 * @Description 用 netty 实现的 NIO 方式的客户端类
 * @createTime 2021/7/6 20:08
 */
public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static final Bootstrap bootstrap;

    private String host;
    private int port;

    private CommonSerializer serializer;

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                 .channel(NioSocketChannel.class)
                 .option(ChannelOption.SO_KEEPALIVE, true);
    }

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new CommonDecoder())
                        .addLast(new CommonEncoder(serializer))
                        .addLast(new NettyClientHandler());
            }
        });

        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            logger.info("客户端连接到服务器: {}:{}", host, port);
            Channel channel = future.channel();

            if (channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(f -> {
                    if (f.isSuccess()) {
                        logger.info(String.format("客户端发送信息: %s", rpcRequest.toString()));
                    } else {
                        logger.error("发送消息时有错误发生: ", f.cause());
                    }
                });

                channel.closeFuture().sync();
                /* 通过 AttributeKey 的方式阻塞获得全局可见的返回结果 */
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            logger.error("发送消息时有错误发生: ", e);
        }
        return null;
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

}

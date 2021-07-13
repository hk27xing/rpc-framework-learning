package top.lm.rpc.transport.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.factory.SingletonFactory;
import top.lm.rpc.handler.RequestHandler;
import top.lm.rpc.entity.RpcRequest;
import top.lm.rpc.entity.RpcResponse;
import top.lm.rpc.factory.ThreadPoolFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author hk27xing
 * @Description Netty 客户端处理期
 * @createTime 2021/7/6 19:31
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private final ExecutorService threadPool;
    private final RequestHandler requestHandler;

    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
        this.threadPool     = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                RpcRequest rpcRequest) throws Exception {
        threadPool.execute(() -> {
            try {
                logger.info("服务器接收到请求: {}", rpcRequest);
                Object result        = requestHandler.handle(rpcRequest);
                ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result, rpcRequest.getRequestId()));
                future.addListener(ChannelFutureListener.CLOSE);
            } finally {
                ReferenceCountUtil.release(rpcRequest);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        logger.error("过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}

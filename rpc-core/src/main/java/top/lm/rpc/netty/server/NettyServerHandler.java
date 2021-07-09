package top.lm.rpc.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.RequestHandler;
import top.lm.rpc.entity.RpcRequest;
import top.lm.rpc.entity.RpcResponse;
import top.lm.rpc.registry.DefaultServiceRegistry;
import top.lm.rpc.registry.ServiceRegistry;

/**
 * @author hk27xing
 * @Description Netty 客户端处理期
 * @createTime 2021/7/6 19:31
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;

    static {
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                RpcRequest rpcRequest) throws Exception {
        try {
            logger.info(String.format("服务器接收到请求: %s", rpcRequest));
            String interfaceName = rpcRequest.getInterfaceName();
            Object service       = serviceRegistry.getService(interfaceName);
            Object result        = requestHandler.handle(rpcRequest, service);
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result, rpcRequest.getRequestId()));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(rpcRequest);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        logger.error("过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}

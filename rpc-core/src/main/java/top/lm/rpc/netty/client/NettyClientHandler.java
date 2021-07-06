package top.lm.rpc.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.entity.RpcResponse;

/**
 * @author hk27xing
 * @Description Netty 客户端处理器
 * @createTime 2021/7/6 20:04
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext cxt,
                                RpcResponse rpcResponse) throws Exception {
        try {
            logger.info(String.format("客户端接收到消息: %s", rpcResponse));
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            cxt.channel().attr(key).set(rpcResponse);
            cxt.channel().close();
        } finally {
            ReferenceCountUtil.release(rpcResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        logger.error("过程调用时有错误发生: ", cause);
        cause.printStackTrace();
        ctx.close();
    }

}

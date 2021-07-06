package top.lm.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.entity.RpcRequest;
import top.lm.rpc.entity.RpcResponse;
import top.lm.rpc.enumeration.PackageType;
import top.lm.rpc.enumeration.RpcError;
import top.lm.rpc.exception.RpcException;
import top.lm.rpc.serializer.CommonSerializer;

import java.util.List;

/**
 * @author hk27xing
 * @Description 通用的解码拦截器
 * @createTime 2021/7/6 19:18
 */
public class CommonDecoder extends ReplayingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf in,
                          List<Object> out) throws Exception {
        int magicNumber = in.readInt();
        if (magicNumber != MAGIC_NUMBER) {
            logger.error("不识别的协议包: {}", magicNumber);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }

        int packageCode = in.readInt();
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别的数据包: {}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }

        int serializerCode = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null) {
            logger.error("不识别的反序列化器: {}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }

        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }
}

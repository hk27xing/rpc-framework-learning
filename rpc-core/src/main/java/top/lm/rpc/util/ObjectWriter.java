package top.lm.rpc.util;

import top.lm.rpc.entity.RpcRequest;
import top.lm.rpc.enumeration.PackageType;
import top.lm.rpc.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author hk27xing
 * @Description Socket 传输方式从输出流中写入字节并序列化
 * @createTime 2021/7/9 17:58
 */
public class ObjectWriter {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private static byte[] intToBytes(int value) {
        byte[] des = new byte[4];
        des[3] = (byte) ((value >> 24) & 0xFF);
        des[2] = (byte) ((value >> 16) & 0xFF);
        des[1] = (byte) ((value >> 8)  & 0xFF);
        des[0] = (byte) (value         & 0xFF);
        return des;
    }

    public static void writeObject(OutputStream outputStream,
                                   Object object,
                                   CommonSerializer serializer) throws IOException {
        outputStream.write(intToBytes(MAGIC_NUMBER));
        if (object instanceof RpcRequest) {
            outputStream.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        } else {
            outputStream.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }

        outputStream.write(intToBytes(serializer.getCode()));
        byte[] bytes = serializer.serialize(object);
        outputStream.write(intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();
    }

}

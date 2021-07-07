package top.lm.rpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.entity.RpcRequest;
import top.lm.rpc.entity.RpcResponse;
import top.lm.rpc.enumeration.SerializerCode;
import top.lm.rpc.exception.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author hk27xing
 * @Description Kryo 序列化
 * @createTime 2021/7/7 16:58
 */
public class KryoSerializer implements CommonSerializer{

    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    /* Kryo 可能存在线程安全问题, 这里放在 ThreadLocal 里面 */
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {

            Kryo kryo = kryoThreadLocal.get();
            /* 将对象写入 output 中 */
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();

            /* 获得对象的字节数组 */
            return output.toBytes();
        } catch (IOException e) {
            logger.error("序列化时有错误发生: ", e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {

            Kryo kryo = kryoThreadLocal.get();
            Object obj = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return obj;
        } catch (IOException e) {
            logger.error("反序列化时有错误发生: ", e);
            throw new SerializeException("反序列化时有错误发生");
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("KRYO").getCode();
    }

}

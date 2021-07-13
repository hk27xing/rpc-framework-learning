package top.lm.rpc.transport;

import top.lm.rpc.serializer.CommonSerializer;

/**
 * @Description 服务端通用接口
 * @author hk27xing
 * */
public interface RpcServer {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    void start();

    <T> void publishService(T service, Class<T> serviceClass);

}

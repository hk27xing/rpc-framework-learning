package top.lm.rpc.transport;

import top.lm.rpc.serializer.CommonSerializer;

/**
 * @Description 服务端通用接口
 * @author hk27xing
 * */
public interface RpcServer {

    void start();

    void setSerializer(CommonSerializer serializer);

    <T> void publishService(Object service, Class<T> serviceClass);

}

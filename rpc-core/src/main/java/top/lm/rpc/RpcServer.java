package top.lm.rpc;

import top.lm.rpc.serializer.CommonSerializer;

/**
 * @Description 服务端通用接口
 * @author hk27xing
 * */
public interface RpcServer {

    void start(int port);

    void setSerializer(CommonSerializer serializer);

}

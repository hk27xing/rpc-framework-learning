package top.lm.rpc.transport;

import top.lm.rpc.entity.RpcRequest;
import top.lm.rpc.serializer.CommonSerializer;

/**
 * @Description 客户端通用接口
 * @author hk27xing
 * */
public interface RpcClient {

    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);

}

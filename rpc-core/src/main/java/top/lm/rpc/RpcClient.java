package top.lm.rpc;

import top.lm.rpc.entity.RpcRequest;

/**
 * @Description 客户端通用接口
 * @author hk27xing
 * */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}

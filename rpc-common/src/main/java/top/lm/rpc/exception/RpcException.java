package top.lm.rpc.exception;

import top.lm.rpc.enumeration.RpcError;

/**
 * @author hk27xing
 * @Description RPC 调用过程异常
 * @createTime 2021/7/5 16:59
 */
public class RpcException extends RuntimeException{

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }

}

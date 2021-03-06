package top.lm.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hk27xing
 * @Description RPC调用过程中的错误
 * @createTime 2021/7/5 16:53
 */
@AllArgsConstructor
@Getter
public enum RpcError {

    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENTS_INTERFACE("注册的服务没有实现接口"),
    UNKNOWN_PROTOCOL("不识别的协议包"),
    UNKNOWN_SERIALIZER("不识别的序列化"),
    UNKNOWN_PACKAGE_TYPE("不识别的数据包类型"),
    SERIALIZER_NOT_FOUND("找不到序列化器"),
    RESPONSE_NOT_MATCHED("响应和请求号不匹配"),
    CLIENT_CONNECTION_FAILURE("客户端连接失败"),
    SERVICE_REGISTRY_CONNECTION_FAILURE("连接注册中心失败"),
    REGISTER_SERVICE_FAILED("注册服务失败");

    private final String message;

}

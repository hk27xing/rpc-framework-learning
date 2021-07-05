package top.lm.rpc.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 消费者向发送的请求对象
 * @author hk27xing
 * */
@Data
@Builder
public class RpcRequest implements Serializable {

    /* 接口名称 */
    private String interfaceName;

    /* 调用方法名称 */
    private String methodName;

    /* 调用方法参数类型 */
    private Class<?>[] paramTypes;

    /* 调用方法参数 */
    private Object[] parameters;

}

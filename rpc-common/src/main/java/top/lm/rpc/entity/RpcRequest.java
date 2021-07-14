package top.lm.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 消费者向发送的请求对象
 * @author hk27xing
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    /* 请求号 */
    private String requestId;

    /* 接口名称 */
    private String interfaceName;

    /* 调用方法名称 */
    private String methodName;

    /* 调用方法参数 */
    private Object[] parameters;

    /* 调用方法参数类型 */
    private Class<?>[] paramTypes;

    /* 是否是心跳包, 及时检测是否断线的一种机制 */
    private Boolean heartBeat;

}

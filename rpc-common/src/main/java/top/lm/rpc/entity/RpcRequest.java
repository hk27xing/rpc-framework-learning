package top.lm.rpc.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RpcRequest implements Serializable {

    private String interfaceName;   // 接口名称
    private String methodName;      // 调用方法名称
    private Class<?>[] paramTypes;  // 调用方法参数类型
    private Object[] parameters;    // 调用方法参数

}

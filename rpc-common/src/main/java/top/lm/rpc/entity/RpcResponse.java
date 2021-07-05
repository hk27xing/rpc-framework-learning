package top.lm.rpc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.lm.rpc.enumeration.ResponseCode;

import java.io.Serializable;

/**
 * @Description 生产者执行完成或出错后向消费者返回的结果对象
 * @author hk27xing
 * */
@Data
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {

    /* 相应状态码 */
    private Integer statusCode;

    /* 相应状态补充信息 */
    private String message;

    /* 相应数据 */
    private T data;

    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }

}

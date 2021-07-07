package top.lm.rpc.exception;/**
 * @Description 序列化异常
 * @author hk27xing
 * @createTime 2021/7/7 16:05
 * */
public class SerializeException extends RuntimeException{

    public SerializeException(String msg) {
        super(msg);
    }

}

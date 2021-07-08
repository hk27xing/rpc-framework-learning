package top.lm.rpc.serializer;

/**
 * @author hk27xing
 * @Description 通用序列化接口
 * @createTime 2021/7/6 18:31
 */
public interface CommonSerializer {

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new HessianSerializer();
            default:
                return null;
        }
    }

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

}

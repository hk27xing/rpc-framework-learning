package top.lm.rpc.provider;

/**
 * @author hk27xing
 * @Description 保存和提供服务示例
 * @createTime 2021/7/11 11:08
 */
public interface ServiceProvider {

    <T> void addServiceProvider(T service, Class<T> serviceClass);

    Object getServiceProvider(String serviceName);

}

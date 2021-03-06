package top.lm.rpc.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.enumeration.RpcError;
import top.lm.rpc.exception.RpcException;
import top.lm.rpc.registry.ServiceRegistry;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hk27xing
 * @Description 默认的服务注册表, 保存本地服务到服务端
 * @createTime 2021/7/5 19:50
 */
public class ServiceProviderImpl implements ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registryService    = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        if (!registryService.add(serviceName)) return;

        serviceMap.put(serviceName, service);
        logger.info("向接口: {} 注册服务: {}", serviceName, service.toString());
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}

package top.lm.rpc.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.enumeration.RpcError;
import top.lm.rpc.exception.RpcException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hk27xing
 * @Description **
 * @createTime 2021/7/5 19:50
 */
public class DefaultServiceRegistry implements ServiceRegistry{

    private static final Logger logger           = LoggerFactory.getLogger(DefaultServiceRegistry.class);

    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private final Set<String> registryService    = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void registry(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (!registryService.add(serviceName)) return;

        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENTS_INTERFACE);
        }

        for (Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }
        logger.info("向接口: {} 注册服务: {}", interfaces, serviceName);
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}

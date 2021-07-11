package top.lm.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @Description 服务注册中心通用接口
 * @author hk27xing
 */
public interface ServiceRegistry {

    /**
     * @Description 将一个服务注册进注册表
     * @param serviceName       服务名称
     * @param inetSocketAddress 提供服务的地址
     */
    void registry(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * @Description 根据服务名称查找服务实体
     * @param serviceName 服务名称
     * @return 服务实体
     */
    InetSocketAddress lookupService(String serviceName);

}

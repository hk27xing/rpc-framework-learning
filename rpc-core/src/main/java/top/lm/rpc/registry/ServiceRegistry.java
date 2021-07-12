package top.lm.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @Description 服务注册接口
 * @author hk27xing
 */
public interface ServiceRegistry {

    /**
     * @Description 将一个服务注册进注册表
     * @param serviceName       服务名称
     * @param inetSocketAddress 提供服务的地址
     */
    void registry(String serviceName, InetSocketAddress inetSocketAddress);

}

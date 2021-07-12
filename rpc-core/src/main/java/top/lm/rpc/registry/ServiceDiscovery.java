package top.lm.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author hk27xing
 * @Description 服务发现接口
 * @createTime 2021/7/12 15:47
 */
public interface ServiceDiscovery {

    /**
     * @Description 根据服务名称查找服务实体
     * @param serviceName 服务名称
     * @return 服务实体
     */
    InetSocketAddress lookupService(String serviceName);

}

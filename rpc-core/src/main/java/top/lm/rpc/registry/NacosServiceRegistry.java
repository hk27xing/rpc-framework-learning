package top.lm.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.enumeration.RpcError;
import top.lm.rpc.exception.RpcException;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author hk27xing
 * @Description Nacos 服务注册中心
 * @createTime 2021/7/11 11:15
 */
public class NacosServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    private static final String SERVER_ADDER = "127.0.0.1:8848";
    private static final NamingService namingService;

    static {
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDER);
        } catch (NacosException e) {
            logger.error("连接到 Nacos 时有错误发生: ", e);
            throw new RpcException(RpcError.SERVICE_REGISTRY_CONNECTION_FAILURE);
        }
    }

    @Override
    public void registry(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName,
                                           inetSocketAddress.getHostName(),
                                           inetSocketAddress.getPort());
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生: ", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务时有错误发生: ", e);
        }
        return null;
    }
}

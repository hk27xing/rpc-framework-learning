package top.lm.rpc.util;

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
 * @Description 管理 Nacos 连接等的工具类
 * @createTime 2021/7/12 15:07
 */
public class NacosUtil {

    private static final Logger logger = LoggerFactory.getLogger(NacosUtil.class);

    private static final String SERVER_ADDR = "127.0.0.1:8848";

    public static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            logger.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.SERVICE_REGISTRY_CONNECTION_FAILURE);
        }
    }

    public static void registryService(NamingService namingService,
                                       String serviceName,
                                       InetSocketAddress address) throws NacosException {
        namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
    }

    public static List<Instance> getAllInstance(NamingService namingService,
                                                String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
}

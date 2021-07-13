package top.lm.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.enumeration.RpcError;
import top.lm.rpc.exception.RpcException;
import top.lm.rpc.util.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author hk27xing
 * @Description Nacos 服务注册中心
 * @createTime 2021/7/11 11:15
 */
public class NacosServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    @Override
    public void registry(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生: ", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

}

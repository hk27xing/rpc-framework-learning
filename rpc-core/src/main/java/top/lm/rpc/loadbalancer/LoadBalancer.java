package top.lm.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author hk27xing
 * @createTime 2021/7/14 20:06
 */
public interface LoadBalancer {

    Instance select(List<Instance> instances);

}

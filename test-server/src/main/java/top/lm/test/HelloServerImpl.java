package top.lm.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.api.HelloObject;
import top.lm.rpc.api.HelloService;

/**
 * @Description HelloService 接口的实现类
 * @author hk27xing
 * */
public class HelloServerImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServerImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接受到: {}", object.getMessage());
        return "这是调用的返回值: id = " + object.getId();
    }

}

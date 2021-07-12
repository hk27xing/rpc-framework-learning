package top.lm.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.api.HelloObject;
import top.lm.rpc.api.HelloService;

/**
 * @Description HelloService 接口的实现类
 * @author hk27xing
 * */
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到: {}", object.getMessage());
        return "本次处理来自Netty传输";
    }

}

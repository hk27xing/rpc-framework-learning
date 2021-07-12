package top.lm.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.api.HelloObject;
import top.lm.rpc.api.HelloService;

/**
 * @author hk27xing
 * @Description **
 * @createTime 2021/7/12 16:00
 */
public class HelloServiceImpl2 implements HelloService {

    private static Logger logger = LoggerFactory.getLogger(HelloServiceImpl2.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到消息: {}", object);
        return "本次处理来自Socket传输";
    }
}

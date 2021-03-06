package top.lm.test;

import top.lm.rpc.api.HelloService;
import top.lm.rpc.transport.netty.server.NettyServer;

/**
 * @author hk27xing
 * @Description 测试用 netty 服务端
 * @createTime 2021/7/6 20:38
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9999);
        server.publishService(helloService, HelloService.class);
    }
}

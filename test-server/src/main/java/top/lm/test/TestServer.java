package top.lm.test;

import top.lm.rpc.api.HelloService;
import top.lm.rpc.server.RpcServer;

/**
 * @Description 服务端测试
 * @author hk27xing
 * */
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServerImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService, 9000);
    }
}

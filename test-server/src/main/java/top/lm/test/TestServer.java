package top.lm.test;

import top.lm.rpc.api.HelloService;
import top.lm.rpc.registry.DefaultServiceRegistry;
import top.lm.rpc.registry.ServiceRegistry;
import top.lm.rpc.server.RpcServer;

/**
 * @Description 服务端测试
 * @author hk27xing
 * */
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServerImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.registry(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}

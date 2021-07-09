package top.lm.test;

import top.lm.rpc.api.HelloService;
import top.lm.rpc.registry.DefaultServiceRegistry;
import top.lm.rpc.registry.ServiceRegistry;
import top.lm.rpc.serializer.HessianSerializer;
import top.lm.rpc.serializer.KryoSerializer;
import top.lm.rpc.socket.server.SocketServer;

/**
 * @Description 服务端测试
 * @author hk27xing
 * */
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService       = new HelloServerImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();

        serviceRegistry.registry(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.start(9000);
    }
}

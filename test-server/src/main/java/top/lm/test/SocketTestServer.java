package top.lm.test;

import top.lm.rpc.api.HelloService;
import top.lm.rpc.provider.ServiceProviderImpl;
import top.lm.rpc.registry.ServiceRegistry;
import top.lm.rpc.serializer.KryoSerializer;
import top.lm.rpc.transport.socket.server.SocketServer;

/**
 * @Description 服务端测试
 * @author hk27xing
 * */
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService       = new HelloServerImpl();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.publishService(helloService, HelloService.class);
    }
}

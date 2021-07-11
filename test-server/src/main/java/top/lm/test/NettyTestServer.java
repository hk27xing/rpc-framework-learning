package top.lm.test;

import top.lm.rpc.api.HelloService;
import top.lm.rpc.transport.netty.server.NettyServer;
import top.lm.rpc.provider.ServiceProviderImpl;
import top.lm.rpc.registry.ServiceRegistry;
import top.lm.rpc.serializer.ProtobufSerializer;

/**
 * @author hk27xing
 * @Description 测试用 netty 服务端
 * @createTime 2021/7/6 20:38
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServerImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9999);
        server.setSerializer(new ProtobufSerializer());
        server.publishService(helloService, HelloService.class);
    }
}

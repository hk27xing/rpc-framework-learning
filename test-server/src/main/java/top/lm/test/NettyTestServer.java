package top.lm.test;

import top.lm.rpc.api.HelloService;
import top.lm.rpc.netty.server.NettyServer;
import top.lm.rpc.registry.DefaultServiceRegistry;
import top.lm.rpc.registry.ServiceRegistry;
import top.lm.rpc.serializer.KryoSerializer;
import top.lm.rpc.serializer.ProtobufSerializer;

/**
 * @author hk27xing
 * @Description 测试用 netty 服务端
 * @createTime 2021/7/6 20:38
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServerImpl();
        ServiceRegistry registry  = new DefaultServiceRegistry();

        registry.registry(helloService);
        NettyServer server = new NettyServer();
        server.setSerializer(new ProtobufSerializer());
        server.start(9999);
    }
}

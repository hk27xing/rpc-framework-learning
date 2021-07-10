package top.lm.test;

import top.lm.rpc.RpcClient;
import top.lm.rpc.RpcClientProxy;
import top.lm.rpc.api.HelloObject;
import top.lm.rpc.api.HelloService;
import top.lm.rpc.netty.client.NettyClient;
import top.lm.rpc.serializer.KryoSerializer;
import top.lm.rpc.serializer.ProtobufSerializer;

/**
 * @author hk27xing
 * @Description 测试用 netty 客户端
 * @createTime 2021/7/6 20:40
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client              = new NettyClient("127.0.0.1", 9999);
        client.setSerializer(new ProtobufSerializer());

        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService     = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object            = new HelloObject(12, "Hello World!");
        String res                    = helloService.hello(object);

        System.out.println(res);
    }
}

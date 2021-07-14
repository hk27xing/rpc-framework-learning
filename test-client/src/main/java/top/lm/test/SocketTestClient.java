package top.lm.test;

import top.lm.rpc.serializer.CommonSerializer;
import top.lm.rpc.transport.RpcClientProxy;
import top.lm.rpc.api.HelloObject;
import top.lm.rpc.api.HelloService;
import top.lm.rpc.serializer.HessianSerializer;
import top.lm.rpc.transport.socket.client.SocketClient;

/**
 * @Description 客户端测试
 * @author hk27xing
 * */
public class SocketTestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(CommonSerializer.PROTOBUF_SERIALIZER);

        RpcClientProxy proxy      = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object        = new HelloObject(12, "Hello World!");

        for (int i = 0; i < 50; i++) {
            String res = helloService.hello(object);
            System.out.println(res);
        }

    }
}

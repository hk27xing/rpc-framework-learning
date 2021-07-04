package top.lm.test;

import top.lm.rpc.api.HelloObject;
import top.lm.rpc.api.HelloService;
import top.lm.rpc.client.RpcClientProxy;

public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "Hello World!");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}

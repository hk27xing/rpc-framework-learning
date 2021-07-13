package top.lm.test;

import top.lm.rpc.api.HelloService;
import top.lm.rpc.serializer.CommonSerializer;
import top.lm.rpc.transport.socket.server.SocketServer;

/**
 * @Description 服务端测试
 * @author hk27xing
 * */
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService       = new HelloServiceImpl2();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998, CommonSerializer.PROTOBUF_SERIALIZER);
        socketServer.publishService(helloService, HelloService.class);
    }
}

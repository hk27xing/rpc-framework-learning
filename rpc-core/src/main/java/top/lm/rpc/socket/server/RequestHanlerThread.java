package top.lm.rpc.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.RequestHandler;
import top.lm.rpc.entity.RpcRequest;
import top.lm.rpc.entity.RpcResponse;
import top.lm.rpc.registry.ServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author hk27xing
 * @Description 处理 RpcRequest 的工作线程
 * @createTime 2021/7/5 20:22
 */
public class RequestHanlerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHanlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;

    public RequestHanlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket          = socket;
        this.requestHandler  = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream   = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            String interfaceName  = rpcRequest.getInterfaceName();
            Object service        = serviceRegistry.getService(interfaceName);
            Object result         = requestHandler.handle(rpcRequest, service);

            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用或发送时有错误发生: ", e);
        }
    }
}

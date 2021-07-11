package top.lm.rpc.transport.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lm.rpc.handler.RequestHandler;
import top.lm.rpc.entity.RpcRequest;
import top.lm.rpc.entity.RpcResponse;
import top.lm.rpc.registry.ServiceRegistry;
import top.lm.rpc.serializer.CommonSerializer;
import top.lm.rpc.transport.socket.util.ObjectReader;
import top.lm.rpc.transport.socket.util.ObjectWriter;

import java.io.*;
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
    private CommonSerializer serializer;

    public RequestHanlerThread(Socket socket,
                               RequestHandler requestHandler,
                               ServiceRegistry serviceRegistry,
                               CommonSerializer serializer) {
        this.socket          = socket;
        this.requestHandler  = requestHandler;
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (InputStream inputStream  = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            String interfaceName  = rpcRequest.getInterfaceName();
            Object result         = requestHandler.handle(rpcRequest);

            RpcResponse<Object> response = RpcResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream, response, serializer);
        } catch (IOException e) {
            logger.error("调用或发送时有错误发生: ", e);
        }
    }
}

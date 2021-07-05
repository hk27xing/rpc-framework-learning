package top.lm.rpc.registry;

public interface ServiceRegistry {
    <T> void registry(T service);
    Object getService(String serviceName);
}

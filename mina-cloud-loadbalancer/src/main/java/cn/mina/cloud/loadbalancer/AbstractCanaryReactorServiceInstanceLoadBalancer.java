package cn.mina.cloud.loadbalancer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 自定义金丝雀负载均衡策略
 * 按照注册中心服务实例元数据（DEFAULT_CANARY_RULE_HEADER）路由负载
 *
 * @author Created by haoteng on 2023/3/8.
 */
public abstract class AbstractCanaryReactorServiceInstanceLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private static final Log log = LogFactory.getLog(AbstractCanaryReactorServiceInstanceLoadBalancer.class);

    protected static final String LOADBALANCER_MODE = "mina.cloud.loadbalancer.canary.mode";
    final AtomicInteger position = new AtomicInteger(0);
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    protected String serviceId;

    protected Environment environment;

    public AbstractCanaryReactorServiceInstanceLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                                                            String serviceId, Environment environment) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
        this.environment = environment;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(serviceInstances, request));
    }

    /**
     * 实现获取服务实例方法
     * @param serviceInstances
     * @param request
     * @return
     */
    public abstract Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> serviceInstances, Request request);


    /**
     * 按照配置的金丝雀模式，获取服务实例
     */
    protected Response<ServiceInstance> getServiceInstanceByCanaryMode(List<ServiceInstance> instances, List<ServiceInstance> canaryInstances) {
        // 获取金丝雀模式，默认严格模式
        String mode = environment.getProperty(LOADBALANCER_MODE, LoadBalanceMode.STRICT.toString());
        switch (LoadBalanceMode.valueOf(mode)) {
            case LOOSE:
                if (!CollectionUtils.isEmpty(canaryInstances)) {
                    return getRoundRobinInstance(canaryInstances);
                } else {
                    return getRoundRobinInstance(instances);
                }
            case STRICT:
                return getRoundRobinInstance(canaryInstances);
            default:
                log.warn("Not supported loadBalance mode :{}" + mode);
                return new EmptyResponse();
        }
    }

    /**
     * 使用RoundRobin机制获取节点
     */
    protected Response<ServiceInstance> getRoundRobinInstance(List<ServiceInstance> instances) {
        // 如果没有可用节点，则返回空
        if (instances.isEmpty()) {
            log.warn("No servers available for service: " + serviceId);
            return new EmptyResponse();
        }
        // 每一次计数器都自动+1，实现轮询的效果
        int pos = Math.abs(this.position.incrementAndGet());
        ServiceInstance instance = instances.get(pos % instances.size());
        return new DefaultResponse(instance);
    }

}

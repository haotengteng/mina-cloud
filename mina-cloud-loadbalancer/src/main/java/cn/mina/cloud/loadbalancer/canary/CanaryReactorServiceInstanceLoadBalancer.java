package cn.mina.cloud.loadbalancer.canary;

import cn.mina.cloud.common.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.mina.cloud.common.Constant.*;

/**
 * 自定义金丝雀负载均衡策略
 * 按照注册中心服务实例元数据（DEFAULT_CANARY_RULE_HEADER）路由负载
 *
 * @author Created by haoteng on 2023/3/8.
 */
public class CanaryReactorServiceInstanceLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private static final Logger log = LoggerFactory.getLogger(CanaryReactorServiceInstanceLoadBalancer.class);
    final AtomicInteger position = new AtomicInteger(0);
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    protected String serviceId;

    protected Environment environment;


    public CanaryReactorServiceInstanceLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
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
     *
     * @param serviceInstances
     * @param request
     * @return
     */
    public Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> serviceInstances, Request request) {
        // 注册中心无可用实例 返回空
        if (CollectionUtils.isEmpty(serviceInstances)) {
            log.warn("No instance available for service: {}", this.serviceId);
            return new EmptyResponse();
        }
        DefaultRequestContext context = (DefaultRequestContext) request.getContext();
        RequestData requestData = (RequestData) context.getClientRequest();
        HttpHeaders headers = requestData.getHeaders();
        // 获取到header中的流量标记
        String enable = headers.getFirst(GATEWAY_CANARY_ENABLE_HEADER);
        if (Constant.Boolean.TRUE.equals(enable)) {
            // 根据流量标记进行负载
            String canaryType = headers.getFirst(GATEWAY_CANARY_TYPE_HEADER);
            // 获取指定负载均衡类型，选择实例
            AbstractCanaryLoadBalancerRule canaryLoadBalancerRule = AbstractCanaryLoadBalancerRule.getInstances(canaryType);
            List<ServiceInstance> instanceResponse = canaryLoadBalancerRule.getInstanceResponse(serviceInstances, headers, environment);
            return getRoundRobinInstance(instanceResponse);
        } else {
            // 路由所有服务实例
            return getRoundRobinInstance(serviceInstances);
        }
    }


    /**
     * 使用RoundRobin机制获取节点
     */
    protected Response<ServiceInstance> getRoundRobinInstance(List<ServiceInstance> instances) {
        // 如果没有可用节点，则返回空
        if (instances.isEmpty()) {
            log.warn("No servers available for service: {}", serviceId);
            return new EmptyResponse();
        }
        // 每一次计数器都自动+1，实现轮询的效果
        int pos = Math.abs(position.incrementAndGet());
        ServiceInstance instance = instances.get(pos % instances.size());
        return new DefaultResponse(instance);
    }


}

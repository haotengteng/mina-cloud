package cn.mina.cloud.loadbalancer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 自定义金丝雀负载均衡策略
 * 按照注册中心服务实例元数据（DEFAULT_CANARY_RULE_HEADER）路由负载
 *
 * @author Created by haoteng on 2023/3/8.
 */
public class DefaultCanaryReactorServiceInstanceLoadBalancer extends AbstractCanaryReactorServiceInstanceLoadBalancer {
    private static final Logger log = LoggerFactory.getLogger(IPCanaryReactorServiceInstanceLoadBalancer.class);
    public static final String DEFAULT_CANARY_RULE_HEADER = "Default-Canary";

    public DefaultCanaryReactorServiceInstanceLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                                                           String serviceId,
                                                           Environment environment) {
        super(serviceInstanceListSupplierProvider, serviceId, environment);
    }


    public Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> serviceInstances, Request request) {
        return getInstanceResponse(serviceInstances, request);
    }

    // 根据金丝雀的规则返回目标节点
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, Request request) {
        // 注册中心无可用实例 返回空
        if (CollectionUtils.isEmpty(instances)) {
            log.warn("No instance available for service: {}", super.serviceId);
            return new EmptyResponse();
        }
        DefaultRequestContext context = (DefaultRequestContext) request.getContext();
        RequestData requestData = (RequestData) context.getClientRequest();
        HttpHeaders headers = requestData.getHeaders();
        // 获取到header中的流量标记
        String canary = headers.getFirst(DEFAULT_CANARY_RULE_HEADER);
        // 如果header中包含流量标记
        // 循环每个Nacos服务节点，过滤出metadata值相同的instance，再使用RoundRobin查找
        if (StringUtils.isNoneBlank(canary)) {
            List<ServiceInstance> canaryInstances = instances.stream()
                    .filter(e -> {
                        String canaryVersionInMetadata = e.getMetadata().get(DEFAULT_CANARY_RULE_HEADER);
                        return StringUtils.equalsIgnoreCase(canaryVersionInMetadata, canary);
                    }).collect(Collectors.toList());
            return getServiceInstanceByCanaryMode(instances, canaryInstances);
            // 如果没有找到打标标记，或者标记为空，则使用RoundRobin规则进行查找
        } else {
            // 过滤掉所有金丝雀测试的节点，即Nacos Metadaba中包含流量标记的节点
            // 剩余的节点中进行RoundRobin查找
            List<ServiceInstance> noneCanaryInstances = instances.stream()
                    .filter(e -> !e.getMetadata().containsKey(DEFAULT_CANARY_RULE_HEADER))
                    .collect(Collectors.toList());
            return getRoundRobinInstance(noneCanaryInstances);
        }
    }
}

package cn.mina.cloud.loadbalancer.canary;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static cn.mina.cloud.common.Constant.*;

/**
 * 自定义金丝雀负载均衡策略
 * 按照注册中心服务实例元数据（DEFAULT_CANARY_RULE_HEADER）路由负载
 *
 * @author Created by haoteng on 2023/3/8.
 */
public class MetadataCanaryLoadBalancerRule extends AbstractCanaryLoadBalancerRule {
    private static final Logger log = LoggerFactory.getLogger(MetadataCanaryLoadBalancerRule.class);


    // 根据金丝雀的规则返回目标节点

    @Override
    public List<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, HttpHeaders headers, Environment env) {
        // 获取到header中的流量标记
        String isCanary = headers.getFirst(IS_CANARY_HEADER);
        String canaryMode = headers.getFirst(GATEWAY_CANARY_MODE_HEADER);
        // 循环每个Nacos服务节点，过滤出metadata值相同的instance，再使用RoundRobin查找
        if (Is.YES.equals(isCanary)) {
            String canary = headers.getFirst(DEFAULT_CANARY_PAYLOAD_HEADER);
            List<ServiceInstance> canaryInstances = instances.stream()
                    .filter(e -> {
                        String canaryVersionInMetadata = e.getMetadata().get(DEFAULT_CANARY_PAYLOAD_HEADER);
                        return StringUtils.equalsIgnoreCase(canaryVersionInMetadata, canary);
                    }).collect(Collectors.toList());
            List<ServiceInstance> noneCanaryInstances = instances.stream()
                    .filter(e -> !e.getMetadata().containsKey(DEFAULT_CANARY_PAYLOAD_HEADER))
                    .collect(Collectors.toList());
            return getServiceInstanceByCanaryMode(noneCanaryInstances, canaryInstances, canaryMode);
            // 如果没有找到打标标记，或者标记为空，则使用RoundRobin规则进行查找
        } else {
            // 过滤掉所有金丝雀测试的节点，即Nacos Metadaba中包含流量标记的节点
            return instances.stream()
                    .filter(e -> !e.getMetadata().containsKey(DEFAULT_CANARY_PAYLOAD_HEADER))
                    .collect(Collectors.toList());
        }
    }
}

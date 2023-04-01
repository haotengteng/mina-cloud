package cn.mina.cloud.loadbalancer.canary;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequestContext;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.mina.cloud.common.Constant.*;

/**
 * 自定义金丝雀负载均衡策略
 * 按照指定服务实例ip路由负载
 *
 * @author Created by haoteng on 2023/3/10.
 */
public class HostCanaryLoadBalancerRule extends AbstractCanaryLoadBalancerRule {

    private static final Logger log = LoggerFactory.getLogger(HostCanaryLoadBalancerRule.class);


    // 根据金丝雀的规则返回目标节点
    public List<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, HttpHeaders headers, Environment env) {
        // 获取到header中的流量标记
        String isCanary = headers.getFirst(IS_CANARY_HEADER);
        if (Is.YES.equals(isCanary)) {
            String canaryMode = headers.getFirst(GATEWAY_CANARY_MODE_HEADER);
            // 灰度流量
            String canaryPayload = headers.getFirst(DEFAULT_CANARY_PAYLOAD_HEADER);
            Stream<String> canaryIps = Stream.of(canaryPayload.split(","));
            // 循环每个Nacos服务节点，过滤出指定ip的instance，再使用RoundRobin查找
            List<ServiceInstance> canaryInstances = instances.stream()
                    .filter(e -> canaryIps.anyMatch(c -> c.equals(e.getHost()))).collect(Collectors.toList());
            return getServiceInstanceByCanaryMode(instances, canaryInstances, canaryMode);
        } else {
            // 灰度模式下 正常流量
            // 过滤掉所有金丝雀测试的节点，即ip路由模式下非指定的金丝雀ip 所在的实例
            String canaryPayload = headers.getFirst(DEFAULT_CANARY_PAYLOAD_HEADER);
            if (StringUtils.isNotBlank(canaryPayload)) {
                Stream<String> canaryIps = Stream.of(canaryPayload.split(","));
                List<ServiceInstance> noneCanaryInstances = instances.stream()
                        .filter(e -> canaryIps.noneMatch(c -> c.equals(e.getHost())))
                        .collect(Collectors.toList());
                return noneCanaryInstances;
            }
            return instances;
        }
    }

}

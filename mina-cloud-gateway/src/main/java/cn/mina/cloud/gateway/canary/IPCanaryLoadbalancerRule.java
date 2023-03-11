package cn.mina.cloud.gateway.canary;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;

import static cn.mina.cloud.loadbalancer.IPCanaryReactorServiceInstanceLoadBalancer.LOADBALANCER_CANARY_IPS;

/**
 * 流量打标规则，按照Ip
 *
 * @author Created by haoteng on 2023/3/9.
 */
public class IPCanaryLoadbalancerRule implements CanaryLoadbalancerRule {

    @Override
    public String getCanary(ServerHttpRequest request, Environment environment) {
        // 打标规则实现
        String ips = environment.getProperty(LOADBALANCER_CANARY_IPS);
        if (StringUtils.isBlank(ips)) {
            // 返回空字符串 代表空的ip候选列表
            return "";
        } else {
            return ips;
        }
    }
}

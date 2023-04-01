package cn.mina.cloud.gateway.canary;

import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;

import static cn.mina.cloud.common.Constant.GATEWAY_CANARY_RULE_CONFIG_PARAM;

/**
 * 流量打标规则，按照配置，可适配Ip
 *
 * @author Created by haoteng on 2023/3/9.
 */
public abstract class AbstractConfigCanaryLoadbalancerRule implements CanaryLoadbalancerRule {

    @Override
    public CanaryRuleResult getCanary(ServerHttpRequest request, Environment environment) {
        // 打标规则实现
        String config = environment.getProperty(GATEWAY_CANARY_RULE_CONFIG_PARAM);
        return getCanaryConfig(request, config);
    }

    public abstract CanaryRuleResult getCanaryConfig(ServerHttpRequest request, String configParam);
}

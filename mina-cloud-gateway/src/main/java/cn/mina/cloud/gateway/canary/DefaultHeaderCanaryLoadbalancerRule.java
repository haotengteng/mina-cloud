package cn.mina.cloud.gateway.canary;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;

import static cn.mina.cloud.common.Constant.DEFAULT_CANARY_RULE_HEADER;

/**
 * 流量打标默认规则，按照请求头判断
 *
 * @author Created by haoteng on 2023/3/9.
 */
public class DefaultHeaderCanaryLoadbalancerRule implements CanaryLoadbalancerRule {

    @Override
    public CanaryRuleResult getCanary(ServerHttpRequest request, Environment environment) {
        CanaryRuleResult result = new CanaryRuleResult();
        // 打标规则实现
        String canary = request.getHeaders().getFirst(DEFAULT_CANARY_RULE_HEADER);
        if (StringUtils.isNoneBlank(canary)) {
            result.setIsCanary(true);
            result.setIsPenetrate(true);
            result.setPayLoad(canary);
            return result;
        } else {
            result.setIsCanary(false);
            result.setIsPenetrate(false);
            return result;
        }
    }
}

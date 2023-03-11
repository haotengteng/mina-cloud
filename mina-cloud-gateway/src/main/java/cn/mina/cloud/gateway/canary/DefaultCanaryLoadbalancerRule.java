package cn.mina.cloud.gateway.canary;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 流量打标默认规则，按照请求头判断
 *
 * @author Created by haoteng on 2023/3/9.
 */
public class DefaultCanaryLoadbalancerRule implements CanaryLoadbalancerRule {

    private static final String DEFAULT_CANARY_RULE_HEADER = "Mina-Canary";

    @Override
    public String getCanary(ServerHttpRequest request, Environment environment) {
        // 打标规则实现
        if (request.getHeaders().containsKey(DEFAULT_CANARY_RULE_HEADER)) {
            String canary = request.getHeaders().getFirst(DEFAULT_CANARY_RULE_HEADER);
            if (StringUtils.isNoneBlank(canary)) {
             return canary;
            }
        }
        // 返回null  不打标
        return null;
    }
}

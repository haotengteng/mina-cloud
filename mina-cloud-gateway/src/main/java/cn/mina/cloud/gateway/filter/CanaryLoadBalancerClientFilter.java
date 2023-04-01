package cn.mina.cloud.gateway.filter;

import cn.mina.cloud.common.canary.CanaryMode;
import cn.mina.cloud.common.Constant;
import cn.mina.cloud.gateway.canary.CanaryLoadbalancerRule;
import cn.mina.cloud.gateway.canary.CanaryRuleResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static cn.mina.cloud.common.Constant.*;

/**
 * 金丝雀负载均衡过滤器
 * <p>
 * 解析token信息，判断是否符合命中用户，进行流量打标(放入header传递)
 * 对不进行登录校验的请求 支持指定消息头打标
 */
@Slf4j
public class CanaryLoadBalancerClientFilter implements GlobalFilter, Ordered, EnvironmentAware {

    private static final int CANARY_LOADBALANCER_CLIENT_FILTER_ORDER = 1000;

    private Environment environment;


    @Autowired
    private CanaryLoadbalancerRule canaryLoadbalancerRule;

    @Override
    @SneakyThrows(Exception.class)
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 校验开启灰度情况
        String enable = environment.getProperty(GATEWAY_CANARY_ENABLE, Constant.Boolean.FALSE);
        if (Constant.Boolean.TRUE.equals(enable)) {
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = new HttpHeaders();

            // 获取灰度流量信息
            CanaryRuleResult result = canaryLoadbalancerRule.getCanary(request, environment);
            // 兼容不同协议进行流量打标
            handlerOpenFeignProtocol(headers, result);
            handlerDubboProtocol(headers, result);

            ServerHttpRequest nowRequest = request.mutate().headers(header -> header.addAll(headers)).build();
            ServerWebExchange build = exchange.mutate().request(nowRequest).build();
            return chain.filter(build);
        } else {
            // 未开启灰度情况
            ServerWebExchange build = exchange.mutate().build();
            return chain.filter(build);
        }

    }

    private void handlerOpenFeignProtocol(HttpHeaders headers, CanaryRuleResult result) {
        headers.add(GATEWAY_CANARY_ENABLE_HEADER, Constant.Boolean.TRUE);
        if (result.getIsCanary()) {
            // 流量打标
            headers.add(IS_CANARY_HEADER, Is.YES);
        }
        if (result.getIsPenetrate()) {
            // 透传参数
            headers.add(DEFAULT_CANARY_PAYLOAD_HEADER, (String) result.getPayLoad());
        }
        // 获取灰度类型，默认使用元数据方式
        String canaryType = environment.getProperty(GATEWAY_CANARY_LOADBALANCER_ENABLE, CanaryType.METADATA);
        headers.add(GATEWAY_CANARY_TYPE_HEADER, canaryType);
        // 获取灰度模式，LOOSE 或者 STRICT  默认 STRICT
        String canaryMode = environment.getProperty(GATEWAY_CANARY_LOADBALANCER_MODE, CanaryMode.STRICT.getValue());
        headers.add(GATEWAY_CANARY_MODE_HEADER, canaryMode);
    }


    private void handlerDubboProtocol(HttpHeaders headers, CanaryRuleResult result) {
        headers.add(DUBBO_CANARY_RULE_HEADER, getDubboAttachment(GATEWAY_CANARY_ENABLE_HEADER, Constant.Boolean.TRUE));
        if (result.getIsCanary()) {
            // 流量打标
            headers.add(DUBBO_CANARY_RULE_HEADER, getDubboAttachment(IS_CANARY_HEADER, Is.YES));
        }

        if (result.getIsPenetrate()) {
            // 透传参数
            headers.add(DUBBO_CANARY_RULE_HEADER, getDubboAttachment(DEFAULT_CANARY_PAYLOAD_HEADER, (String) result.getPayLoad()));
        }
        // 获取灰度类型，默认使用元数据方式
        String canaryType = environment.getProperty(GATEWAY_CANARY_LOADBALANCER_ENABLE, CanaryType.METADATA);
        headers.add(DUBBO_CANARY_RULE_HEADER, getDubboAttachment(GATEWAY_CANARY_TYPE_HEADER, canaryType));
        // 获取灰度模式，LOOSE 或者 STRICT  默认 STRICT
        String canaryMode = environment.getProperty(GATEWAY_CANARY_LOADBALANCER_MODE, CanaryMode.STRICT.getValue());
        headers.add(DUBBO_CANARY_RULE_HEADER, getDubboAttachment(GATEWAY_CANARY_MODE_HEADER, canaryMode));
    }


    private static String getDubboAttachment(String key, String value) {
        return key + "=" + value;
    }

    @Override
    public int getOrder() {
        return CANARY_LOADBALANCER_CLIENT_FILTER_ORDER;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

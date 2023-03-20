package cn.mina.cloud.gateway.filter;

import cn.mina.cloud.gateway.canary.CanaryLoadbalancerRule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import static cn.mina.cloud.loadbalancer.canary.DefaultCanaryReactorServiceInstanceLoadBalancer.DEFAULT_CANARY_RULE_HEADER;


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
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = new HttpHeaders();
        String result = canaryLoadbalancerRule.getCanary(request, environment);
        if (StringUtils.isNoneBlank(result)) {
            // 流量打标
            headers.add(DEFAULT_CANARY_RULE_HEADER, result);
        }
        ServerHttpRequest nowRequest = request.mutate().headers(header -> header.addAll(headers)).build();
        ServerWebExchange build = exchange.mutate().request(nowRequest).build();
        return chain.filter(build);

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

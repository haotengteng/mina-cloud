package cn.mina.cloud.gateway.canary;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * @author Created by haoteng on 2023/3/9.
 */
public interface CanaryLoadbalancerRule {

    String getCanary(ServerHttpRequest request, Environment environment);
}

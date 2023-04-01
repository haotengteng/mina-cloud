package cn.mina.cloud.loadbalancer.canary;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Created by haoteng on 2023/3/9.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnProperty(prefix = "mina.cloud.gateway.canary", name = "enable", havingValue = "true")
public @interface ConditionalOnLoadBalancerCanary {
}

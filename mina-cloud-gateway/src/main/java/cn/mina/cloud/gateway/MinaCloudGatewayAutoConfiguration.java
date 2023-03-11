package cn.mina.cloud.gateway;

import cn.mina.boot.support.YmlPropertySourceFactory;
import cn.mina.cloud.gateway.canary.CanaryLoadbalancerRule;
import cn.mina.cloud.gateway.canary.DefaultCanaryLoadbalancerRule;
import cn.mina.cloud.gateway.canary.IPCanaryLoadbalancerRule;
import cn.mina.cloud.gateway.filter.CanaryLoadBalancerClientFilter;
import cn.mina.cloud.gateway.filter.GatewayLogFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Created by haoteng on 2023/3/8.
 */
@Configuration
@PropertySource(value = "classpath:mina.cloud.gateway.yml", factory = YmlPropertySourceFactory.class)
public class MinaCloudGatewayAutoConfiguration {


    /**
     * 网关日志打印拦截器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mina.cloud.gateway.logger", name = "enable", havingValue = "true", matchIfMissing = true)
    public GatewayLogFilter gatewayLogFilter() {
        return new GatewayLogFilter();
    }

    /**
     * 金丝雀流量负载均衡拦截器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mina.cloud.gateway.canary", name = "enable", havingValue = "true")
    public CanaryLoadBalancerClientFilter canaryLoadBalancerClientFilter() {
        return new CanaryLoadBalancerClientFilter();
    }

    /**
     * 默认流量打标规则
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(value = CanaryLoadBalancerClientFilter.class)
    @ConditionalOnProperty(prefix = "mina.cloud.gateway.canary", name = "type", havingValue = "default", matchIfMissing = true)
    public CanaryLoadbalancerRule defaultCanaryLoadbalancerRule() {
        return new DefaultCanaryLoadbalancerRule();
    }

    /**
     * 流量按IP打标规则
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(value = CanaryLoadBalancerClientFilter.class)
    @ConditionalOnProperty(prefix = "mina.cloud.gateway.canary", name = "type", havingValue = "ip")
    public CanaryLoadbalancerRule ipCanaryLoadbalancerRule() {
        return new IPCanaryLoadbalancerRule();
    }


}

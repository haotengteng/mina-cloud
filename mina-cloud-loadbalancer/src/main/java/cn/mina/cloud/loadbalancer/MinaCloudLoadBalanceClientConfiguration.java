package cn.mina.cloud.loadbalancer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnBlockingDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnReactiveDiscoveryEnabled;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

/**
 * 自定义负载均衡策略自动配置
 *
 * @author Created by haoteng on 2023/3/9.
 */
@Import(value = {
        MinaCloudLoadBalanceClientConfiguration.ReactiveSupportConfiguration.class,
        MinaCloudLoadBalanceClientConfiguration.BlockingSupportConfiguration.class
})
public class MinaCloudLoadBalanceClientConfiguration {

    private static final int REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER = 17382746;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnLoadBalancerCanary
    @ConditionalOnProperty(prefix = "mina.cloud.loadbalancer.canary", name = "type", havingValue = "default", matchIfMissing = true)
    public ReactorLoadBalancer<ServiceInstance> defaultCanaryReactorServiceInstanceLoadBalancer(Environment environment,
                                                                                                LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new DefaultCanaryReactorServiceInstanceLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name, environment);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnLoadBalancerCanary
    @ConditionalOnProperty(prefix = "mina.cloud.loadbalancer.canary", name = "type", havingValue = "ip")
    public ReactorLoadBalancer<ServiceInstance> ipCanaryReactorServiceInstanceLoadBalancer(Environment environment,
                                                                                           LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new IPCanaryReactorServiceInstanceLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name, environment);
    }


    @Configuration(proxyBeanMethods = false)
    @ConditionalOnReactiveDiscoveryEnabled
    @ConditionalOnLoadBalancerCanary
    @Order(REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER)
    public static class ReactiveSupportConfiguration {

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "spring.cloud.loadbalancer", name = "configurations", havingValue = "default", matchIfMissing = true)
        public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withDiscoveryClient()
                    .build(context);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBlockingDiscoveryEnabled
    @ConditionalOnLoadBalancerCanary
    @Order(REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER + 1)
    public static class BlockingSupportConfiguration {

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "spring.cloud.loadbalancer", name = "configurations", havingValue = "default", matchIfMissing = true)
        public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient()
                    .build(context);
        }
    }

}

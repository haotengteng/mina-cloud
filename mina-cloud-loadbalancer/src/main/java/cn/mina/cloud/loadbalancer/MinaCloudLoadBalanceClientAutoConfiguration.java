package cn.mina.cloud.loadbalancer;

import cn.mina.boot.support.YmlPropertySourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 自定义负载均衡策略自动配置
 *
 * @author Created by haoteng on 2023/3/9.
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConditionalOnDiscoveryEnabled
@PropertySource(value = "classpath:mina.cloud.loadbalancer.yml", factory = YmlPropertySourceFactory.class)
@LoadBalancerClients(defaultConfiguration = MinaCloudLoadBalanceClientConfiguration.class)
public class MinaCloudLoadBalanceClientAutoConfiguration {

}
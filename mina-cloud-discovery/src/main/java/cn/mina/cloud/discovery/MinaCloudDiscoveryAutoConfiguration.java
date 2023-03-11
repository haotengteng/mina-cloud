package cn.mina.cloud.discovery;

import cn.mina.boot.common.util.DateUtil;
import cn.mina.boot.support.YmlPropertySourceFactory;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * @author Created by haoteng on 2023/3/11.
 */
@Configuration
@ConditionalOnDiscoveryEnabled
@PropertySource(value = "classpath:mina.cloud.discovery.yml", factory = YmlPropertySourceFactory.class)
public class MinaCloudDiscoveryAutoConfiguration {

    private static final String DISCOVERY_CANARY_TAG = "mina.cloud.discovery.canary.tag";
    public static final String DEFAULT_CANARY_RULE_HEADER = "Default-Canary";
    public static final String STARTUP_TIME = "startup.time";


    /**
     * 对nacos注册中心金丝雀实例元数据打标记
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mina.cloud.discovery.canary", name = "enable", havingValue = "true")
    public NacosDiscoveryProperties canaryDiscoveryProperties(Environment environment) {
        NacosDiscoveryProperties nacosDiscoveryProperties = new NacosDiscoveryProperties();
        String tag = environment.getProperty(DISCOVERY_CANARY_TAG, "canary");
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        metadata.put(STARTUP_TIME, DateUtil.nowStr());
        metadata.put(DEFAULT_CANARY_RULE_HEADER, tag);
        return nacosDiscoveryProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public NacosDiscoveryProperties defaultDiscoveryProperties() {
        NacosDiscoveryProperties nacosDiscoveryProperties = new NacosDiscoveryProperties();
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        metadata.put(STARTUP_TIME, DateUtil.nowStr());
        return nacosDiscoveryProperties;
    }




}

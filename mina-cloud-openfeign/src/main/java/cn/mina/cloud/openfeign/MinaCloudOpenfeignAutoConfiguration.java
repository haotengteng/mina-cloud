package cn.mina.cloud.openfeign;

import cn.mina.cloud.openfeign.canary.CanaryRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Created by haoteng on 2022/8/16.
 */
@Configuration
@Import(OpenFeignLogConfig.class)
@EnableConfigurationProperties(OpenFeignProperties.class)
public class MinaCloudOpenfeignAutoConfiguration {

    /**
     * openfeign 灰度信息透传拦截器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public CanaryRequestInterceptor canaryRequestInterceptor(){
        return new CanaryRequestInterceptor();
    }
}

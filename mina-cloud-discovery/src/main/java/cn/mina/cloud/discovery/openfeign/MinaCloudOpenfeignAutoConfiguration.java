package cn.mina.cloud.discovery.openfeign;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Created by haoteng on 2022/8/16.
 */
@Configuration
@Import(OpenFeignLogConfig.class)
@EnableConfigurationProperties(OpenFeignProperties.class)
public class MinaCloudOpenfeignAutoConfiguration {
}

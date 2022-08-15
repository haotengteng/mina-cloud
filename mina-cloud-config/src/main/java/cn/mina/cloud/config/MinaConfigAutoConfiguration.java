package cn.mina.cloud.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Created by haoteng on 2022/7/26.
 */

@Configuration
@ConditionalOnClass(ApplicationContext.class)
public class MinaConfigAutoConfiguration {

    @Bean
    public MinaConfigContext minaConfigContext() {
        return new MinaConfigContext();
    }
}

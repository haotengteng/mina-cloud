package cn.mina.cloud.sentinel.client;

import cn.mina.boot.support.YmlPropertySourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * sentinel 自动配置加载
 * sentinel 默认的客户端参数必须有值
 *
 * @author Created by haoteng on 2023/3/15.
 */
@Configuration
@PropertySource(value = "classpath:mina.cloud.sentinel.client.yml", factory = YmlPropertySourceFactory.class)

public class MinaCloudSentinelAutoConfiguration {

    /**
     * springcloud alibaba sentinel client异常统一处理
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientBlockExceptionHandler clientBlockExceptionHandler() {
        return new ClientBlockExceptionHandler();
    }

}


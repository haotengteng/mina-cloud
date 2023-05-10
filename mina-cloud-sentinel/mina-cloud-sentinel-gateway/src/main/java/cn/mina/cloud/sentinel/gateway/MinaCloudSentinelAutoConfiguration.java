package cn.mina.cloud.sentinel.gateway;

import cn.mina.boot.support.YmlPropertySourceFactory;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@PropertySource(value = "classpath:mina-cloud-sentinel-gateway.yml", factory = YmlPropertySourceFactory.class)
public class MinaCloudSentinelAutoConfiguration {


    /**
     * springcloud alibaba gateway sentinel 异常统一处理
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({
            com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler.class,
            org.springframework.web.reactive.function.server.ServerResponse.class})
    public BlockRequestHandler sentinelExceptionHandler() {
        return new SentinelExceptionHandler();
    }
}


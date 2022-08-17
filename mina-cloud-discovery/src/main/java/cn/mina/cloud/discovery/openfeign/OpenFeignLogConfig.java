package cn.mina.cloud.discovery.openfeign;

import feign.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author Created by haoteng on 2022/8/16.
 */
public class OpenFeignLogConfig {


    @Autowired
    private OpenFeignProperties openFeignProperties;

    /**
     * NONE： 默认的，不显示任何日志
     * <p>
     * BASIC： 仅记录请求方法、URL、响应状态码以及执行时间
     * <p>
     * HEADERS：除了BASIC 中自定义的信息外，还有请求和响应的信息头
     * <p>
     * FULL： 除了HEADERS中定义的信息外， 还有请求和响应的正文以及元数据。
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "mina.cloud.openfeign.log", name = "enable", havingValue = "true", matchIfMissing = false)
    Logger.Level feignLoggerLeave() {
        switch (openFeignProperties.getLevel()) {
            case "NONE":
                return Logger.Level.NONE;
            case "BASIC":
                return Logger.Level.BASIC;
            case "HEADERS":
                return Logger.Level.HEADERS;
            case "FULL":
                return Logger.Level.FULL;
            default:
                // 默认BASIC等级
                return Logger.Level.BASIC;
        }
    }
}

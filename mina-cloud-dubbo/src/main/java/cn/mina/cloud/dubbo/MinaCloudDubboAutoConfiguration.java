package cn.mina.cloud.dubbo;

import cn.mina.boot.support.YmlPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Created by haoteng on 2023/3/11.
 */
@Configuration
@PropertySource(value = "classpath:mina.cloud.dubbo.yml", factory = YmlPropertySourceFactory.class)
public class MinaCloudDubboAutoConfiguration {

}

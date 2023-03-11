package cn.mina.cloud.config;

import cn.mina.boot.MinaBootApplication;
import cn.mina.cloud.config.controller.ExampleConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExampleConfig.class)
public class MinaCloudExampleConfigApplication {

    public static void main(String[] args) {
        MinaBootApplication.run(MinaCloudExampleConfigApplication.class, args);
    }

}

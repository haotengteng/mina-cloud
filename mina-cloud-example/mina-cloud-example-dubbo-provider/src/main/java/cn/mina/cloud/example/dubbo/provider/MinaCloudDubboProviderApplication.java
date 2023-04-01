package cn.mina.cloud.example.dubbo.provider;

import cn.mina.boot.MinaBootApplication;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class MinaCloudDubboProviderApplication {

    public static void main(String[] args) throws InterruptedException {
        MinaBootApplication.run(MinaCloudDubboProviderApplication.class, args);
    }
}

package cn.mina.cloud.example.dubbo.consumer;

import cn.mina.boot.MinaBootApplication;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class MinaCloudDubboConsumerApplication {

    public static void main(String[] args) throws InterruptedException {
        MinaBootApplication.run(MinaCloudDubboConsumerApplication.class, args);
//        new CountDownLatch(1).await();
    }

}

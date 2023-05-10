package cn.mina.cloud.config;

import cn.mina.boot.MinaApplication;
import cn.mina.cloud.config.controller.ExampleConfig;
import cn.mina.cloud.config.controller.FooController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableConfigurationProperties(ExampleConfig.class)
public class MinaCloudExampleConfigApplication implements CommandLineRunner {

    public static void main(String[] args) {
        MinaApplication.run(MinaCloudExampleConfigApplication.class, args);
    }

    @Autowired
    private FooController fooController;

    @Override
    public void run(String... args) throws Exception {
        new Thread(()->{
            try {
                while (true){
                    TimeUnit.SECONDS.sleep(3);
                    fooController.sayHello();
                }
            } catch (UnknownHostException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}

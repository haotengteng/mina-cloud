package cn.mina.cloud.example.gateway;

import cn.mina.boot.MinaBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MinaCloudExampleGatewayApplication {

    public static void main(String[] args) {
        MinaBootApplication.run(MinaCloudExampleGatewayApplication.class, args);
    }

}
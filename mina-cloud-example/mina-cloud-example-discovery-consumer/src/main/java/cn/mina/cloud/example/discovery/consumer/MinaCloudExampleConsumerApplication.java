package cn.mina.cloud.example.discovery.consumer;

import cn.mina.boot.MinaBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MinaCloudExampleConsumerApplication {

	public static void main(String[] args) {
		MinaBootApplication.run(MinaCloudExampleConsumerApplication.class, args);
	}

}

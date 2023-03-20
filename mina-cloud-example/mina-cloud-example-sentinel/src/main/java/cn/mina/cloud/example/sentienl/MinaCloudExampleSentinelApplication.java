package cn.mina.cloud.example.sentienl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MinaCloudExampleSentinelApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinaCloudExampleSentinelApplication.class, args);
	}

}

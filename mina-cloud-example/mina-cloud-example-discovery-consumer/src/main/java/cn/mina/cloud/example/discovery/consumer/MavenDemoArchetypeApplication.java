package cn.mina.cloud.example.discovery.consumer;

import cn.mina.boot.context.MinaBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MavenDemoArchetypeApplication {

	public static void main(String[] args) {
		MinaBootApplication.run(MavenDemoArchetypeApplication.class, args);
	}

}

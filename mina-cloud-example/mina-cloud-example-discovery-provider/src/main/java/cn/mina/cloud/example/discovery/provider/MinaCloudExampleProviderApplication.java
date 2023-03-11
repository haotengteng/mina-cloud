package cn.mina.cloud.example.discovery.provider;

import cn.mina.boot.MinaBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MinaCloudExampleProviderApplication {

	public static void main(String[] args) {
		MinaBootApplication.run(MinaCloudExampleProviderApplication.class, args);
	}

}

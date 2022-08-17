package cn.mina.cloud.example.discovery.provider;

import cn.mina.boot.context.MinaBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MavenDemoArchetypeApplication {

	public static void main(String[] args) {
		MinaBootApplication.run(MavenDemoArchetypeApplication.class, args);
	}

}

package cn.mina.cloud.config;

import cn.mina.boot.context.MinaBootApplication;
import cn.mina.cloud.config.controller.ExampleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExampleConfig.class)
public class MavenDemoArchetypeApplication {

	public static void main(String[] args) {
		MinaBootApplication.run(MavenDemoArchetypeApplication.class, args);
	}

}

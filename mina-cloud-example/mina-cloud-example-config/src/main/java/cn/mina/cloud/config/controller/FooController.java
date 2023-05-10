package cn.mina.cloud.config.controller;

import cn.mina.cloud.config.MinaCloudConfigContext;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Component
@RequestMapping("/foo")
@RefreshScope
public class FooController{

    Logger log = LoggerFactory.getLogger(FooController.class);

    /**
     * @RefreshScope 注解使用注意：改注解时基于动态代理实现，在使用动态刷新的时候需要通过添加了该注解的单例bean访问调用获取，
     * 不能在该bean内部直接获取
     */
    @Value("${mina.user.name}")
    private String name;

    @Value("${mina.user.address}")
    private String address;

    @Autowired
    private  ExampleConfig exampleConfig;

    @Autowired
    private MinaCloudConfigContext minaCloudConfigContext;

    public void sayHello() throws UnknownHostException {
        log.info("nacos-config 监听到配置信息为:{}", name);
//        log.info("nacos-config 监听到配置信息为:{}", address);
        log.info("exampleConfig监听到配置信息为:{}", exampleConfig.getName());
//        log.info("minaCloudConfigContext监听到配置信息为:{}", minaCloudConfigContext.getProperty("mina.user.name"));
    }



}

package cn.mina.cloud.config.controller;

import cn.mina.cloud.config.MinaCloudConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/foo")
@RefreshScope
public class FooController {

    Logger log = LoggerFactory.getLogger(FooController.class);
    @Value("${user.name:mi}")
    private String name;

    @Value("${user.address:hangzhou}")
    private String address;

    @Autowired
    private  ExampleConfig exampleConfig;

    @Autowired
    private MinaCloudConfigContext minaCloudConfigContext;

    @GetMapping(path = "hello")
    public String sayHello() throws UnknownHostException {
        log.info("nacos-config 监听到配置信息为:{}", name);
        log.info("nacos-config 监听到配置信息为1:{}", exampleConfig.getName());
        log.info("nacos-config 监听到配置信息为:{}", address);
        log.info("nacos-config 监听到配置信息为:{}", minaCloudConfigContext.getProperty("user.name"));

        String ip = getLocalIP();
        return ip + ": Hello docker!";
    }

    private String getLocalIP() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return addr.getHostName() + "-" + addr.getHostAddress();
    }
}

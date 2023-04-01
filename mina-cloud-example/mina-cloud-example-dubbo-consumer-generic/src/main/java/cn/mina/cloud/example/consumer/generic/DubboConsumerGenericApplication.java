package cn.mina.cloud.example.consumer.generic;

import cn.mina.boot.common.util.JsonUtil;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericService;
import org.apache.dubbo.spring.boot.autoconfigure.DubboConfigurationProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableDubbo
@RestController
@RequestMapping("foo")
public class DubboConsumerGenericApplication implements ApplicationContextAware {

    private static ApplicationContext app;

    @Autowired
    private DubboConfigurationProperties properties;

    @RequestMapping("hello")
    public String hello() {

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>(); // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
        reference.setInterface("cn.mina.cloud.example.dubbo.api.MinaDubboDemoService1"); // 弱类型接口名
        reference.setGeneric("true"); // 声明为泛化接口
        reference.setCheck(false);
        reference.setApplication(properties.getApplication());
        reference.setRegistry(properties.getRegistry());
        RpcContext.getContext().setAttachment("generic","gson");
        GenericService genericService = reference.get(); // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用

        // 基本类型以及Date,List,Map等不需要转换，直接调用
        Object result = genericService.$invoke("foo",
                new String[]{"java.lang.String"},
                new Object[]{"ping"});
        return JsonUtil.toJSONString(result);
    }

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerGenericApplication.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        app = applicationContext;
    }
}

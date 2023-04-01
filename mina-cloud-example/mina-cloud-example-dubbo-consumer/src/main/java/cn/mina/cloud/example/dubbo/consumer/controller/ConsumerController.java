package cn.mina.cloud.example.dubbo.consumer.controller;

import cn.mina.cloud.example.dubbo.api.MinaDubboDemoService1;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by haoteng on 2022/11/21.
 */
@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @DubboReference(providedBy = "mina-cloud-dubbo-provider")
    private MinaDubboDemoService1 service1;

    @GetMapping("foo")
    private String foo() {
        System.out.println("=========consumer===========");
        return service1.foo("foo");
    }
}

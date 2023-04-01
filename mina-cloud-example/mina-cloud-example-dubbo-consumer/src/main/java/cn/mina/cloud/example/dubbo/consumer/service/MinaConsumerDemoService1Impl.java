package cn.mina.cloud.example.dubbo.consumer.service;

import cn.mina.cloud.example.dubbo.api.MinaDubboDemoService1;
import cn.mina.cloud.example.dubbo.api.MinaDubboDemoService2;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Created by haoteng on 2023/3/26.
 */
@DubboService
@Path("/foo")
public class MinaConsumerDemoService1Impl implements MinaConsumerDemoService1 {

    @DubboReference(providedBy = "mina-cloud-dubbo-provider")
    private MinaDubboDemoService1 service1;
    @DubboReference(providedBy = "mina-cloud-dubbo-provider")
    private MinaDubboDemoService2 service2;

    @Override
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String foo(String foo) {
        System.out.println(service1.foo(foo));
        System.out.println(service2.foo(foo));
        return "111";
    }
}

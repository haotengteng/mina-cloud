package cn.mina.cloud.example.dubbo.consumer.service;

import cn.mina.cloud.example.dubbo.api.MinaDubboDemoService1;
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
public class MinaConsumerDemoService2Impl implements MinaConsumerDemoService2 {

    @DubboReference(providedBy = "mina-cloud-dubbo-provider")
    private MinaDubboDemoService1 service1;

    @Override
    @Path("/ping2")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String foo(String foo) {
        return service1.foo(foo);
    }
}

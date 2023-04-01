package cn.mina.cloud.example.dubbo.provider.service;

import cn.mina.cloud.example.dubbo.api.MinaDubboDemoService2;
import org.apache.dubbo.config.annotation.DubboService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Created by haoteng on 2022/11/21.
 */
@DubboService
public class MinaDubboDemoService2Impl implements MinaDubboDemoService2 {
    @Override
    public String foo(String ping) {
        return "foo";
    }

    @Override
    public Object hello() {
        return "hello";
    }
}

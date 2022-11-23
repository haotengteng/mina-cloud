package cn.mina.cloud.example.dubbo.provider.service;

import cn.mina.cloud.example.dubbo.api.MinaDubboDemoService1;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Created by haoteng on 2022/11/21.
 */
@DubboService
public class MinaDubboDemoService1Impl implements MinaDubboDemoService1 {
    @Override
    public String foo(String ping) {
        return "foo111";
    }

    @Override
    public Object hello() {
        return "hello";
    }
}

package cn.mina.cloud.example.discovery.consumer.controller;

import cn.mina.boot.common.util.JsonUtil;
import cn.mina.boot.web.common.context.ClientResult;
import cn.mina.boot.web.common.context.MinaWebTools;
import cn.mina.cloud.example.discovery.consumer.service.FooService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by haoteng on 2022/8/15.
 */
@RestController
@RequestMapping(path = "foo")
@Slf4j
public class FooController implements EnvironmentAware {

    @Autowired
    private FooService fooService;



    @GetMapping("ping")
    public ClientResult<String> test() {
        ClientResult<String> pong = fooService.getFoo("ping");
        log.info(JsonUtil.toJSONString(pong));
        return MinaWebTools.response.success();
    }



    @GetMapping("fallback")
    public ClientResult<String> fallback() {
        ClientResult<String> pong = fooService.fallback("ping");
        log.info(JsonUtil.toJSONString(pong));
        return MinaWebTools.response.success();
    }


    @Override
    public void setEnvironment(Environment environment) {
//        environment.
    }
}

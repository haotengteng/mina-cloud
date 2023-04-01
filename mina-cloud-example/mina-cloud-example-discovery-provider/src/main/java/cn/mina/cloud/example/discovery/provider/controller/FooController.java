package cn.mina.cloud.example.discovery.provider.controller;

import cn.mina.boot.web.common.context.MinaWebResult;
import cn.mina.boot.web.common.context.MinaWebTools;
import cn.mina.boot.web.common.exception.MinaGlobalException;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by haoteng on 2022/8/15.
 */
@RestController
@RequestMapping(path = "foo")
public class FooController {

    @GetMapping("ping")
    public MinaWebResult<String> getFoo(@RequestParam String ping) {
//        throw new MinaGlobalException("ceshi");
//        TimeUnit.SECONDS.sleep(1);
        return MinaWebTools.response.success("pong");
    }

    @SneakyThrows
    @GetMapping("fallback")
    public MinaWebResult<String> testFallback(String ping) {
//        ThreadUtils.sleep(Duration.ofMinutes(3));
        return MinaWebTools.response.success("pong");
    }
}

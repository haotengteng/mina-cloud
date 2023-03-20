package cn.mina.cloud.example.sentienl.service;

import cn.mina.boot.web.common.context.MinaWebResult;

import cn.mina.cloud.example.sentienl.service.fallback.FooServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Created by haoteng on 2022/8/15.
 */
//@FeignClient(name = "mina-provider", fallback = FooServiceFallback.class)
@FeignClient(name = "mina-provider")
public interface FooService {
    @GetMapping("foo/ping")
    MinaWebResult<String> getFoo(@RequestParam("ping") String ping);


    @GetMapping("foo/fallback")
    MinaWebResult<String> fallback(@RequestParam("ping") String ping);
}

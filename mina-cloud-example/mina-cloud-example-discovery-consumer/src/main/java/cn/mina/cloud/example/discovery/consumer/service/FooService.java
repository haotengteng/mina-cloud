package cn.mina.cloud.example.discovery.consumer.service;

import cn.mina.boot.web.common.context.ClientResult;
import cn.mina.boot.web.common.context.MinaWebTools;
import cn.mina.cloud.example.discovery.consumer.service.fallback.FooServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Created by haoteng on 2022/8/15.
 */
@FeignClient(name = "mina-provider", fallback = FooServiceFallback.class)
public interface FooService {
    @GetMapping("foo/ping")
    ClientResult<String> getFoo(@RequestParam("ping") String ping);


    @GetMapping("foo/fallback")
    ClientResult<String> fallback(@RequestParam("ping") String ping);
}

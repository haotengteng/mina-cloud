package cn.mina.cloud.example.discovery.consumer.service.fallback;

import cn.mina.boot.web.common.context.MinaWebResult;
import cn.mina.boot.web.common.context.MinaWebTools;
import cn.mina.cloud.example.discovery.consumer.service.FooService;
import org.springframework.stereotype.Service;

/**
 * @author Created by haoteng on 2022/8/15.
 */
@Service
public class FooServiceFallback implements FooService {
    @Override
    public MinaWebResult<String> getFoo(String ping) {
        return MinaWebTools.response.about("fallback service");
    }

    @Override
    public MinaWebResult<String> fallback(String ping) {
        return MinaWebTools.response.about("fallback service111");
    }
}

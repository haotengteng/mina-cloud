package cn.mina.cloud.sentinel.gateway;

import cn.mina.boot.common.util.JsonUtil;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * sentinel 异常网关模块全局处理
 *
 * @author Created by haoteng on 2023/3/17.
 */
public class SentinelExceptionHandler implements BlockRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(SentinelExceptionHandler.class);

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
        ObjectNode resultObj = JsonUtil.getObjectNode();
        String resourceName = ((BlockException) throwable).getRule().getResource();
        if (throwable instanceof FlowException) {
            logger.error("请求触发限流设置，该请求已拦截：{}", resourceName);
            resultObj.put("code", RatelimitErrorCode.ERROR_RATELIMIT_BLOCK_ERROR.getCode());
            resultObj.put("message", RatelimitErrorCode.ERROR_RATELIMIT_BLOCK_ERROR.getMessage());
        }
        if (throwable instanceof DegradeException) {
            logger.error("请求触发降级设置，该请求已降级处理：{}", resourceName);
            resultObj.put("code", RatelimitErrorCode.ERROR_RATELIMIT_DEGRADE_ERROR.getCode());
            resultObj.put("message", RatelimitErrorCode.ERROR_RATELIMIT_DEGRADE_ERROR.getMessage());
        } else {
            logger.error("请求触发sentinel拦截，该请求拦截：{}", resourceName);
            resultObj.put("code", RatelimitErrorCode.ERROR_RATELIMIT_BLOCK_ERROR.getCode());
            resultObj.put("message", RatelimitErrorCode.ERROR_RATELIMIT_BLOCK_ERROR.getMessage());
        }
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(resultObj));
    }

}

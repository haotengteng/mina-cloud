package cn.mina.cloud.sentinel.gateway;

import cn.mina.boot.common.util.JsonUtil;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.node.ObjectNode;
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


    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
        ObjectNode resultObj = JsonUtil.getObjectNode();
        if (throwable instanceof FlowException) {
            resultObj.put("code", 700000);
            resultObj.put("message", "系统繁忙，请稍后再试");
        }
        if (throwable instanceof DegradeException) {
            resultObj.put("code", 710000);
            resultObj.put("message", "系统繁忙，请稍后再试");
        }
        if (throwable instanceof ParamFlowException) {
            resultObj.put("code", 720000);
            resultObj.put("message", "系统繁忙，请稍后再试");
        }
        if (throwable instanceof SystemBlockException) {
            resultObj.put("code", 730000);
            resultObj.put("message", "系统繁忙，请稍后再试");
        }
        if (throwable instanceof AuthorityException) {
            resultObj.put("code", 740000);
            resultObj.put("message", "系统繁忙，请稍后再试");
        }
        return ServerResponse.status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(resultObj));
    }

}

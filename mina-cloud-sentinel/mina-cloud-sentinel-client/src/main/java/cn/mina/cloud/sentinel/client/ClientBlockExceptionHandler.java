package cn.mina.cloud.sentinel.client;

import cn.mina.boot.common.exception.MinaBaseException;
import cn.mina.boot.common.util.JsonUtil;
import cn.mina.boot.ratelimit.sentinel.exception.RatelimitErrorCode;
import cn.mina.boot.web.common.exception.GlobalErrorCode;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 针对 sentinel feign没有指定fallback方法时，抛出BlockException报错处理
 * 设置优先级高于 web通用异常处理类WebExceptionHandler
 *
 * @author Created by haoteng on 2023/3/20.
 */
@Order(-1)
public class ClientBlockExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(ClientBlockExceptionHandler.class);

    @SneakyThrows(IOException.class)
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ObjectNode result = JsonUtil.getObjectNode();
        ex.printStackTrace();
        Throwable cause = ex.getCause();
        if (cause instanceof FlowException) {
            String resourceName = ((BlockException) cause).getRule().getResource();
            logger.error("请求触发限流设置，该请求已拦截：{}", resourceName);
            result.put("code", RatelimitErrorCode.ERROR_RATELIMIT_BLOCK_ERROR.getCode());
            result.put("message", RatelimitErrorCode.ERROR_RATELIMIT_BLOCK_ERROR.getMessage());
        } else if (cause instanceof DegradeException) {
            String resourceName = ((BlockException) cause).getRule().getResource();
            logger.error("请求触发降级设置，该请求已降级处理：{}", resourceName);
            result.put("code", RatelimitErrorCode.ERROR_RATELIMIT_DEGRADE_ERROR.getCode());
            result.put("message", RatelimitErrorCode.ERROR_RATELIMIT_DEGRADE_ERROR.getMessage());
        } else if (cause instanceof BlockException) {
            String resourceName = ((BlockException) cause).getRule().getResource();
            logger.error("请求触发sentinel拦截，该请求拦截：{}", resourceName);
            result.put("code", GlobalErrorCode.ERROR_SYS_ERROR.getCode());
            result.put("message", GlobalErrorCode.ERROR_SYS_ERROR.getMessage());
        } else if (ex instanceof MinaBaseException) {
            MinaBaseException baseException = (MinaBaseException) ex;
            result.put("code", baseException.getCode());
            result.put("message", baseException.getMessage());
        } else {
            result.put("code", GlobalErrorCode.ERROR_SYS_ERROR.getCode());
            result.put("message", GlobalErrorCode.ERROR_SYS_ERROR.getMessage());
        }
        response.addHeader("Content-Type", "application/json; charset=UTF-8");
        response.getWriter().append(JsonUtil.toJSONString(result)).flush();
        return new ModelAndView();
    }
}

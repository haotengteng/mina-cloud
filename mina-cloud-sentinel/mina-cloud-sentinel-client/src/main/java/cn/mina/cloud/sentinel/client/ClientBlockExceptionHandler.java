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
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 针对 sentinel feign没有指定fallback方法时，抛出BlockException报错处理
 *  设置优先级高于 web通用异常处理类WebExceptionHandler
 * @author Created by haoteng on 2022/7/19.
 */
@Order(-1)
public class ClientBlockExceptionHandler implements HandlerExceptionResolver {

    @SneakyThrows(IOException.class)
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ObjectNode result = JsonUtil.getObjectNode();
        ex.printStackTrace();
        Throwable cause = ex.getCause();
        if (cause instanceof FlowException) {
            result.put("code", RatelimitErrorCode.ERROR_RATELIMIT_BLOCK_ERROR.getCode());
            result.put("message", RatelimitErrorCode.ERROR_RATELIMIT_BLOCK_ERROR.getMessage());
        } else if (cause instanceof DegradeException) {
            result.put("code", RatelimitErrorCode.ERROR_RATELIMIT_DEGRADE_ERROR.getCode());
            result.put("message", RatelimitErrorCode.ERROR_RATELIMIT_DEGRADE_ERROR.getMessage());
        } else if (cause instanceof BlockException) {
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

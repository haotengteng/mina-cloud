package cn.mina.cloud.openfeign.canary;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static cn.mina.cloud.common.Constant.*;

/**
 * openfeign 透传请求头参数
 *
 * @author Created by haoteng on 2023/3/26.
 */
public class CanaryRequestInterceptor implements RequestInterceptor {
    private static final Set<String> CANARY_HEADERS = new HashSet<>();

    static {
        CANARY_HEADERS.add(DEFAULT_CANARY_PAYLOAD_HEADER);
        CANARY_HEADERS.add(GATEWAY_CANARY_ENABLE_HEADER);
        CANARY_HEADERS.add(GATEWAY_CANARY_TYPE_HEADER);
        CANARY_HEADERS.add(GATEWAY_CANARY_MODE_HEADER);
        CANARY_HEADERS.add(IS_CANARY_HEADER);
    }

    @Override
    public void apply(RequestTemplate template) {
        Map<String, String> headers = getHeaders();
        headers.forEach(template::header);
    }

    /**
     * 获取 request 中的所有的 header 值
     *
     * @return
     */
    private Map<String, String> getHeaders() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            if (CANARY_HEADERS.contains(key)) {
                String value = request.getHeader(key);
                map.put(key, value);
            }
        }
        return map;
    }

}

package cn.mina.cloud.gateway.canary;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Arrays;

/**
 * 流量打标规则，按照配置
 * host 规则
 *
 * @author Created by haoteng on 2023/3/9.
 */
public class DefaultHostCanaryLoadbalancerRule extends AbstractConfigCanaryLoadbalancerRule {

    @Override
    public CanaryRuleResult getCanaryConfig(ServerHttpRequest request, String configParam) {
        // 打标规则实现
        CanaryRuleResult result = new CanaryRuleResult();

        if (isCanary(request, configParam)) {
            result.setIsCanary(true);
            result.setIsPenetrate(true);
            result.setPayLoad(configParam);
            return result;
        } else {
            result.setIsCanary(false);
            // 即使是非灰度流量也要返回配置的金丝雀ip ,适应场景 :灰度模式下的非灰度流量的正常路由分配
            result.setIsPenetrate(true);
            result.setPayLoad(configParam);
            return result;
        }
    }

    /**
     * 根据request对象获取ip，判断是否是灰度流量
     * @param request
     * @param configParam
     * @return
     */
    public Boolean isCanary(ServerHttpRequest request, String configParam) {
        String ipAddress = getIpAddress(request);
        if (!StringUtils.isAnyBlank(configParam, ipAddress)) {
            return Arrays.asList(configParam.split(",")).contains(ipAddress.trim());
        } else {
            return false;
        }
    }

    private static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }

}

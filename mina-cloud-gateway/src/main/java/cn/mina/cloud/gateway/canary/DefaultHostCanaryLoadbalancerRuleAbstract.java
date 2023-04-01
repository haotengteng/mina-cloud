package cn.mina.cloud.gateway.canary;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 流量打标规则，按照配置
 * host 规则
 *
 * @author Created by haoteng on 2023/3/9.
 */
public class DefaultHostCanaryLoadbalancerRuleAbstract extends AbstractConfigCanaryLoadbalancerRule {

    @Override
    public CanaryRuleResult getCanaryConfig(ServerHttpRequest request, String configParam) {
        // 打标规则实现
        CanaryRuleResult result = new CanaryRuleResult();

        if (isCanary()) {
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
     * 默认host规则下，所有流量均为灰度流量，（可自行覆盖重写该方法）
     *
     * @return
     */
    public Boolean isCanary() {
        return true;
    }

}

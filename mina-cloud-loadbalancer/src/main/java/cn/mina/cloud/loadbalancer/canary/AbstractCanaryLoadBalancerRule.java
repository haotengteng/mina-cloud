package cn.mina.cloud.loadbalancer.canary;

import cn.mina.boot.web.common.exception.MinaGlobalException;
import cn.mina.cloud.common.canary.CanaryMode;
import cn.mina.cloud.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义金丝雀负载均衡策略
 * 按照注册中心服务实例元数据（DEFAULT_CANARY_RULE_HEADER）路由负载
 *
 * @author Created by haoteng on 2023/3/8.
 */
public abstract class AbstractCanaryLoadBalancerRule {

    private static final Logger log = LoggerFactory.getLogger(AbstractCanaryLoadBalancerRule.class);

    private static final ConcurrentHashMap<String, AbstractCanaryLoadBalancerRule> loadBalancerRules = new ConcurrentHashMap<>(2);


    protected static AbstractCanaryLoadBalancerRule getInstances(String canaryType) {
        AbstractCanaryLoadBalancerRule balancerRule = loadBalancerRules.get(canaryType);
        if (balancerRule == null) {
            synchronized (loadBalancerRules) {
                // DDL 校验
                AbstractCanaryLoadBalancerRule rule = loadBalancerRules.get(canaryType);
                if (null == rule) {
                    switch (canaryType) {
                        case Constant.CanaryType.METADATA:
                            loadBalancerRules.put(canaryType, new MetadataCanaryLoadBalancerRule());
                            break;
                        case Constant.CanaryType.HOST:
                            loadBalancerRules.put(canaryType, new HostCanaryLoadBalancerRule());
                            break;
                        default:
                            log.warn("不支持的负载均衡策略:{}", canaryType);
                            throw new MinaGlobalException("灰度负载均衡类型配置有误");
                    }
                    return loadBalancerRules.get(canaryType);
                } else {
                    return rule;
                }
            }
        } else {
            return balancerRule;
        }
    }

    /**
     * 实现获取服务实例方法
     *
     * @param serviceInstances
     * @param request
     * @return
     */
    public abstract List<ServiceInstance> getInstanceResponse(List<ServiceInstance> serviceInstances, HttpHeaders headers, Environment env);


    /**
     * 按照配置的金丝雀模式，获取服务实例
     */
    protected List<ServiceInstance> getServiceInstanceByCanaryMode(List<ServiceInstance> instances,
                                                                   List<ServiceInstance> canaryInstances,
                                                                   String mode) {
        switch (CanaryMode.valueOf(mode)) {
            case LOOSE:
                if (!CollectionUtils.isEmpty(canaryInstances)) {
                    return canaryInstances;
                } else {
                    return instances;
                }
            case STRICT:
                return canaryInstances;
            default:
                log.warn("Not supported loadBalance mode :{}", mode);
                return new ArrayList<>();
        }
    }

}

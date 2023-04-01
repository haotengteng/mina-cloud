package cn.mina.cloud.dubbo.discovery;

import cn.mina.boot.common.util.DateUtil;
import cn.mina.cloud.common.Constant;
import org.apache.dubbo.registry.client.ServiceInstance;
import org.apache.dubbo.registry.client.ServiceInstanceCustomizer;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static cn.mina.cloud.common.Constant.*;

/**
 * dubbo服务实例元数据自定义扩展
 *
 * @author Created by haoteng on 2023/3/23.
 */
public class MinaCloudDubboServiceInstanceCustomizer implements ServiceInstanceCustomizer, EnvironmentAware {


    private Environment env;

    /**
     * 对nacos注册中心金丝雀实例元数据打标记
     */
    @Override
    public void customize(ServiceInstance serviceInstance, ApplicationModel applicationModel) {
        Map<String, String> metadata = new HashMap<>();
        String canary = env.getProperty(DISCOVERY_CANARY_ENABLE);
        if (Constant.Boolean.TRUE.equals(canary)){
            String tag = env.getProperty(DISCOVERY_CANARY_TAG, "canary");
            metadata.put(DEFAULT_CANARY_PAYLOAD_HEADER, tag);
        }
        metadata.put(STARTUP_TIME, DateUtil.nowStr());
        serviceInstance.getMetadata().putAll(metadata);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}

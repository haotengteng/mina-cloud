package cn.mina.cloud.gateway.canary;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Created by haoteng on 2023/3/29.
 */
@Data
public class CanaryRuleResult implements Serializable {

    private static final long serialVersionUID = 156018979736242774L;

    /**
     * 灰度流量标记
     */
    private Boolean isCanary;

    /**
     * 灰度流量传递数据
     */
    private Object payLoad;

    /**
     * 非灰度流量是否透传payLoad
     */
    private Boolean isPenetrate;
}

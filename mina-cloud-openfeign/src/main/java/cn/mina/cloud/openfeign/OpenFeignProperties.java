package cn.mina.cloud.openfeign;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Created by haoteng on 2022/8/16.
 */
@ConfigurationProperties(prefix = "mina.cloud.openfeign.log")
public class OpenFeignProperties {

    /**
     * 是否启用
     */
    private boolean enable;
    /**
     * 日志等级
     */
    private String level;


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

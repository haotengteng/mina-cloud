package cn.mina.cloud.common.canary;

/**
 * @author Created by haoteng on 2023/3/28.
 */
public enum CanaryType {
    /**
     * 默认模式，头信息传递，dubbo 使用隐式传参
     */
    DEFAULT("DEFAULT"),
    /**
     * 指定host模式
     */
    IP("IP");

    private String value;

    CanaryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

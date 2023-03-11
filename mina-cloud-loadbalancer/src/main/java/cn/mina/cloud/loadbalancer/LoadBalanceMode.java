package cn.mina.cloud.loadbalancer;

/**
 *
 /**
 * 负载模式
 * STRICT：严格按照流量标记路由
 * LOOSE：尽量按照路由表及路由
 *
 * @author Created by haoteng on 2023/3/9.
 */
public enum LoadBalanceMode {
    /**
     * 严格模式
     */
    STRICT("STRICT"),
    /**
     * 宽松模式
     */
    LOOSE("LOOSE");

    private String value;

    LoadBalanceMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

package cn.mina.cloud.common;

/**
 * @author Created by haoteng on 2023/3/28.
 */
public interface Constant {

    public static final String DEFAULT_CANARY_PAYLOAD_HEADER = "Default-Canary";

    public static final String GATEWAY_CANARY_ENABLE_HEADER = "Canary-Enable";

    public static final String GATEWAY_CANARY_TYPE_HEADER = "Canary-Type";

    public static final String GATEWAY_CANARY_MODE_HEADER = "Canary-Mode";

    public static final String DEFAULT_CANARY_RULE_HEADER = "Mina-Canary";

    public static final String IS_CANARY_HEADER = "Is-Canary";

    public static final String GATEWAY_CANARY_ENABLE = "mina.cloud.gateway.canary.enable";
    public static final String GATEWAY_CANARY_LOADBALANCER_ENABLE = "mina.cloud.gateway.canary.loadbalancer.enable";

    public static final String GATEWAY_CANARY_LOADBALANCER_IP_ADDRESS = "mina.cloud.gateway.canary.loadbalancer.ip-address";
    public static final String GATEWAY_CANARY_LOADBALANCER_MODE = "mina.cloud.gateway.canary.loadbalancer.mode";
    public static final String GATEWAY_CANARY_RULE_CONFIG_PARAM = "mina.cloud.gateway.canary.rule.config-param";

    public static final String DUBBO_CANARY_RULE_HEADER = "Dubbo-Attachments";

    public static final String LOADBALANCER_MODE = "mina.cloud.loadbalancer.canary.mode";


    public static final String DISCOVERY_CANARY_TAG = "mina.cloud.discovery.canary.tag";

    public static final String STARTUP_TIME = "startup.time";

    public static final String DISCOVERY_CANARY = "mina.cloud.discovery.canary";
    public static final String DISCOVERY_CANARY_ENABLE = DISCOVERY_CANARY + ".enable";

    public static final String LOADBALANCER_CANARY_TYPE = "mina.cloud.loadbalancer.canary.type.enable";



    interface Is {
        public static final String YES = "yes";
        public static final String NO = "no";
    }

    interface Boolean {
        public static final String TRUE = "true";
        public static final String FALSE = "false";
    }

    interface CanaryType{
        public static final String METADATA = "metadata";
        public static final String HOST = "host";
    }


}

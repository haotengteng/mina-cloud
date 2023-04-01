package cn.mina.cloud.dubbo.loadbalancer.canary;

import org.apache.dubbo.rpc.PenetrateAttachmentSelector;
import org.apache.dubbo.rpc.RpcContext;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.mina.cloud.common.Constant.*;

/**
 * 指定灰度参数透传
 * 默认不实现该接口情况下透传所有参数，实现该接口透传select防范返回参数
 * 注：该功能未启用
 *
 * @author Created by haoteng on 2023/3/26.
 */
public class CanaryPenetrateAttachmentSelector implements PenetrateAttachmentSelector {

    private static final Set<String> CANARY_HEADERS = new HashSet<>();
    static {
        CANARY_HEADERS.add(DEFAULT_CANARY_PAYLOAD_HEADER);
        CANARY_HEADERS.add(GATEWAY_CANARY_ENABLE_HEADER);
        CANARY_HEADERS.add(GATEWAY_CANARY_TYPE_HEADER);
        CANARY_HEADERS.add(GATEWAY_CANARY_MODE_HEADER);
        CANARY_HEADERS.add(IS_CANARY_HEADER);
    }

    @Override
    public Map<String, Object> select() {
        Map<String, Object> map = RpcContext.getServerAttachment().getObjectAttachments();
        return map.entrySet().stream()
                .filter(key -> CANARY_HEADERS.contains(key.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

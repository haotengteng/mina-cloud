package cn.mina.cloud.dubbo.loadbalancer.canary;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.router.RouterResult;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.mina.cloud.common.Constant.*;

/**
 * dubbo 金丝雀路由实现，IP模式
 *
 * @author Created by haoteng on 2023/3/24.
 */
public class HostDubboCanaryRouter extends AbstractCanaryRule {

    public HostDubboCanaryRouter() {
    }


    @Override
    public <T> RouterResult<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation,
                                              boolean needToPrintMessage) throws RpcException {
        // 获取流量灰度标记
        String isCanary = RpcContext.getServerAttachment().getAttachment(IS_CANARY_HEADER);
        // 获取流量灰度ip
        String canary = RpcContext.getServerAttachment().getAttachment(DEFAULT_CANARY_PAYLOAD_HEADER);
        // 灰度流量
        if (Is.YES.equals(isCanary)) {
            String mode = RpcContext.getServerAttachment().getAttachment(GATEWAY_CANARY_MODE_HEADER);
            // 获取金丝雀节点
            Stream<String> canaryIps = Stream.of(canary.split(","));
            List<Invoker<T>> canaryInvokers = invokers.stream()
                    .filter(e -> canaryIps.anyMatch(c -> c.equals(e.getUrl().getHost()))).collect(Collectors.toList());
            List<Invoker<T>> invokerList = getInvokersByCanaryMode(canaryInvokers, invokers, mode);
            return new RouterResult<>(invokerList);
        } else {
            // 正常流量
            // 获取非金丝雀节点
            if (StringUtils.isNotBlank(canary)) {
                Stream<String> canaryIps = Stream.of(canary.split(","));
                List<Invoker<T>> noneCanaryInstances = invokers.stream()
                        .filter(e -> canaryIps.noneMatch(c -> c.equals(e.getUrl().getHost())))
                        .collect(Collectors.toList());
                return new RouterResult<>(noneCanaryInstances);
            } else {
                return new RouterResult<>(invokers);
            }
        }
    }
}

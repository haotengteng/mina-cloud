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

import static cn.mina.cloud.common.Constant.*;

/**
 * dubbo 金丝雀路由实现，默认路径参数模式
 *
 * @author Created by haoteng on 2023/3/24.
 */
public class MetaDataDubboCanaryRouter extends AbstractCanaryRule {


    public MetaDataDubboCanaryRouter() {

    }


    @Override
    public <T> RouterResult<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation,
                                              boolean needToPrintMessage) throws RpcException {
        System.out.println("==========matedata=============");
        // 获取流量灰度标记
        String isCanary = RpcContext.getServerAttachment().getAttachment(IS_CANARY_HEADER);
        // 灰度流量
        if (Is.YES.equals(isCanary)) {
            String canary = RpcContext.getServerAttachment().getAttachment(DEFAULT_CANARY_PAYLOAD_HEADER);
            // 获取灰度模式
            String mode = RpcContext.getServerAttachment().getAttachment(GATEWAY_CANARY_MODE_HEADER);
            // 获取金丝雀节点
            List<Invoker<T>> canaryInvokers = invokers.stream().filter(invoker -> {
                String canaryNode = getCanaryTagByUrl(invoker.getUrl());
                return StringUtils.equals(canary, canaryNode);
            }).collect(Collectors.toList());
            // 获取非金丝雀节点
            List<Invoker<T>> normalInvokers = invokers.stream().filter(invoker -> {
                String canaryNode = getCanaryTagByUrl(invoker.getUrl());
                return StringUtils.isBlank(canaryNode);
            }).collect(Collectors.toList());
            List<Invoker<T>> invokerList = getInvokersByCanaryMode(canaryInvokers, normalInvokers, mode);
            return new RouterResult<>(invokerList);
        } else {
            // 正常流量
            // 获取非金丝雀节点
            List<Invoker<T>> normalInvokers = invokers.stream().filter(invoker -> {
                String canaryNode = getCanaryTagByUrl(invoker.getUrl());
                return StringUtils.isBlank(canaryNode);
            }).collect(Collectors.toList());
            return new RouterResult<>(normalInvokers);
        }
    }

    private String getCanaryTagByUrl(URL url) {
        return url.getParameter(DEFAULT_CANARY_PAYLOAD_HEADER);
    }

}

package cn.mina.cloud.dubbo.loadbalancer.canary;

import cn.mina.boot.common.exception.MinaBaseException;
import cn.mina.cloud.common.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.router.AbstractRouter;
import org.apache.dubbo.rpc.cluster.router.RouterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static cn.mina.cloud.common.Constant.GATEWAY_CANARY_ENABLE_HEADER;
import static cn.mina.cloud.common.Constant.GATEWAY_CANARY_TYPE_HEADER;

/**
 * @author Created by haoteng on 2023/4/1.
 */
public class CanaryRouter extends AbstractRouter {

    private static final Logger log = LoggerFactory.getLogger(CanaryRouter.class);

    @Override
    public <T> RouterResult<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation,
                                              boolean needToPrintMessage) throws RpcException {

        String canaryEnable = RpcContext.getServerAttachment().getAttachment(GATEWAY_CANARY_ENABLE_HEADER);

        if (Constant.Boolean.TRUE.equals(canaryEnable)) {
            System.out.println("==========CanaryRouter============");
            String canaryType = RpcContext.getServerAttachment().getAttachment(GATEWAY_CANARY_TYPE_HEADER);
            AbstractCanaryRule canaryRule = AbstractCanaryRule.getInstances(canaryType);
            return canaryRule.route(invokers, url, invocation, needToPrintMessage);
        }
        return new RouterResult<>(invokers);
    }

}

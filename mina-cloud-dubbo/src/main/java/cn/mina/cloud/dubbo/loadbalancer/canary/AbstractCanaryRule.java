package cn.mina.cloud.dubbo.loadbalancer.canary;

import cn.mina.boot.common.exception.MinaBaseException;
import cn.mina.cloud.common.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.router.RouterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Created by haoteng on 2023/4/1.
 */
public abstract class AbstractCanaryRule {

    private static final Logger log = LoggerFactory.getLogger(AbstractCanaryRule.class);
    protected abstract <T> RouterResult<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation,
                                                       boolean needToPrintMessage) throws RpcException;



    private static final ConcurrentHashMap<String, AbstractCanaryRule> canaryRouters = new ConcurrentHashMap<>(2);

    /**
     * 工厂模式 获取指定路由
     *
     * @param canaryType
     * @return
     */
    protected static AbstractCanaryRule getInstances(String canaryType) {
        AbstractCanaryRule canaryRouter = canaryRouters.get(canaryType);
        if (canaryRouter == null) {
            synchronized (canaryRouters) {
                // DDL 校验
                AbstractCanaryRule router = canaryRouters.get(canaryType);
                if (null == router) {
                    switch (canaryType) {
                        case Constant.CanaryType.METADATA:
                            canaryRouters.put(canaryType, new MetaDataDubboCanaryRouter());
                            break;
                        case Constant.CanaryType.HOST:
                            canaryRouters.put(canaryType, new HostDubboCanaryRouter());
                            break;
                        default:
                            log.warn("不支持的路由策略:{}", canaryType);
                            throw new MinaBaseException("路由策略配置有误,请检查。。。");
                    }
                    return canaryRouters.get(canaryType);
                } else {
                    return router;
                }
            }
        } else {
            return canaryRouter;
        }
    }



    protected <T> List<Invoker<T>> getInvokersByCanaryMode(List<Invoker<T>> canaryInvokers, List<Invoker<T>> invokers, String mode) {
        // 宽松模式下，可以路由非金丝雀节点
        if (StringUtils.equals(mode, "LOOSE")) {
            if (CollectionUtils.isEmpty(canaryInvokers)) {
                return invokers;
            }
        }
        return canaryInvokers;
    }
}

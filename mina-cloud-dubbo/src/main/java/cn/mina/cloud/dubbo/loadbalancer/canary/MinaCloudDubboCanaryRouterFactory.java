package cn.mina.cloud.dubbo.loadbalancer.canary;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.cluster.CacheableRouterFactory;
import org.apache.dubbo.rpc.cluster.Router;

import static cn.mina.cloud.common.Constant.GATEWAY_CANARY_ENABLE_HEADER;
import static cn.mina.cloud.common.Constant.GATEWAY_CANARY_TYPE_HEADER;

/**
 * @author Created by haoteng on 2023/3/24.
 */
@Activate
public class MinaCloudDubboCanaryRouterFactory extends CacheableRouterFactory {


    @Override
    protected Router createRouter(URL url) {
        return new CanaryRouter();
    }

}

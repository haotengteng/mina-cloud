package cn.mina.cloud.dubbo.filter;

import cn.mina.boot.common.util.JsonUtil;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Created by haoteng on 2023/3/25.
 */
@Activate(group = {CommonConstants.PROVIDER})
public class MinaCloudDubboInvokerLogFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(MinaCloudDubboInvokerLogFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = invocation.getInvoker().getUrl();
//        ,+JsonUtil.toJSONString(RpcContext.getServerAttachment().getObjectAttachments())
        logger.info("接收到dubbo请求，接口地址:{},请求参数:{},隐式参数:{}", url.getUrlAddress().toString(),JsonUtil.toJSONString(invocation.getArguments()),
                JsonUtil.toJSONString(RpcContext.getServerAttachment().getObjectAttachments()));
        Result result = invoker.invoke(invocation);
        logger.info("dubbo请求结束，响应数据:{}",result.getValue());
        return result;
    }
}

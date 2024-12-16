package com.x9x.radp.spring.cloud.dubbo.cluster;

import com.x9x.radp.commons.collections.CollectionUtils;
import com.x9x.radp.commons.net.IpConfigUtils;
import com.x9x.radp.spring.cloud.dubbo.DubboAttachments;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker;
import org.apache.dubbo.rpc.cluster.support.FailoverClusterInvoker;

import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author x9x
 * @since 2024-10-01 22:07
 */
@Slf4j
public class LocalCallFirstCluster implements Cluster {


    @Override
    public <T> Invoker<T> join(Directory<T> directory, boolean buildFilterChain) throws RpcException {
        return new AbstractClusterInvoker<T>(directory) {
            @Override
            protected Result doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance) throws RpcException {
                checkInvokers(invokers, invocation);

                final String host = RpcContext.getContext().getAttachments().containsKey(DubboAttachments.IP) ?
                        RpcContext.getContext().getAttachment(DubboAttachments.IP) :
                        NetUtils.getLocalHost();
                RpcContext.getContext().setAttachment(DubboAttachments.IP, host);

                List<String> invokerHosts = invokers.stream()
                        .map(invoker -> invoker.getUrl().getHost())
                        .collect(Collectors.toList());

                // 如果本地没有启动被调用的服务, 就获取不到本纪地址, 按默认的策略执行
                if (CollectionUtils.isEmpty(invokerHosts) || !invokerHosts.contains(host)) {
                    FailoverClusterInvoker<T> failoverClusterInvoker = new FailoverClusterInvoker<>(directory);
                    // 过滤同一子网的地址,防止和其他开发人员冲突
                    List<Invoker<T>> filteredInvokers = invokers.stream()
                            .filter(invoker -> {
                                try {
                                    if (IpConfigUtils.isSameSubnet(invoker.getUrl().getHost(), host)) {
                                        return false;
                                    }
                                } catch (UnknownHostException e) {
                                    throw new RuntimeException(e);
                                }
                                return true;
                            })
                            .collect(Collectors.toList());
                    // 如果公共环境没有运行相关服务, 按默认的策略执行
                    if (CollectionUtils.isEmpty(filteredInvokers)) {
                        log.warn("All same-subnet matches were found, resulting in an empty filter for local calls in the first cluster.");
                        return failoverClusterInvoker.doInvoke(invocation, invokers, loadbalance);
                    }
                    return failoverClusterInvoker.doInvoke(invocation, filteredInvokers, loadbalance);
                }

                Invoker<T> invoked = invokers.stream()
                        .filter(invoker -> {
                            if (invoker.getUrl().getHost().equals(host)) {
                                log.debug("Local call first cluster select host: {}", host);
                                return true;
                            }
                            return false;
                        }).findFirst()
                        .orElseThrow(() -> new RpcException("Local call first cluster invoke failed"));
                return invoked.invoke(invocation);
            }
        };
    }
}

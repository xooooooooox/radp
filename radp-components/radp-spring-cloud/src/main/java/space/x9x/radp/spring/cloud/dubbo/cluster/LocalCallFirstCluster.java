/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.spring.cloud.dubbo.cluster;

import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker;
import org.apache.dubbo.rpc.cluster.support.FailoverClusterInvoker;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.cloud.dubbo.DubboAttachments;

/**
 * 本地优先调用集群实现
 * <p>
 * 该集群实现会优先选择本地服务进行调用，如果本地没有对应服务，则会按照以下策略选择远程服务： 1. 过滤掉与本地在同一子网的服务（避免与其他开发人员环境冲突） 2.
 * 如果过滤后没有可用服务，则使用默认的故障转移策略
 *
 * @author IO x9x
 * @since 2024-10-01 22:07
 */
@Slf4j
public class LocalCallFirstCluster implements Cluster {

	@Override
	public <T> Invoker<T> join(Directory<T> directory, boolean buildFilterChain) throws RpcException {
		return new AbstractClusterInvoker<T>(directory) {
			@Override
			protected Result doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance)
					throws RpcException {
				// 检查调用者列表是否为空
				checkInvokers(invokers, invocation);

				// 获取本地主机地址
				final String localHost = getLocalHost();

				// 获取所有可用服务的主机地址列表
				List<String> invokerHosts = getInvokerHosts(invokers);

				// 如果本地没有启动被调用的服务，则使用远程服务
				if (CollectionUtils.isEmpty(invokerHosts) || !invokerHosts.contains(localHost)) {
					return invokeRemoteService(directory, invocation, invokers, loadbalance, localHost);
				}

				// 优先使用本地服务
				return invokeLocalService(invocation, invokers, localHost);
			}

			/**
			 * 获取本地主机地址
			 * <p>
			 * 优先从RPC上下文中获取IP地址，如果没有则使用本地主机地址
			 * @return 本地主机地址
			 */
			private String getLocalHost() {
				// 使用ClientAttachment替代已废弃的getContext方法
				final String host = RpcContext.getClientAttachment().getAttachment(DubboAttachments.IP) != null
						? RpcContext.getClientAttachment().getAttachment(DubboAttachments.IP) : NetUtils.getLocalHost();
				// 将主机地址保存到RPC上下文中
				RpcContext.getClientAttachment().setAttachment(DubboAttachments.IP, host);
				return host;
			}

			/**
			 * 获取所有可用服务的主机地址列表
			 * @param invokers 调用者列表
			 * @return 主机地址列表
			 */
			private List<String> getInvokerHosts(List<Invoker<T>> invokers) {
				return invokers.stream().map(invoker -> invoker.getUrl().getHost()).collect(Collectors.toList());
			}

			/**
			 * 调用远程服务
			 * <p>
			 * 过滤掉与本地在同一子网的服务，避免与其他开发人员环境冲突
			 * @param directory 服务目录
			 * @param invocation 调用信息
			 * @param invokers 调用者列表
			 * @param loadbalance 负载均衡策略
			 * @param localHost 本地主机地址
			 * @return 调用结果
			 */
			private Result invokeRemoteService(Directory<T> directory, Invocation invocation, List<Invoker<T>> invokers,
					LoadBalance loadbalance, String localHost) {

				FailoverClusterInvoker<T> failoverClusterInvoker = new FailoverClusterInvoker<>(directory);

				// 过滤同一子网的地址，防止和其他开发人员环境冲突
				List<Invoker<T>> filteredInvokers = filterSameSubnetInvokers(invokers, localHost);

				// 如果过滤后没有可用服务，则使用原始调用者列表
				if (CollectionUtils.isEmpty(filteredInvokers)) {
					log.warn("所有服务都在同一子网内，无法过滤。使用原始调用者列表进行故障转移调用。");
					return failoverClusterInvoker.doInvoke(invocation, invokers, loadbalance);
				}

				return failoverClusterInvoker.doInvoke(invocation, filteredInvokers, loadbalance);
			}

			/**
			 * 过滤与本地在同一子网的服务
			 * @param invokers 调用者列表
			 * @param localHost 本地主机地址
			 * @return 过滤后的调用者列表
			 */
			private List<Invoker<T>> filterSameSubnetInvokers(List<Invoker<T>> invokers, String localHost) {
				return invokers.stream()
					.filter(invoker -> !isInSameSubnet(invoker.getUrl().getHost(), localHost))
					.collect(Collectors.toList());
			}

			/**
			 * 判断两个主机是否在同一子网
			 * @param host 主机地址
			 * @param localHost 本地主机地址
			 * @return 是否在同一子网
			 */
			private boolean isInSameSubnet(String host, String localHost) {
				try {
					return IpConfigUtils.isSameSubnet(host, localHost);
				}
				catch (UnknownHostException e) {
					log.warn("判断子网时发生异常: {}", e.getMessage());
					return false; // 发生异常时默认不在同一子网
				}
			}

			/**
			 * 调用本地服务
			 * @param invocation 调用信息
			 * @param invokers 调用者列表
			 * @param localHost 本地主机地址
			 * @return 调用结果
			 */
			private Result invokeLocalService(Invocation invocation, List<Invoker<T>> invokers, String localHost) {
				Invoker<T> localInvoker = findLocalInvoker(invokers, localHost);
				return localInvoker.invoke(invocation);
			}

			/**
			 * 查找本地调用者
			 * @param invokers 调用者列表
			 * @param localHost 本地主机地址
			 * @return 本地调用者
			 * @throws RpcException 如果找不到本地调用者
			 */
			private Invoker<T> findLocalInvoker(List<Invoker<T>> invokers, String localHost) {
				return invokers.stream()
					.filter(invoker -> invoker.getUrl().getHost().equals(localHost))
					.findFirst()
					.map(invoker -> {
						log.debug("本地优先调用集群选择主机: {}", localHost);
						return invoker;
					})
					.orElseThrow(() -> new RpcException("本地优先调用集群调用失败：找不到本地调用者"));
			}
		};
	}

}
